package com.oocl.ita.ivy.parkinglot.repository;


import com.oocl.ita.ivy.parkinglot.entity.ParkingBoyDTO;
import com.oocl.ita.ivy.parkinglot.entity.ParkingOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingOrderRepository extends JpaRepository<ParkingOrder, String> {

    /*@Query(value="SELECT parking_boy.*, new_table.id AS parking_lot_id, new_table.* FROM parking_boy INNER JOIN ( SELECT pl_id_table.*, parking_boy_id FROM parking_boy_parking_lot_list AS pbpll INNER JOIN ( SELECT * FROM parking_lot WHERE capacity > used_capacity LIMIT 1, 1 ) AS pl_id_table ON pbpll.parking_lot_list_id = pl_id_table.id ) AS new_table ON parking_boy.id = new_table.parking_boy_id",nativeQuery = true)
    ParkingBoyDTO getParkingBoyAndParkingBoy();*/
}
