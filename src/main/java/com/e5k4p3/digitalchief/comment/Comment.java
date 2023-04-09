package com.e5k4p3.digitalchief.comment;

import com.e5k4p3.digitalchief.post.model.Post;
import com.e5k4p3.digitalchief.user.model.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author", nullable = false)
    User author;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post", nullable = false)
    Post post;
    @Column(name = "text", nullable = false, length = 500)
    String text;
    @Column(name = "created", nullable = false)
    LocalDateTime created;
    @Column(name = "edited", nullable = false)
    Boolean edited;
}
