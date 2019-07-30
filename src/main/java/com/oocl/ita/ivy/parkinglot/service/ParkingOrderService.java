package com.oocl.ita.ivy.parkinglot.service;

import com.oocl.ita.ivy.parkinglot.entity.*;

import com.oocl.ita.ivy.parkinglot.entity.enums.BusinessExceptionType;
import com.oocl.ita.ivy.parkinglot.entity.enums.OrderStatus;
import com.oocl.ita.ivy.parkinglot.entity.enums.ParkingBoyStatus;
import com.oocl.ita.ivy.parkinglot.exception.BusinessException;
import com.oocl.ita.ivy.parkinglot.repository.CustomerRepository;
import com.oocl.ita.ivy.parkinglot.repository.ParkingLotRepository;
import com.oocl.ita.ivy.parkinglot.repository.ParkingOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ParkingOrderService {

    @Autowired
    private ParkingOrderRepository orderRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ParkingBoyService parkingBoyService;
    @Autowired
    private ParkingLotRepository parkingLotRepository;


    public ParkingOrder save(String customerUsername, String carNo) {
        Customer customer = customerRepository.findByUsername(customerUsername);
        if (customer == null) {
            throw new BusinessException(BusinessExceptionType.RECODE_NOT_FOUNT);
        }
        ParkingOrder parkingOrder = new ParkingOrder();
        parkingOrder.setCarNo(carNo);
        parkingOrder.setCustomer(customer);
        parkingOrder.setOrderStatus(OrderStatus.PROGRESSING);
        parkingOrder.setSubmitTime(new Date());
        return orderRepository.save(parkingOrder);
    }

    public ParkingOrder CustomerPark(ParkingOrder parkingOrder) {
        /*
        * 1.先把订单分配给有停车空位，并且是open的parkingboy
        * 2.如果没有open的，就把订单给stop的parkingboy
        * 3.没有就返回
        **/
        ParkingBoy parkingBoy = parkingBoyService.getParkingBoyInSomeStatus(ParkingBoyStatus.OPEN.getStatus());
        if(parkingBoy == null)
            parkingBoyService.getParkingBoyInSomeStatus(ParkingBoyStatus.STOP.getStatus());
        if(parkingBoy == null)
            return null;
        parkingOrder.setOrderStatus(OrderStatus.PARK);
        parkingOrder.setParkParkingBoy(parkingBoy);
        parkingOrder.setParkingLot(parkingBoy.getParkingLotList().get(0));
        parkingOrder.setStartTime(new Date());

        return orderRepository.save(parkingOrder);
    }

    public ParkingOrder CustomerFetch(ParkingOrder parkingOrder) throws Exception {
        ParkingLot parkingLot = parkingOrder.getParkingLot();
        List<ParkingBoy> parkingBoyList = parkingBoyService.getParkingBoyByParkingLot(parkingLot.getId(), String.valueOf(ParkingBoyStatus.OPEN));
        if (parkingBoyList.size() == 0) {
            parkingBoyList = parkingBoyService.getParkingBoyByParkingLot(parkingLot.getId(), String.valueOf(ParkingBoyStatus.STOP));
        }
        if (parkingBoyList.size() == 0) {
            return null;
        }

        parkingOrder.setOrderStatus(OrderStatus.PAID);
        parkingOrder.setFetchParkingBoy(parkingBoyList.get(0));
        parkingOrder.setEndTime(new Date());

        return orderRepository.save(parkingOrder);
    }

}
