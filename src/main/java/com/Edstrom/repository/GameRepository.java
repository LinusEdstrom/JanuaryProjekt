package com.Edstrom.repository;

import com.Edstrom.entity.Game;
import com.Edstrom.entity.Member;

import java.util.List;

public interface GameRepository {

    List<Game> findAll();

    public void save(Game game);
}
