package com.edstrom.repository;

import com.edstrom.entity.Costume;

import java.util.List;

public interface CostumeRepository {

    List<Costume> findAll();

    void save(Costume costume);
}
