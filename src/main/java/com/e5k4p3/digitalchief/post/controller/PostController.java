package com.e5k4p3.digitalchief.post.controller;

import com.e5k4p3.digitalchief.post.dto.PostRequestDto;
import com.e5k4p3.digitalchief.post.dto.PostResponseDto;
import com.e5k4p3.digitalchief.post.model.PostSort;
import com.e5k4p3.digitalchief.post.service.PostService;
import com.e5k4p3.digitalchief.utils.Create;
import com.e5k4p3.digitalchief.utils.Update;
import com.e5k4p3.digitalchief.utils.ValidationErrorsHandler;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostController {
    final PostService postService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostResponseDto createPost(@Validated(Create.class) @RequestBody PostRequestDto postRequestDto,
                                      BindingResult bindingResult,
                                      @RequestHeader("X-Requester-Id") Long requesterId) {
        log.debug("Получен POST запрос на добавление поста от пользователя с id " + requesterId + ".");
        ValidationErrorsHandler.logValidationErrors(bindingResult);
        return postService.createPost(postRequestDto, requesterId);
    }

    @PatchMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public PostResponseDto updatePost(@Validated(Update.class) @RequestBody PostRequestDto postRequestDto,
                                      BindingResult bindingResult,
                                      @PathVariable Long postId,
                                      @RequestHeader("X-Requester-Id") Long requesterId) {
        log.debug("Получен PATCH запрос на изменение поста с id " + postId +
                " от пользователя с id " + requesterId + ".");
        ValidationErrorsHandler.logValidationErrors(bindingResult);
        return postService.updatePost(postRequestDto, postId, requesterId);
    }

    @DeleteMapping("/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable Long postId,
                           @RequestHeader("X-Requester-Id") Long requesterId) {
        log.debug("Получен DELETE запрос на удаление поста с id " + postId +
                " от пользователя с id " + requesterId + ".");
        postService.deletePost(postId, requesterId);
    }

    @GetMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public PostResponseDto getPostById(@PathVariable Long postId) {
        log.debug("Получен GET запрос на получение поста с id " + postId + ".");
        return postService.getPostById(postId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PostResponseDto> getAllPosts(@RequestParam(required = false, defaultValue = "0") Integer from,
                                             @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.debug("Получен GET запрос на получение всех постов без фильтров.");
        return postService.getAllPosts(from, size);
    }

    @GetMapping("/filters")
    @ResponseStatus(HttpStatus.OK)
    public List<PostResponseDto> getAllPostsWithFilters(@RequestParam(required = false) String text,
                                                        @RequestParam(required = false) Set<Long> authors,
                                                        @RequestParam(required = false, defaultValue = "VIEWS") PostSort sort,
                                                        @RequestParam(required = false, defaultValue = "0") Integer from,
                                                        @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.debug("Получен GET запрос на получение всех постов с фильтрами.");
        return postService.getAllPostsWithFilters(text, authors, sort, from, size);
    }

    @DeleteMapping("/debug")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void resetData() {
        log.debug("Получен DELETE запрос на удаление всех постов.");
        postService.resetData();
    }
}
