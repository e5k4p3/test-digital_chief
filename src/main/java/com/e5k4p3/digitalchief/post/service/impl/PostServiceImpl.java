package com.e5k4p3.digitalchief.post.service.impl;

import com.e5k4p3.digitalchief.comment.model.Comment;
import com.e5k4p3.digitalchief.comment.dao.CommentRepository;
import com.e5k4p3.digitalchief.exceptions.EntityNotFoundException;
import com.e5k4p3.digitalchief.exceptions.ForbiddenOperationException;
import com.e5k4p3.digitalchief.post.dao.PostRepository;
import com.e5k4p3.digitalchief.post.dto.PostRequestDto;
import com.e5k4p3.digitalchief.post.dto.PostResponseDto;
import com.e5k4p3.digitalchief.post.model.Post;
import com.e5k4p3.digitalchief.post.model.PostSort;
import com.e5k4p3.digitalchief.post.service.PostService;
import com.e5k4p3.digitalchief.post.utils.PostMapper;
import com.e5k4p3.digitalchief.user.dao.UserRepository;
import com.e5k4p3.digitalchief.user.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static com.e5k4p3.digitalchief.post.model.PostSort.CREATED;
import static com.e5k4p3.digitalchief.post.model.PostSort.VIEWS;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostServiceImpl implements PostService {
    final PostRepository postRepository;
    final UserRepository userRepository;
    final CommentRepository commentRepository;
    final EntityManager entityManager;

    @Override
    @Transactional
    public PostResponseDto createPost(PostRequestDto postRequestDto, Long requesterId) {
        Post postToCreate = postRepository.save(PostMapper.toPost(postRequestDto, checkUserExistence(requesterId)));
        log.info("Пользователь с id " + requesterId + " создал пост с названием " + postToCreate.getTitle() +
                " и был сохранен под id " + postToCreate.getId());
        return PostMapper.toPostResponseDto(postToCreate);
    }

    @Override
    @Transactional
    public PostResponseDto updatePost(PostRequestDto postRequestDto, Long postId, Long requesterId) {
        Post postToUpdate = checkPostExistence(postId);
        checkPostAuthor(postToUpdate, requesterId);
        String commonPhrase = "У поста с id " + postId;
        if (postRequestDto.getTitle() != null && !postRequestDto.getTitle().isBlank()) {
            String oldTitle = postToUpdate.getTitle();
            String newTitle = postRequestDto.getTitle();
            postToUpdate.setTitle(newTitle);
            log.info(commonPhrase + " было изменено название с " + oldTitle + " на " + newTitle + ".");
            postToUpdate.setEdited(true);
        }
        if (postRequestDto.getContent() != null && !postRequestDto.getContent().isBlank()) {
            String oldContent = postToUpdate.getContent();
            String newContent = postRequestDto.getContent();
            postToUpdate.setContent(newContent);
            log.info(commonPhrase + " был изменен контент с " + oldContent + " на " + newContent + ".");
            postToUpdate.setEdited(true);
        }
        List<Comment> comments = commentRepository.findAllByPostIdOrderByCreatedDesc(postId);
        postToUpdate.setComments(comments);
        return PostMapper.toPostResponseDto(postToUpdate);
    }

    @Override
    @Transactional
    public void deletePost(Long postId, Long requesterId) {
        Post postToDelete = checkPostExistence(postId);
        checkPostAuthor(postToDelete, requesterId);
        postRepository.deleteById(postId);
        log.info("Пост с id " + postId + " был удален.");
    }

    @Override
    @Transactional
    public PostResponseDto getPostById(Long postId) {
        Post postToGet = checkPostExistence(postId);
        List<Comment> comments = commentRepository.findAllByPostIdOrderByCreatedDesc(postId);
        postToGet.setComments(comments);
        postToGet.setViews(postToGet.getViews() + 1);
        log.info("Возвращаем данные о посте с id " + postId + ".");
        return PostMapper.toPostResponseDto(postToGet);
    }

    @Override
    @Transactional
    public List<PostResponseDto> getAllPosts(Integer from, Integer size) {
        log.info("Возвращаем данные обо всех постах без фильтров.");
        return postRepository.findAll(PageRequest.of((from / size), size)).stream()
                .peek(post -> post.setComments(commentRepository.findAllByPostIdOrderByCreatedDesc(post.getId())))
                .peek(post -> post.setViews(post.getViews() + 1))
                .map(PostMapper::toPostResponseDto)
                .sorted(Comparator.comparingLong(PostResponseDto::getViews).reversed())
                .toList();
    }

    @Override
    @Transactional
    public List<PostResponseDto> getAllPostsWithFilters(String text,
                                                        Set<Long> authors,
                                                        PostSort sort,
                                                        Integer from,
                                                        Integer size) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Post> postCriteriaQuery = criteriaBuilder.createQuery(Post.class);
        Root<Post> postRoot = postCriteriaQuery.from(Post.class);
        postCriteriaQuery.select(postRoot);
        List<Predicate> predicates = new ArrayList<>();

        if (text != null) {
            predicates.add(criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(postRoot.get("title").as(String.class)),
                            criteriaBuilder.literal('%' + text.toLowerCase() + '%')),
                    criteriaBuilder.like(criteriaBuilder.lower(postRoot.get("content").as(String.class)),
                            criteriaBuilder.literal('%' + text.toLowerCase() + '%'))));
        }
        if (authors != null && !authors.isEmpty()) {
            Expression<Long> authorIdExpression = postRoot.get("author").get("id");
            predicates.add(authorIdExpression.in(authors));
        }

        postCriteriaQuery.where(predicates.toArray(new Predicate[0]));

        if (sort.equals(CREATED)) {
            postCriteriaQuery.orderBy(criteriaBuilder.desc(postRoot.get("created")));
        }

        TypedQuery<Post> postTypedQuery = entityManager.createQuery(postCriteriaQuery);
        PageRequest pageRequest = PageRequest.of((from / size), size);
        postTypedQuery.setFirstResult(pageRequest.getPageNumber());
        postTypedQuery.setMaxResults(pageRequest.getPageSize());
        List<PostResponseDto> postsToGet = postTypedQuery.getResultList().stream()
                .peek(post -> post.setComments(commentRepository.findAllByPostIdOrderByCreatedDesc(post.getId())))
                .peek(post -> post.setViews(post.getViews() + 1))
                .map(PostMapper::toPostResponseDto)
                .toList();

        log.info("Возвращаем данные обо всех постах с фильтрами поиска.");

        if (sort.equals(VIEWS)) {
            return postsToGet.stream().sorted(Comparator.comparingLong(PostResponseDto::getViews).reversed()).toList();
        }

        return postsToGet;
    }

    @Override
    @Transactional
    public void resetData() {
        entityManager.createNativeQuery("TRUNCATE posts RESTART IDENTITY CASCADE").executeUpdate();
        log.info("Данные обо всех постах удалены.");
    }

    private Post checkPostExistence(Long postId) {
        return postRepository.findById(postId).orElseThrow(
                () -> new EntityNotFoundException("Пост с id " + postId + " не найден."));
    }

    private User checkUserExistence(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("Пользователь с id " + userId + " не найден."));
    }

    private void checkPostAuthor(Post post, Long requesterId) {
        if (!post.getAuthor().getId().equals(checkUserExistence(requesterId).getId())) {
            throw new ForbiddenOperationException("Пользователь с id " + requesterId + " не может изменить пост с id "
                    + post.getId() + ", так как не является его автором.");
        }
    }
}
