package com.e5k4p3.digitalchief.comment.service.impl;

import com.e5k4p3.digitalchief.comment.dao.CommentRepository;
import com.e5k4p3.digitalchief.comment.dto.CommentRequestDto;
import com.e5k4p3.digitalchief.comment.dto.CommentResponseDto;
import com.e5k4p3.digitalchief.comment.model.Comment;
import com.e5k4p3.digitalchief.comment.service.CommentService;
import com.e5k4p3.digitalchief.comment.util.CommentMapper;
import com.e5k4p3.digitalchief.exceptions.EntityNotFoundException;
import com.e5k4p3.digitalchief.exceptions.ForbiddenOperationException;
import com.e5k4p3.digitalchief.post.dao.PostRepository;
import com.e5k4p3.digitalchief.post.model.Post;
import com.e5k4p3.digitalchief.user.dao.UserRepository;
import com.e5k4p3.digitalchief.user.model.User;
import jakarta.persistence.EntityManager;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentServiceImpl implements CommentService {
    final CommentRepository commentRepository;
    final PostRepository postRepository;
    final UserRepository userRepository;
    final EntityManager entityManager;

    @Override
    @Transactional
    public CommentResponseDto createComment(CommentRequestDto commentRequestDto, Long requesterId, Long postId) {
        User author = checkUserExistence(requesterId);
        Post post = checkPostExistence(postId);
        Comment commentToCreate = commentRepository.save(CommentMapper.toComment(commentRequestDto, author, post));
        log.info("Комментарий от пользователя с id " + requesterId + " к посту с id " + postId +
                " был сохранен под id " + commentToCreate.getId() + ".");
        return CommentMapper.toCommentResponseDto(commentToCreate);
    }

    @Override
    @Transactional
    public CommentResponseDto updateComment(CommentRequestDto commentRequestDto, Long commentId, Long requesterId) {
        checkUserExistence(requesterId);
        Comment commentToUpdate = checkCommentExistence(commentId);
        checkCommentAuthor(commentToUpdate, requesterId);
        String oldText = commentToUpdate.getText();
        String newText = commentRequestDto.getText();
        commentToUpdate.setText(newText);
        commentToUpdate.setEdited(true);
        log.info("У комментария с id " + commentId + " был изменен текст с " + oldText + " на " + newText + ".");
        return CommentMapper.toCommentResponseDto(commentToUpdate);
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId, Long requesterId) {
        checkCommentAuthor(checkCommentExistence(commentId), checkUserExistence(requesterId).getId());
        commentRepository.deleteById(commentId);
        log.info("Комментарий с id " + commentId + " был удален.");
    }

    @Override
    @Transactional(readOnly = true)
    public CommentResponseDto getCommentById(Long commentId) {
        Comment commentToGet = checkCommentExistence(commentId);
        log.info("Возвращаем данные о комментарии с id " + commentId + ".");
        return CommentMapper.toCommentResponseDto(commentToGet);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentResponseDto> getAllCommentsByPostId(Long postId) {
        checkPostExistence(postId);
        log.info("Возвращаем все комментарии к посту с id " + postId + ".");
        return commentRepository.findAllByPostIdOrderByCreatedDesc(postId).stream()
                .map(CommentMapper::toCommentResponseDto)
                .toList();
    }

    @Override
    @Transactional
    public void resetData() {
        entityManager.createNativeQuery("TRUNCATE comments RESTART IDENTITY CASCADE").executeUpdate();
        log.info("Данные обо всех комментариях удалены.");
    }

    private User checkUserExistence(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("Пользователь с id " + userId + " не найден."));
    }

    private Comment checkCommentExistence(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
                () -> new EntityNotFoundException("Комментарий с id " + commentId + " не найден."));
    }

    private Post checkPostExistence(Long postId) {
        return postRepository.findById(postId).orElseThrow(
                () -> new EntityNotFoundException("Пост с id " + postId + " не найден."));
    }

    private void checkCommentAuthor(Comment comment, Long userId) {
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new ForbiddenOperationException("Пользователь с id " + userId +
                    " не может изменить комментарий с id " + comment.getId() + ", так как не является его автором.");
        }
    }
}
