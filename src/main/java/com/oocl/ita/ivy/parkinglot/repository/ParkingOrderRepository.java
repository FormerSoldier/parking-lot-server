package com.oocl.ita.ivy.parkinglot.repository;


import com.oocl.ita.ivy.parkinglot.entity.ParkingOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParkingOrderRepository extends JpaRepository<ParkingOrder, String> {

    @Query(value = "SELECT o.* from parking_order o, customer c WHERE o.customer_id = c.id AND c.id = :customerId",
            nativeQuery = true)
    List<ParkingOrder> findByCustomerId(@Param("customerId") String customerId);

}
