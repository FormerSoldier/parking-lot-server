package com.oocl.ita.ivy.parkinglot.service;

import com.itmuch.lightsecurity.jwt.UserOperator;
import com.oocl.ita.ivy.parkinglot.entity.Customer;
import com.oocl.ita.ivy.parkinglot.entity.ParkingOrder;
import com.oocl.ita.ivy.parkinglot.repository.ParkingOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserOrderService {

    @Autowired
    private ParkingOrderRepository parkingOrderRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private UserOperator userOperator;

    public List<ParkingOrder> fetchCurrentUserFetchableOrder() {
        Customer customer = customerService.findByUsername(userOperator.getUser().getUsername());
        return null;
    }
}
