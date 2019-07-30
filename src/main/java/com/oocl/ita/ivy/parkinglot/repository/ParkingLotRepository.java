package com.oocl.ita.ivy.parkinglot.repository;

import com.oocl.ita.ivy.parkinglot.entity.ParkingLot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface ParkingLotRepository extends JpaRepository<ParkingLot,String> {

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE parking_lot SET used_capacity = used_capacity + 1 WHERE id = :id",nativeQuery = true)
    void addUsedCapacity(@Param("id") String id);
}
