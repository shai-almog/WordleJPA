package com.debugagent.wordle.db;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class WebUser {
    @Id
    @GeneratedValue
    private Long id;
    private String uuid;

    @OneToMany(mappedBy = "webUser")
    @OrderBy("attempt")
    private List<Game> games;

    public WebUser() {}

    public WebUser(String uuid) {
        this.uuid = uuid;
    }

    public void addGame(Game game) {
        game.setWebUser(this);
        if(games == null) {
            games = List.of(game);
        } else {
            games = new ArrayList<>(games);
            games.add(game);
        }
    }
}
