package com.e5k4p3.digitalchief.comment.dao;

import com.e5k4p3.digitalchief.comment.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPostIdOrderByCreatedDesc(Long postId);
}
