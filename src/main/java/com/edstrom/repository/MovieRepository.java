package com.edstrom.repository;

import com.edstrom.entity.Movie;

import java.util.List;

public interface MovieRepository {

    List<Movie> findAll();

    void save(Movie movie);
}
