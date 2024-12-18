package com.revbook.demo.repository;

import com.revbook.demo.entity.Comment;
import com.revbook.demo.entity.Post;
import com.revbook.demo.entity.Reaction;
import com.revbook.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    @Query("SELECT r FROM Reaction r WHERE r.post = :post")
    Optional<List<Reaction>> findByPost(@Param("post") Post post);

    @Query("SELECT r FROM Reaction r WHERE r.comment = :comment")
    Optional<List<Reaction>> findByComment(@Param("comment") Comment comment);

    Optional<Reaction> findByReacterAndComment(User reacter, Comment comment);

    Optional<Reaction> findByReacterAndPost(User reacter, Post post);
}
