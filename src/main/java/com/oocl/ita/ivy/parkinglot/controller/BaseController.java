package com.oocl.ita.ivy.parkinglot.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

public interface BaseController<T, ID extends Serializable> {
    @PostMapping
    T save(@RequestBody T t);

    @GetMapping
    List<T> findAll();

    @GetMapping(params = {"page"})
    Page<T> findAll(@PageableDefault(size = 15) Pageable pageable);

    @GetMapping("/{id}")
    T findById(@PathVariable(name = "id") ID id) throws Exception;

    @DeleteMapping("/{id}")
    void deleteById(@PathVariable(name = "id") ID id);

    @PutMapping
    T update(@RequestBody T t);

}
