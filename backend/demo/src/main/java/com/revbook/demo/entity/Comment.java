package com.revbook.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

/*
    Class to model comments on social media posts and other comments.
    Comments must be made by users.
    Comments can have reactions and child comments.
    The existence of child comments means parent comments may also exist.
*/

@Entity
@Data
@Table(name="comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @ManyToOne
    @JoinColumn(name="poster_id", nullable = false)
    private User poster;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    private String commentText;

    @CreationTimestamp
    private Date timePosted;

    @ManyToOne
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> childComments = new HashSet<>();

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Reaction> reactionSet = new HashSet<>();

}
