package com.Edstrom.repository;

import com.Edstrom.entity.Member;
import com.Edstrom.entity.Movie;

import java.util.List;

public interface MovieRepository {

    List<Movie> findAll();

    void save(Movie movie);
}
