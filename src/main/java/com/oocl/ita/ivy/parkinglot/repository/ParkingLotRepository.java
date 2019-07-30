package com.oocl.ita.ivy.parkinglot.repository;

import com.oocl.ita.ivy.parkinglot.entity.ParkingLot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ParkingLotRepository extends JpaRepository<ParkingLot,String> {

    @Query(value="SELECT pl_id_table.* FROM parking_boy_parking_lot_list AS pbpll INNER JOIN ( SELECT * FROM parking_lot WHERE capacity > used_capacity LIMIT 1, 1 ) AS pl_id_table ON pbpll.parking_lot_list_id = pl_id_table.id",nativeQuery = true)
    ParkingLot getValidParkingLot();
}
