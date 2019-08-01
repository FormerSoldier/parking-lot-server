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

import static com.oocl.ita.ivy.parkinglot.entity.enums.BusinessExceptionType.RECODE_NOT_FOUNT;

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
    @Autowired
    private ExpenseRateService expenseRateService;

    public ParkingOrder customerPark(String customerUsername, String carNo) throws BusinessException {
        Customer customer = customerRepository.findByUsername(customerUsername);
        if (customer == null) {
            throw new BusinessException(RECODE_NOT_FOUNT);
        }

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
                throw new BusinessException(BusinessExceptionType.PARKING_BOY_NOT_AVAILABLE);
//                return processingCondition(parkingOrder, OrderStatus.PROGRESSING);
            }
            parkingBoy.setOrderNumInClose(parkingBoy.getOrderNumInClose()+1);
        }else{
            parkingBoy.setOrderNumInOpen(parkingBoy.getOrderNumInOpen()+1);
        }
        ParkingOrder parkingOrder = new ParkingOrder();
        parkingOrder.setCarNo(carNo);
        parkingOrder.setCustomer(customer);
        parkingOrder.setSubmitTime(new Date());


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
        //设置pb为忙
        parkingBoy.setFree(false);


        return orderRepository.save(parkingOrder);
    }


    public ParkingOrder customerFetch(String fetchId) throws Exception {
        ParkingOrder parkingOrder = orderRepository.findById(fetchId).orElseThrow(() -> new BusinessException(RECODE_NOT_FOUNT));
        ParkingLot parkingLot = parkingOrder.getParkingLot();
        List<ParkingBoy> parkingBoyList = parkingBoyService.getParkingBoyByParkingLot(parkingLot.getId(), String.valueOf(ParkingBoyStatus.OPEN));
        if (parkingBoyList.size() == 0) {
            parkingBoyList = parkingBoyService.getParkingBoyByParkingLot(parkingLot.getId(), String.valueOf(ParkingBoyStatus.STOP));
        }
        if (parkingBoyList.size() == 0) {
            throw new BusinessException(BusinessExceptionType.PARKING_BOY_NOT_AVAILABLE);
            // 如果该停车场中，没有空闲的parkingboy，那么修改订单为待取车
            //return processingCondition(parkingOrder, OrderStatus.FETCHING);
        }
        parkingOrder.setOrderStatus(OrderStatus.FETCHING);
        parkingOrder.setFetchParkingBoy(parkingBoyList.get(0));

        parkingBoyList.get(0).setFree(false);

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
        return orderRepository.findById(id).orElseThrow(() -> new BusinessException(RECODE_NOT_FOUNT));
    }


    public ParkingBoyVo parkingBoyPark(String orderId){

        ParkingOrder parkingOrder = orderRepository.findById(orderId).orElseThrow(() ->new BusinessException(RECODE_NOT_FOUNT));
        User user = parkingOrder.getCustomer().getUser();
        Customer customer = parkingOrder.getCustomer();

        ParkingBoyVo parkingBoyVo = new ParkingBoyVo();

        parkingBoyVo.setOrderId(parkingOrder.getId());
        parkingBoyVo.setNumber(parkingOrder.getNumber());
        parkingBoyVo.setUsername(user.getName());
        parkingBoyVo.setPhone(customer.getPhone());
        parkingBoyVo.setCarNo(parkingOrder.getCarNo());
        parkingBoyVo.setSubmitTime(parkingOrder.getSubmitTime());
        parkingBoyVo.setOrderStatus(parkingOrder.getOrderStatus());


        ParkingBoy me = parkingBoyService.getCurrentParkingBoy();
        ParkingLot parkingLot = parkingOrder.getParkingLot();


        parkingOrder.setStartTime(new Date());
        parkingOrder.setOrderStatus(OrderStatus.PARK);
        me.setFree(true);
        //这里再调用系统自动指派订单

        parkingBoyVo.setParkParkingBoyName(me.getName());
        parkingBoyVo.setOrderStatus(parkingOrder.getOrderStatus());
        parkingBoyVo.setParkingLotName(parkingLot.getName());
        parkingBoyVo.setFetchParkingBoyName("");

        orderRepository.save(parkingOrder);
        return parkingBoyVo;
    }


    public ParkingBoyVo parkingBoyFetch(String orderId){

        ParkingOrder parkingOrder = orderRepository.findById(orderId).orElseThrow(() ->new BusinessException(RECODE_NOT_FOUNT));
        User user = parkingOrder.getCustomer().getUser();
        Customer customer = parkingOrder.getCustomer();

        ParkingBoyVo parkingBoyVo = new ParkingBoyVo();

        parkingBoyVo.setOrderId(parkingOrder.getId());
        parkingBoyVo.setNumber(parkingOrder.getNumber());
        parkingBoyVo.setUsername(user.getName());
        parkingBoyVo.setPhone(customer.getPhone());
        parkingBoyVo.setCarNo(parkingOrder.getCarNo());
        parkingBoyVo.setSubmitTime(parkingOrder.getSubmitTime());
        parkingBoyVo.setOrderStatus(parkingOrder.getOrderStatus());


        ParkingBoy me = parkingBoyService.getCurrentParkingBoy();

        ParkingLot parkingLot = parkingOrder.getParkingLot();

        me.setFree(true);

        // 调用系统指派订单

        parkingLot.setUsedCapacity(parkingLot.getUsedCapacity()-1);


        parkingOrder.setOrderStatus(OrderStatus.PAID);
        parkingOrder.setFetchParkingBoy(me);
        parkingOrder.setEndTime(new Date());

        double rate = expenseRateService.getExpenseRate().getExpenseRate();
        long diffHour = TimeUtils.getTwoDateDiffHours(parkingOrder.getStartTime(), parkingOrder.getEndTime());
        diffHour = diffHour == 0 ? 1 : diffHour;
        parkingOrder.setPrice(rate * diffHour);

        parkingBoyVo.setPrice(parkingOrder.getPrice());
        parkingBoyVo.setParkParkingBoyName(parkingOrder.getParkParkingBoy().getName());
        parkingBoyVo.setOrderStatus(parkingOrder.getOrderStatus());
        parkingBoyVo.setParkingLotName(parkingLot.getName());
        parkingBoyVo.setFetchParkingBoyName(me.getName());
        parkingBoyVo.setFetchTime(parkingOrder.getEndTime());

        orderRepository.save(parkingOrder);
        return parkingBoyVo;
    }

    public ParkingOrder processingCondition(ParkingOrder parkingOrder, OrderStatus status){
        parkingOrder.setOrderStatus(status);
        parkingOrder = orderRepository.save(parkingOrder);
        ProgressingDataStore.dataStore.put(parkingOrder.getId(),parkingOrder);
        return parkingOrder;
    }

    //拿PB的订单
    //不要问
    //不准改
    //666
    public ParkingBoyVo getProcessingOrderByParkingBoysId () {
        List<ParkingBoyVo> parkOrder = getMySelfParkOrders();
        List<ParkingBoyVo> fetchOrder = getMySelfFetchOrder();
        if (parkOrder.stream().filter(parkingBoyVo -> parkingBoyVo.getOrderStatus().equals(OrderStatus.ACCEPT)).collect(Collectors.toList()).size() == 0) {
            if (fetchOrder.stream().filter(parkingBoyVo -> parkingBoyVo.getOrderStatus().equals(OrderStatus.ACCEPT_FETCH)).collect(Collectors.toList()).size() == 0) {
                return null;
            } else {
                return fetchOrder.stream().filter(parkingBoyVo -> parkingBoyVo.getOrderStatus().equals(OrderStatus.ACCEPT_FETCH)).collect(Collectors.toList()).get(0);
            }
        } else {
            return parkOrder.stream().filter(parkingBoyVo -> parkingBoyVo.getOrderStatus().equals(OrderStatus.ACCEPT)).collect(Collectors.toList()).get(0);
        }
    }
}
