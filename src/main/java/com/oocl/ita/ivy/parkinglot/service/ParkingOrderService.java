package com.oocl.ita.ivy.parkinglot.service;

import com.oocl.ita.ivy.parkinglot.entity.Customer;

import com.oocl.ita.ivy.parkinglot.entity.ParkingBoy;
import com.oocl.ita.ivy.parkinglot.entity.ParkingBoyDTO;
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
    @Autowired
    private ParkingBoyService parkingBoyService;


    /*public ParkingOrder save(String customerUsername, String carNo) {
        Customer customer = customerRepository.findByUsername(customerUsername);
        if (customer == null) {
            new BusinessException(BusinessExceptionType.RECODE_NOT_FOUNT);
        }
        ParkingOrder parkingOrder = new ParkingOrder();
        parkingOrder.setCarNo(carNo);
        parkingOrder.setCustomer(customer);
        parkingOrder.setOrderStatus(OrderStatus.PROGRESSING);
        return orderRepository.save(parkingOrder);
    }*/

    /*public ParkingOrder save(String customerUsername, String carNo) {
        /*
        * 1.先把订单分配给有停车空位，并且是open的parkingboy
        * 2.如果没有open的，就把订单给stop的parkingboy
        * 3.没有就返回
        *
        ParkingBoy parkingBoy = parkingBoyService


    }*/




}
