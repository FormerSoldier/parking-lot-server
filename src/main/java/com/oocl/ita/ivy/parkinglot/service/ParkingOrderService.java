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
import com.oocl.ita.ivy.parkinglot.util.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
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
    @Autowired
    private CustomerService customerService;


//    public ParkingOrder save(String customerUsername, String carNo) {
//        Customer customer = customerRepository.findByUsername(customerUsername);
//        if (customer == null) {
//            throw new BusinessException(BusinessExceptionType.RECODE_NOT_FOUNT);
//        }
//        ParkingOrder parkingOrder = new ParkingOrder();
//        parkingOrder.setCarNo(carNo);
//        parkingOrder.setCustomer(customer);
//        parkingOrder.setSubmitTime(new Date());
//        parkingOrder.setOrderStatus();
//        return orderRepository.save(parkingOrder);
//    }



    public ParkingOrder customerPark(String customerUsername, String carNo) throws BusinessException {
        Customer customer = customerRepository.findByUsername(customerUsername);
        if (customer == null) {
            throw new BusinessException(BusinessExceptionType.RECODE_NOT_FOUNT);
        }

        ParkingOrder parkingOrder = new ParkingOrder();
        parkingOrder.setCarNo(carNo);
        parkingOrder.setCustomer(customer);
        parkingOrder.setSubmitTime(new Date());
        /*
         * 1.先把订单分配给有停车空位，并且是open的parkingboy列表随机一个
         * 2.如果没有open的，就把订单给stop的parkingboy列表随机一个
         * 3.没有就返回
         **/
        ParkingBoy parkingBoy = parkingBoyService.getParkingBoyInSomeStatus(ParkingBoyStatus.OPEN.getStatus());

        if (parkingBoy == null){
            parkingBoy = parkingBoyService.getParkingBoyInSomeStatus(ParkingBoyStatus.STOP.getStatus());
            if(parkingBoy == null) {
                //订单设置为待处理,并保存到数据库和暂存区中
                return processingCondition(parkingOrder, OrderStatus.PROGRESSING);
            }
            parkingBoy.setOrderNumInClose(parkingBoy.getOrderNumInClose()+1);
        }else{
            parkingBoy.setOrderNumInOpen(parkingBoy.getOrderNumInOpen()+1);
        }

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
        validParkingLot.setUsedCapacity(validParkingLot.getUsedCapacity()+1);
        parkingOrder.setParkParkingBoy(parkingBoy);
        parkingOrder.setParkingLot(validParkingLot);
        // 设置为订单已分配
        parkingOrder.setOrderStatus(OrderStatus.ACCEPT);

        Integer userId = parkingOrder.getCustomer().getUser().getId();
        String number = new SimpleDateFormat("yyyyMMddHH").format(new Date()) + new Random().nextInt(1000) + userId;
        parkingOrder.setNumber(number);

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
            // 如果该停车场中，没有空闲的parkingboy，那么修改订单为待取车
            return processingCondition(parkingOrder, OrderStatus.FETCHING);
        }
        parkingOrder.setOrderStatus(OrderStatus.ACCEPT_FETCH);
        parkingOrder.setFetchParkingBoy(parkingBoyList.get(0));
        parkingOrder.setEndTime(new Date());

        int rate = 1;
        long diffHour = TimeUtils.getTwoDateDiffHours(parkingOrder.getStartTime(), parkingOrder.getEndTime());
        parkingOrder.setPrice(rate * diffHour);
        parkingLot.setUsedCapacity(parkingLot.getUsedCapacity()-1);

        return orderRepository.save(parkingOrder);
    }

    public List<ParkingBoyVo> getMySelfParkOrders() {
        ParkingBoy me = parkingBoyService.getCurrentParkingBoy();
        List<ParkingOrder> parkingOrders = orderRepository.findAll();
        List<ParkingBoyVo> result = new ArrayList<>();
        ParkingBoyVo parkingBoyVo = null;
        for (int i = 0; i < parkingOrders.size(); i++) {
            ParkingBoy parkParkingBoy = parkingOrders.get(i).getParkParkingBoy();
            if (me != null && parkParkingBoy != null && me.getId() == parkParkingBoy.getId()) {
                ParkingOrder parkingOrder = parkingOrders.get(i);
                Customer customer = parkingOrder.getCustomer();
                User user = customer.getUser();
                ParkingBoy fetchParkingBoy = parkingOrder.getFetchParkingBoy();
                ParkingLot parkingLot = parkingOrder.getParkingLot();

                parkingBoyVo = new ParkingBoyVo();

                parkingBoyVo.setOrderId(parkingOrder.getId());
                parkingBoyVo.setNumber(parkingOrder.getNumber());
                parkingBoyVo.setUsername(user.getName());
                parkingBoyVo.setPhone(customer.getPhone());
                parkingBoyVo.setCarNo(parkingOrder.getCarNo());
                parkingBoyVo.setPrice(parkingOrder.getPrice());
                parkingBoyVo.setSubmitTime(parkingOrder.getSubmitTime());
                parkingBoyVo.setParkingLotName(parkingLot.getName());
                parkingBoyVo.setFetchTime(parkingOrder.getFetchTime());
                parkingBoyVo.setParkParkingBoyName(parkParkingBoy == null ? null : parkParkingBoy.getName());
                parkingBoyVo.setFetchParkingBoyName(fetchParkingBoy == null ? null : fetchParkingBoy.getName());
                parkingBoyVo.setOrderStatus(parkingOrder.getOrderStatus());
                result.add(parkingBoyVo);
            }

        }
        return result;
    }

    public List<ParkingBoyVo> getMySelfFetchOrder() {
        ParkingBoy me = parkingBoyService.getCurrentParkingBoy();
        List<ParkingOrder> parkingOrders = orderRepository.findAll();
        List<ParkingBoyVo> result = new ArrayList<>();
        ParkingBoyVo parkingBoyVo = null;
        for (int i = 0; i < parkingOrders.size(); i++) {
            ParkingBoy parkParkingBoy = parkingOrders.get(i).getFetchParkingBoy();
            if (me != null && parkParkingBoy != null && me.getId() == parkParkingBoy.getId()) {
                ParkingOrder parkingOrder = parkingOrders.get(i);
                Customer customer = parkingOrder.getCustomer();
                User user = customer.getUser();
                ParkingBoy fetchParkingBoy = parkingOrder.getFetchParkingBoy();
                ParkingLot parkingLot = parkingOrder.getParkingLot();

                parkingBoyVo = new ParkingBoyVo();

                parkingBoyVo.setOrderId(parkingOrder.getId());
                parkingBoyVo.setNumber(parkingOrder.getNumber());
                parkingBoyVo.setUsername(user.getName());
                parkingBoyVo.setPhone(customer.getPhone());
                parkingBoyVo.setCarNo(parkingOrder.getCarNo());
                parkingBoyVo.setPrice(parkingOrder.getPrice());
                parkingBoyVo.setSubmitTime(parkingOrder.getSubmitTime());
                parkingBoyVo.setParkingLotName(parkingLot.getName());
                parkingBoyVo.setFetchTime(parkingOrder.getFetchTime());
                parkingBoyVo.setParkParkingBoyName(parkParkingBoy == null ? "" : parkParkingBoy.getName());
                parkingBoyVo.setFetchParkingBoyName(fetchParkingBoy == null ? null : fetchParkingBoy.getName());
                parkingBoyVo.setOrderStatus(parkingOrder.getOrderStatus());
                result.add(parkingBoyVo);
            }

        }
        return result;
    }

    public List<ParkingBoyVo> getMySelfAllOrders() {
        List<ParkingBoyVo> parkOrders = getMySelfParkOrders();
        List<ParkingBoyVo> fetchOrders = getMySelfFetchOrder();
        List<ParkingBoyVo> temp = parkOrders.stream().filter((item) -> !fetchOrders.contains(item)).collect(Collectors.toList());
        fetchOrders.addAll(temp);
        parkOrders.sort(Comparator.comparing(o -> o.getSubmitTime().toString()));
        return parkOrders;
    }

    public Page<ParkingOrder> findAll(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    public ParkingOrder findById(String id) {
        return orderRepository.findById(id).orElseThrow(() -> new BusinessException(BusinessExceptionType.RECODE_NOT_FOUNT));
    }

    public ParkingOrder processingCondition(ParkingOrder parkingOrder, OrderStatus status){
        parkingOrder.setOrderStatus(status);
        parkingOrder = orderRepository.save(parkingOrder);
        ProgressingDataStore.dataStore.put(parkingOrder.getId(),parkingOrder);
        return parkingOrder;
    }
}
