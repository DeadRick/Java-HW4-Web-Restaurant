package com.example.restaurantnew.repository;

import com.example.restaurantnew.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    Session findBySessionToken(String sessionToken);
}
