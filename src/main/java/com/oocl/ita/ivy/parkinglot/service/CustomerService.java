package com.oocl.ita.ivy.parkinglot.service;

import com.itmuch.lightsecurity.jwt.UserOperator;
import com.oocl.ita.ivy.parkinglot.entity.Customer;
import com.oocl.ita.ivy.parkinglot.entity.User;
import com.oocl.ita.ivy.parkinglot.entity.enums.BusinessExceptionType;
import com.oocl.ita.ivy.parkinglot.entity.enums.Role;
import com.oocl.ita.ivy.parkinglot.exception.BusinessException;
import com.oocl.ita.ivy.parkinglot.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService implements BaseService<Customer,String> {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    UserService userService;

    @Autowired
    UserOperator userOperator;

    @Override
    public Customer save(Customer customer) {
        User user = null;
        if(customer != null && customer.getUser() != null){
            user = customer.getUser();
            user.setRoles(Role.CUSTOMER.getRole());
        }
        user = userService.register(user,Role.CUSTOMER.getRole());
        customer.setUser(user);
        return customerRepository.save(customer);
    }

    public Customer update(Customer customer) {
        User user = null;
        if(customer == null || customer.getUser() == null){
            return customer;
        }
        user = userService.findById(customer.getUser().getId());

        Customer return_customer = customerRepository.findById(customer.getId()).orElse(null);
        if(return_customer == null)
            return null;
        return_customer.setUser(user);

        return_customer.setVIP(customer.isVIP());
        return_customer.setTimes(customer.getTimes());
        return_customer.setPoint(customer.getPoint());

        return_customer.setUser(user);
        return customerRepository.save(return_customer);
    }



    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public void deleteById(String id) {
        customerRepository.deleteById(id);
    }

    @Override
    public Customer findById(String id) {
        return customerRepository.findById(id).orElseThrow(() -> new BusinessException(BusinessExceptionType.RECODE_NOT_FOUNT));
    }

    @Override
    public Page<Customer> findAll(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }

    public void updateUserDeleteFlag(Integer id){
        userService.updateUserDeleteFlagById(id);
    }

}
