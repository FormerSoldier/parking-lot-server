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

    public ParkingOrder customerSubmitParkParkingOrder(String customerUsername, String carNo){
        Customer customer = customerRepository.findByUsername(customerUsername);
        if (customer == null) {
            throw new BusinessException(RECODE_NOT_FOUNT);
        }
        ParkingOrder parkingOrder = new ParkingOrder();
        parkingOrder.setCarNo(carNo);
        parkingOrder.setCustomer(customer);
        parkingOrder.setSubmitTime(new Date());
        return systemAssignOrder(parkingOrder);
    }

    public ParkingOrder systemAssignOrder(ParkingOrder parkingOrder) throws BusinessException {
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

        // calculate the number by user_id now_date and a random number in range of 1,1000
        Integer userId = parkingOrder.getCustomer().getUser().getId();
        String number = new SimpleDateFormat("yyyyMMddHH").format(new Date()) + new Random().nextInt(1000) + userId;
        parkingOrder.setNumber(number);
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
            // 如果该停车场中，没有空闲的parkingboy，那么修改订单为待取车
            return processingCondition(parkingOrder, OrderStatus.FETCHING);
        }
        // calculate price
        parkingOrder.setEndTime(new Date());

        double rate = expenseRateService.getExpenseRate().getExpenseRate();
        parkingOrder.setOrderStatus(OrderStatus.ACCEPT_FETCH);
        parkingOrder.setFetchParkingBoy(parkingBoyList.get(0));

        long diffHour = TimeUtils.getTwoDateDiffHours(parkingOrder.getStartTime(), parkingOrder.getEndTime());
        diffHour = diffHour == 0 ? 1 : diffHour;
        parkingOrder.setPrice(rate * diffHour);

        parkingBoyList.get(0).setFree(false);
        return orderRepository.save(parkingOrder);
    }

    public List<ParkingBoyVo> getMySelfParkOrders() {
        ParkingBoy me = parkingBoyService.getCurrentParkingBoy();
        List<ParkingOrder> parkingOrders = orderRepository.findAll();
        List<ParkingBoyVo> result = new ArrayList<>();
        ParkingBoyVo parkingBoyVo;
        for (int i = 0; i < parkingOrders.size(); i++) {
            ParkingBoy parkParkingBoy = parkingOrders.get(i).getParkParkingBoy();
            if (me != null && parkParkingBoy != null && me.getId().equals(parkParkingBoy.getId())) {
                ParkingOrder parkingOrder = parkingOrders.get(i);
                parkingBoyVo = getParkingBoyVoByParkingOrder(parkingOrder);
                parkingBoyVo.setParkParkingBoyName(parkParkingBoy.getName());
                parkingBoyVo.setFetchParkingBoyName(parkingOrder.getFetchParkingBoy() == null ? "" :parkingOrder.getFetchParkingBoy().getName());
                result.add(parkingBoyVo);
            }

        }
        return result;
    }

    public List<ParkingBoyVo> getMySelfFetchOrder() {
        ParkingBoy me = parkingBoyService.getCurrentParkingBoy();
        List<ParkingOrder> parkingOrders = orderRepository.findAll();
        List<ParkingBoyVo> result = new ArrayList<>();
        ParkingBoyVo parkingBoyVo;
        for (int i = 0; i < parkingOrders.size(); i++) {
            ParkingBoy fetchParkingBoy = parkingOrders.get(i).getFetchParkingBoy();
            if (me != null && fetchParkingBoy != null && me.getId().equals(fetchParkingBoy.getId())) {
                ParkingOrder parkingOrder = parkingOrders.get(i);
                parkingBoyVo = getParkingBoyVoByParkingOrder(parkingOrder);
                parkingBoyVo.setParkParkingBoyName(parkingOrder.getParkParkingBoy() == null ? "" : parkingOrder.getParkParkingBoy().getName());
                parkingBoyVo.setFetchParkingBoyName(fetchParkingBoy.getName());
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
        fetchOrders.sort((o1, o2) -> o2.getSubmitTime().toString().compareTo(o1.getSubmitTime().toString()));
        return fetchOrders;
    }

    public Page<ParkingOrder> findAll(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    public ParkingOrder findById(String id) {
        return orderRepository.findById(id).orElseThrow(() -> new BusinessException(RECODE_NOT_FOUNT));
    }


    public ParkingBoyVo parkingBoyPark(String orderId){

        ParkingOrder parkingOrder = orderRepository.findById(orderId).orElseThrow(() ->new BusinessException(RECODE_NOT_FOUNT));
        ParkingBoyVo parkingBoyVo = getParkingBoyVoByParkingOrder(parkingOrder);
        ParkingBoy me = parkingBoyService.getCurrentParkingBoy();

        parkingOrder.setStartTime(new Date());
        parkingOrder.setOrderStatus(OrderStatus.PARK);
        me.setFree(true);
        //这里再调用系统自动指派订单
        newThreadToAssignOrder();

        parkingBoyVo.setParkParkingBoyName(me.getName());
        orderRepository.save(parkingOrder);
        return parkingBoyVo;
    }

    public ParkingBoyVo parkingBoyFetch(String orderId){
        ParkingOrder parkingOrder = orderRepository.findById(orderId).orElseThrow(() ->new BusinessException(RECODE_NOT_FOUNT));
        ParkingBoy me = parkingBoyService.getCurrentParkingBoy();

        ParkingLot parkingLot = parkingOrder.getParkingLot();
        me.setFree(true);
        // 调用系统指派订单
        newThreadToAssignOrder();

        parkingOrder.setOrderStatus(OrderStatus.PAID);
        parkingOrder.setFetchParkingBoy(me);
        parkingOrder.setFetchTime(new Date());

        parkingLot.setUsedCapacity(parkingLot.getUsedCapacity()-1);

        ParkingBoyVo parkingBoyVo = getParkingBoyVoByParkingOrder(parkingOrder);
        parkingBoyVo.setParkParkingBoyName(parkingOrder.getParkParkingBoy().getName());
        parkingBoyVo.setFetchParkingBoyName(me.getName());
        orderRepository.save(parkingOrder);
        return parkingBoyVo;
    }

    public ParkingOrder processingCondition(ParkingOrder parkingOrder, OrderStatus status){
        parkingOrder.setOrderStatus(status);
        parkingOrder = orderRepository.save(parkingOrder);
        ProgressingDataStore.queue.offer(parkingOrder.getId());
        return parkingOrder;
    }

    private ParkingBoyVo getParkingBoyVoByParkingOrder(ParkingOrder parkingOrder){
        User user = parkingOrder.getCustomer().getUser();
        Customer customer = parkingOrder.getCustomer();

        ParkingBoyVo parkingBoyVo = new ParkingBoyVo();
        ParkingLot parkingLot = parkingOrder.getParkingLot();

        parkingBoyVo.setParkingLotName(parkingLot.getName());
        parkingBoyVo.setOrderId(parkingOrder.getId());
        parkingBoyVo.setPrice(parkingOrder.getPrice());
        parkingBoyVo.setNumber(parkingOrder.getNumber());
        parkingBoyVo.setUsername(user.getName());
        parkingBoyVo.setPhone(customer.getPhone());
        parkingBoyVo.setCarNo(parkingOrder.getCarNo());
        parkingBoyVo.setSubmitTime(parkingOrder.getSubmitTime());
        parkingBoyVo.setOrderStatus(parkingOrder.getOrderStatus());
        parkingBoyVo.setOrderStatus(parkingOrder.getOrderStatus());
        parkingBoyVo.setFetchTime(parkingOrder.getEndTime());
        parkingBoyVo.setFetchParkingBoyName("");
        parkingBoyVo.setParkParkingBoyName("");

        return parkingBoyVo;
    }

    private void newThreadToAssignOrder(){
        if(!ProgressingDataStore.queue.isEmpty()){
            ParkingOrder newParkingOrder = orderRepository.findById(ProgressingDataStore.queue.poll()).orElseThrow(() -> new BusinessException(RECODE_NOT_FOUNT));
            Runnable runnable = () -> systemAssignOrder(newParkingOrder);
            new Thread(runnable).start();
        }
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
