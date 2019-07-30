package com.oocl.ita.ivy.parkinglot.repository;

import com.oocl.ita.ivy.parkinglot.entity.ParkingBoy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;



@Repository
public interface ParkingBoyRepository extends JpaRepository<ParkingBoy, String> {

    @Override
    @Query(value = "SELECT * FROM parking_boy INNER JOIN user_master ON parking_boy.user_id = user_master.id WHERE delete_flag = 0",nativeQuery = true)
    Page<ParkingBoy> findAll(Pageable pageable);

}
