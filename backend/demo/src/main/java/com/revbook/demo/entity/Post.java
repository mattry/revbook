package com.revbook.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/*
    Class to model social media posts.
    Posts must be posted by a user, posts contain text and have timestamps.
    Posts can also have comments and reactions.
*/

@Data
@Entity
@Table(name="posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @ManyToOne
    @JoinColumn(name="poster_id", nullable = false)
    private User poster;

    private String postText;
    private Long timePosted;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> commentSet = new HashSet<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Reaction> reactionSet = new HashSet<>();

}
