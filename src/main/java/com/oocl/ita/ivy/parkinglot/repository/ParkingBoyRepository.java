package com.oocl.ita.ivy.parkinglot.repository;

import com.oocl.ita.ivy.parkinglot.entity.ParkingBoy;
import com.oocl.ita.ivy.parkinglot.entity.ParkingOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



@Repository
public interface ParkingBoyRepository extends JpaRepository<ParkingBoy, String> {

    @Override
    @Query(value = "SELECT * FROM parking_boy INNER JOIN user_master ON parking_boy.user_id = user_master.id WHERE delete_flag = 0",nativeQuery = true)
    Page<ParkingBoy> findAll(Pageable pageable);
    @Query(value="SELECT parking_boy.id AS id, new_table.id AS parking_lot_id FROM parking_boy INNER JOIN ( SELECT pl_id_table.*, parking_boy_id FROM parking_boy_parking_lot_list AS pbpll INNER JOIN ( SELECT * FROM parking_lot WHERE capacity > used_capacity AND STATUS = : STATUS LIMIT 1, 1 ) AS pl_id_table ON pbpll.parking_lot_list_id = pl_id_table.id ) AS new_table ON parking_boy.id = new_table.parking_boy_id",nativeQuery = true)
    ParkingBoy getParkingBoyInSomeStatus(@Param("status") String status);
}
