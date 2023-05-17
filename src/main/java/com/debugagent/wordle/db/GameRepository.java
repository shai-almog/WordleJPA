package com.debugagent.wordle.db;


import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {
    long countByWordAndWebUser(String word, WebUser webUser);
}
