package com.e5k4p3.digitalchief.post.dao;

import com.e5k4p3.digitalchief.post.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
