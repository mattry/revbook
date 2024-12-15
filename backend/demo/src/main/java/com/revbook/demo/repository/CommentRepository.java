package com.revbook.demo.repository;

import com.revbook.demo.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<List<Comment>> findByPost_PostId(Long postId);
    @Query("SELECT c FROM Comment c WHERE c.parentComment = :parentComment")
    Optional<List<Comment>> findByParentComment(@Param("parentComment")Comment parentComment);
}
