package com.oocl.ita.ivy.parkinglot.service;

import com.oocl.ita.ivy.parkinglot.entity.*;

import com.oocl.ita.ivy.parkinglot.entity.enums.BusinessExceptionType;
import com.oocl.ita.ivy.parkinglot.entity.enums.OrderStatus;
import com.oocl.ita.ivy.parkinglot.entity.enums.ParkingBoyStatus;
import com.oocl.ita.ivy.parkinglot.exception.BusinessException;
import com.oocl.ita.ivy.parkinglot.repository.CustomerRepository;
import com.oocl.ita.ivy.parkinglot.repository.ParkingOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ParkingOrderService {

    @Autowired
    private ParkingLotService parkingLotService;
    @Autowired
    private ParkingOrderRepository orderRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ParkingBoyService parkingBoyService;


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

    public ParkingOrder CustomerPark(String customerUsername, String carNo) {

        ParkingOrder parkingOrder = save(customerUsername,carNo);
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
        //找到第一个为有位置的parking_lot
        ParkingLot validParkingLot = null;
        ParkingLot temp = null;
        for(int i = 0; i < parkingBoy.getParkingLotList().size(); i++){
            temp = parkingBoy.getParkingLotList().get(i);
            if(temp.getCapacity() > temp.getUsedCapacity()){
                validParkingLot = temp;
                break;
            }
        }
        //将parking_lot的used_capacity+1
        System.out.println(validParkingLot);
        parkingLotService.addUsedCapacity(validParkingLot.getId());

        validParkingLot = parkingLotService.findById(validParkingLot.getId());
        parkingBoy = parkingBoyService.findById(parkingBoy.getId());
        parkingOrder.setParkingLot(validParkingLot);
        parkingOrder.setParkParkingBoy(parkingBoy);
        parkingOrder.setOrderStatus(OrderStatus.PARK);
        parkingOrder.setStartTime(new Date());

        return orderRepository.save(parkingOrder);
    }

}
