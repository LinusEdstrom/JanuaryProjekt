package com.edstrom.repository;

import com.edstrom.entity.Game;

import java.util.List;

public interface GameRepository {

    List<Game> findAll();

    public void save(Game game);
}
