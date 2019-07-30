package com.oocl.ita.ivy.parkinglot.repository;


import com.oocl.ita.ivy.parkinglot.entity.ParkingOrder;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ParkingOrderRepository extends JpaRepository<ParkingOrder, String> {

}
