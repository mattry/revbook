package com.revbook.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

/*
    Class to model a reaction on social media posts/comments.
    Reactions must be made by users.
    As of current, there is two types of reactions like or dislike.
*/

@Data
@Entity
@Table(name="reactions")
public class Reaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reactionId;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne
    @JoinColumn(name = "reacter_id", nullable = false)
    private User reacter;

    @Enumerated(EnumType.STRING)
    private ReactionType reactionType;

    public enum ReactionType {
        LIKE,
        DISLIKE;
    }

}
