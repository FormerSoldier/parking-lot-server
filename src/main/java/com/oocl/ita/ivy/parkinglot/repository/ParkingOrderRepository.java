package com.oocl.ita.ivy.parkinglot.repository;


import com.oocl.ita.ivy.parkinglot.entity.ParkingBoyDTO;
import com.oocl.ita.ivy.parkinglot.entity.ParkingOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface ParkingOrderRepository extends JpaRepository<ParkingOrder, String> {


    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value="UPDATE parking_order SET parkingboy_id = :parkingBoyId, parkinglot_id = :parkingLotId WHERE id = :id",nativeQuery = true)
    ParkingOrder updateOrderParkingLotIdAndParkingBoyId(@Param("parkingBoyId") String parkingBoyId,
                                                @Param("parkingLotId") String parkingLotId,
                                                @Param("id") String id);

}
