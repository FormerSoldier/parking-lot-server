package com.oocl.ita.ivy.parkinglot.controller;

import com.oocl.ita.ivy.parkinglot.entity.Customer;
import com.oocl.ita.ivy.parkinglot.entity.User;
import com.oocl.ita.ivy.parkinglot.entity.enums.Role;
import com.oocl.ita.ivy.parkinglot.service.CustomerService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController implements BaseController<Customer,String> {
    @Autowired
    CustomerService customerService;
/*
* username:账号
* password:密码
* phone:手机
* name:昵称
* */
    @PostMapping
    @Override
    public Customer save(@RequestBody Customer customer) {
        System.out.println(customer);
        return customerService.save(customer);
    }

    @GetMapping
    @Override
    public List<Customer> findAll() {
        return customerService.findAll();
    }

    @Override
    public Page<Customer> findAll(Pageable pageable) {
        return null;
    }

    @GetMapping("/{id}")
    @Override
    public Customer findById(@PathVariable String id) throws Exception {
        System.out.println(id);
        Customer customer = customerService.findById(id);

        return customer;
    }

    @Override
    public void deleteById(String s) {

    }

    @Override
    public Customer update(Customer customer) {
        return null;
    }
}
