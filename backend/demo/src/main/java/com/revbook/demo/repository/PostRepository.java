package com.revbook.demo.repository;

import com.revbook.demo.entity.User;
import com.revbook.demo.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<List<Post>> findByPoster(User user);

    @Query("SELECT p FROM Post p WHERE p.poster IN (" +
            "SELECT c.followee FROM Connection c WHERE c.follower = :user)")
    Optional<List<Post>> findByFollowing(@Param("user") User user);

    // get posts whose postText contains the search query in it
    @Query("SELECT p FROM Post p WHERE LOWER(p.postText) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Post> searchPostsByText(@Param("query") String query);
}
