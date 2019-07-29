package com.oocl.ita.ivy.parkinglot.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface BaseService<T, ID extends Serializable> {
    T save(T t);

    List<T> findAll();

    void deleteById(ID id);

    Optional<T> findById(ID id);

    Page<T> findAll(Pageable pageable);
}
