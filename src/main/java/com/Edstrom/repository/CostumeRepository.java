package com.Edstrom.repository;

import com.Edstrom.entity.Costume;
import com.Edstrom.entity.Member;

import java.util.List;

public interface CostumeRepository {

    List<Costume> findAll();

    void save(Costume costume);
}
