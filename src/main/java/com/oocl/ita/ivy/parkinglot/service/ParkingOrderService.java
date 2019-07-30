package com.oocl.ita.ivy.parkinglot.service;

import com.oocl.ita.ivy.parkinglot.entity.*;

import com.oocl.ita.ivy.parkinglot.entity.enums.BusinessExceptionType;
import com.oocl.ita.ivy.parkinglot.entity.enums.OrderStatus;
import com.oocl.ita.ivy.parkinglot.entity.enums.ParkingBoyStatus;
import com.oocl.ita.ivy.parkinglot.exception.BusinessException;
import com.oocl.ita.ivy.parkinglot.repository.CustomerRepository;
import com.oocl.ita.ivy.parkinglot.repository.ParkingBoyRepository;
import com.oocl.ita.ivy.parkinglot.repository.ParkingLotRepository;
import com.oocl.ita.ivy.parkinglot.repository.ParkingOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    private ParkingLotRepository parkingLotRepository;
    @Autowired
    private ParkingBoyRepository parkingBoyRepository;


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

    public ParkingOrder customerPark(String customerUsername, String carNo) {

        ParkingOrder parkingOrder = save(customerUsername, carNo);
        /*
         * 1.先把订单分配给有停车空位，并且是open的parkingboy
         * 2.如果没有open的，就把订单给stop的parkingboy
         * 3.没有就返回
         **/
        ParkingBoy parkingBoy = parkingBoyService.getParkingBoyInSomeStatus(ParkingBoyStatus.OPEN.getStatus());
//        List<ParkingBoy> OpenedAndHasFreePlaceParkingBoyList = parkingBoyRepository.findAllByStatus(ParkingBoyStatus.OPEN)
//                .stream()
//                .filter(ParkingBoy::hasFreeParkingLot).collect(Collectors.toList());

        if (parkingBoy == null)
            parkingBoyService.getParkingBoyInSomeStatus(ParkingBoyStatus.STOP.getStatus());
        //找到第一个为有位置的parking_lot
        ParkingLot validParkingLot = null;
        ParkingLot temp = null;
        for (int i = 0; i < parkingBoy.getParkingLotList().size(); i++) {
            temp = parkingBoy.getParkingLotList().get(i);
            if (temp.getCapacity() > temp.getUsedCapacity()) {
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
        if (parkingBoy == null) {
            parkingOrder.setOrderStatus(OrderStatus.PROGRESSING);
        } else {
            parkingOrder.setOrderStatus(OrderStatus.PARK);
        }

        parkingOrder.setParkParkingBoy(parkingBoy);
        parkingOrder.setParkingLot(parkingBoy.getParkingLotList().get(0));

        parkingOrder.setStartTime(new Date());

        return orderRepository.save(parkingOrder);
    }


    public ParkingOrder customerFetch(String fetchId) throws Exception {
        ParkingOrder parkingOrder = orderRepository.findById(fetchId).orElseThrow(() -> new BusinessException(BusinessExceptionType.RECODE_NOT_FOUNT));
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


    public ParkingOrder findById(String id) {
        return orderRepository.findById(id).orElseThrow(() -> new BusinessException(BusinessExceptionType.RECODE_NOT_FOUNT));
    }
}
