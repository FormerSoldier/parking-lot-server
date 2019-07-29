package com.oocl.ita.ivy.parkinglot.controller;

import com.oocl.ita.ivy.parkinglot.entity.Customer;
import com.oocl.ita.ivy.parkinglot.entity.User;
import com.oocl.ita.ivy.parkinglot.service.CustomerService;
import com.oocl.ita.ivy.parkinglot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
//@RequestMapping(path = "/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private UserService userService;

    @PostMapping(path = "/customers")
    public Customer save(@RequestBody Customer customer) {
        User user = new User();
        user.setName("111");
        user.setUsername("111");
        user.setPassword("123456");
        userService.register(user);
        customer.setUser(user);
        return customerService.save(customer);
    }

    @GetMapping(path = "/customers/{username}")
    public Customer findByUsername(@PathVariable String username) {
        return customerService.findByUsername(username);
    }
}
