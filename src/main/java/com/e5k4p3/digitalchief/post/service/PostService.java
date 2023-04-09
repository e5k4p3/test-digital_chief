package com.e5k4p3.digitalchief.post.service;

import com.e5k4p3.digitalchief.post.dto.PostRequestDto;
import com.e5k4p3.digitalchief.post.dto.PostResponseDto;
import com.e5k4p3.digitalchief.post.model.PostSort;

import java.util.List;
import java.util.Set;

public interface PostService {
    PostResponseDto createPost(PostRequestDto postRequestDto, Long userId);

    PostResponseDto updatePost(PostRequestDto postRequestDto, Long postId, Long requesterId);

    void deletePost(Long postId, Long requesterId);

    PostResponseDto getPostById(Long postId);

    List<PostResponseDto> getAllPosts(Integer from, Integer size);

    List<PostResponseDto> getAllPostsWithFilters(String text,
                                                 Set<Long> authors,
                                                 PostSort sort,
                                                 Integer from,
                                                 Integer size);

    void resetData();
}
