package com.oocl.ita.ivy.parkinglot.service;

import com.oocl.ita.ivy.parkinglot.entity.Customer;

import com.oocl.ita.ivy.parkinglot.entity.ParkingOrder;
import com.oocl.ita.ivy.parkinglot.entity.enums.BusinessExceptionType;
import com.oocl.ita.ivy.parkinglot.entity.enums.OrderStatus;
import com.oocl.ita.ivy.parkinglot.exception.BusinessException;
import com.oocl.ita.ivy.parkinglot.repository.CustomerRepository;
import com.oocl.ita.ivy.parkinglot.repository.ParkingOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParkingOrderService {

    @Autowired
    private ParkingOrderRepository orderRepository;
    @Autowired
    private CustomerRepository customerRepository;

    public ParkingOrder save(ParkingOrder order, String customerUsername) {
        Customer customer = customerRepository.findByUsername(customerUsername);
        if (customer == null) {
            new BusinessException(BusinessExceptionType.RECODE_NOT_FOUNT);
        }
        order.setCustomer(customer);
        order.setOrderStatus(OrderStatus.PROGRESSING);
        return orderRepository.save(order);
    }

}
