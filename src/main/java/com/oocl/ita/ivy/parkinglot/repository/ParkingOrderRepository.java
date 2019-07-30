package com.oocl.ita.ivy.parkinglot.repository;


import com.oocl.ita.ivy.parkinglot.entity.ParkingBoyDTO;
import com.oocl.ita.ivy.parkinglot.entity.ParkingOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParkingOrderRepository extends JpaRepository<ParkingOrder, String> {

    @Query(value = "SELECT o.* from parking_order o, customer c WHERE o.customer_id = c.id",
            nativeQuery = true)
    List<ParkingOrder> findByCustomerId(String customerId);

}
