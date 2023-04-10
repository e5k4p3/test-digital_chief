package com.e5k4p3.digitalchief.post.model;

import com.e5k4p3.digitalchief.comment.model.Comment;
import com.e5k4p3.digitalchief.user.model.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "posts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    Long id;
    @Column(name = "title", nullable = false, length = 200)
    String title;
    @Column(name = "content", nullable = false, length = 10000)
    String content;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author", nullable = false)
    User author;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post")
    List<Comment> comments;
    @Column(name = "created", nullable = false)
    LocalDateTime created;
    @Column(name = "edited", nullable = false)
    Boolean edited;
    @Column(name = "views")
    Long views;
}
