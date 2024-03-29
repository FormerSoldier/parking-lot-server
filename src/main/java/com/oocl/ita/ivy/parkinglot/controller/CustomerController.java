package com.oocl.ita.ivy.parkinglot.controller;

import com.oocl.ita.ivy.parkinglot.entity.Customer;
import com.oocl.ita.ivy.parkinglot.entity.User;
import com.oocl.ita.ivy.parkinglot.entity.enums.Role;
import com.oocl.ita.ivy.parkinglot.service.CustomerService;
import com.oocl.ita.ivy.parkinglot.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.math.NumberUtils;
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

    @Autowired
    UserService userService;

    @PostMapping("/login")
    public String login(@RequestBody User user){
        return userService.login(user);
    }

    @PostMapping
    @Override
    public Customer save(@RequestBody Customer customer) {
        return customerService.save(customer);
    }

    @GetMapping
    @Override
    public List<Customer> findAll() {
        return customerService.findAll();
    }

    @GetMapping(params = {"page"})
    @Override
    public Page<Customer> findAll(Pageable pageable) {
        return customerService.findAll(pageable);
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

    /*
        @DeleteMapping("/{id}")
        @Override
        public void deleteById(@PathVariable String id) {
            return ;
           Customer customer = customerService.findById(id);
            if(customer == null )
                return ;
            System.out.println(customer.getUser().getId());
            customerService.updateUserDeleteFlag(customer.getUser().getId());
        }
    */
    @PutMapping
    @Override
    public Customer update(@RequestBody Customer customer) {
        return customerService.save(customer);
    }


    @GetMapping("/me")
    public Customer getCurrentCustomer(){
        return customerService.findBycurrentCustomer();
    }
}
