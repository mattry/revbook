package com.revbook.demo.repository;

import com.revbook.demo.entity.Connection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConnectionRepository extends JpaRepository<Connection, Long> {
}
