package com.oocl.ita.ivy.parkinglot.controller;

import com.oocl.ita.ivy.parkinglot.entity.User;
import com.oocl.ita.ivy.parkinglot.repository.HrRepository;
import com.oocl.ita.ivy.parkinglot.service.HrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hrs")
public class HrController {

    @Autowired
    private HrService hrService;

    @Autowired
    private HrRepository hrRepository;

    @PostMapping
    public User save(@RequestBody  User hr) {
        return hrService.save(hr);
    }

    @GetMapping(params = {"page"})
    public Page<User> findAll(@PageableDefault(size = 15)Pageable pageable) {
        return hrService.findAll(pageable);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Integer id) {
        hrService.deleteById(id);
    }

    @PutMapping
    public User update(@RequestBody User hr) {
        return hrService.update(hr);
    }

}
