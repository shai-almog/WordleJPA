package com.debugagent.wordle.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<WebUser, Long> {
    Optional<WebUser> findByUuid(String uuid);
}
