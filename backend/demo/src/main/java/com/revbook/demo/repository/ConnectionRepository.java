package com.revbook.demo.repository;

import com.revbook.demo.entity.Connection;
import com.revbook.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ConnectionRepository extends JpaRepository<Connection, Long> {
    boolean existsByFollowerAndFollowee(User follower, User followee);

    @Query("SELECT c FROM Connection c WHERE c.followee = :followee")
    List<Connection> findByFollowee(@Param("followee") User followee);

    @Query("SELECT c FROM Connection c WHERE c.follower = :follower")
    List<Connection> findByFollower(@Param("follower") User follower);

    Connection findByFollowerAndFollowee(User follower, User followee);
}
