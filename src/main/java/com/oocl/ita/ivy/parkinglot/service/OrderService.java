package com.oocl.ita.ivy.parkinglot.service;

import com.oocl.ita.ivy.parkinglot.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;


}
