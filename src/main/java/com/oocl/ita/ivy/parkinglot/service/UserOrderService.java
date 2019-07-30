package com.oocl.ita.ivy.parkinglot.service;

import com.itmuch.lightsecurity.jwt.UserOperator;
import com.oocl.ita.ivy.parkinglot.entity.Customer;
import com.oocl.ita.ivy.parkinglot.entity.ParkingOrder;
import com.oocl.ita.ivy.parkinglot.entity.enums.BusinessExceptionType;
import com.oocl.ita.ivy.parkinglot.entity.enums.OrderStatus;
import com.oocl.ita.ivy.parkinglot.exception.BusinessException;
import com.oocl.ita.ivy.parkinglot.repository.CustomerRepository;
import com.oocl.ita.ivy.parkinglot.repository.ParkingOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserOrderService {

    @Autowired
    private ParkingOrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public List<ParkingOrder> getOrdersByCustomerIdByAll(String customerId) {
        List<ParkingOrder> parkingOrders = orderRepository.findByCustomerId(customerId);
        return parkingOrders;
    }

    public List<ParkingOrder> getOrdersByCustomerIdByFetchable(String customerId) {
        List<ParkingOrder> parkingOrders = getOrdersByCustomerIdByAll(customerId);
        List<ParkingOrder> parkingOrdersByFetchable = new ArrayList<>();
        for (ParkingOrder p : parkingOrders) {
            if (p.getOrderStatus().equals(OrderStatus.PARK)) {
                parkingOrdersByFetchable.add(p);
            }
        }
        return parkingOrdersByFetchable;
    }
}
