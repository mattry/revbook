package com.revbook.demo.repository;

import com.revbook.demo.entity.Connection;
import com.revbook.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface ConnectionRepository extends JpaRepository<Connection, Long> {
    boolean existsByFollowerAndFollowee(User follower, User followee);
    Optional<Set<Connection>> findByFollowee(User followee);
    Optional<Set<Connection>> findByFollower(User follower);
    Connection findByFollowerAndFollowee(User follower, User followee);
}
