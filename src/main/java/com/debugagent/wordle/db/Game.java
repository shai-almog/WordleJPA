package com.debugagent.wordle.db;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 5)
    private String guess;

    @Column(length = 5)
    private String word;
    private int attempt;

    @ManyToOne()
    @JoinColumn(name="user_id", nullable=false)
    private WebUser webUser;
}
