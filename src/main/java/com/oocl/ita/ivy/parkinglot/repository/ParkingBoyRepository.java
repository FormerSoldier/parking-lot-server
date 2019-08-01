package com.oocl.ita.ivy.parkinglot.repository;

import com.oocl.ita.ivy.parkinglot.entity.ParkingBoy;
import com.oocl.ita.ivy.parkinglot.entity.enums.ParkingBoyStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ParkingBoyRepository extends JpaRepository<ParkingBoy, String> {

    @Override
    @Query(value = "SELECT * FROM parking_boy INNER JOIN user_master ON parking_boy.user_id = user_master.id WHERE delete_flag = 0", nativeQuery = true)
    Page<ParkingBoy> findAll(Pageable pageable);

    @Query(value = "SELECT parking_boy.*, new_table.id AS parkinglot_id FROM parking_boy INNER JOIN ( SELECT pl_id_table.*, parking_boy_id FROM parking_boy_parking_lot_list AS pbpll INNER JOIN ( SELECT * FROM parking_lot WHERE capacity > used_capacity) AS pl_id_table ON pbpll.parking_lot_list_id = pl_id_table.id ) AS new_table ON parking_boy.id = new_table.parking_boy_id WHERE STATUS = :status AND free = 1 LIMIT 0, 1", nativeQuery = true)
    ParkingBoy getParkingBoyInSomeStatus(@Param("status") String status);

    @Query(
            value = "SELECT p.* from parking_boy p, user_master u" +
                    "WHERE p.user_id = u.id" +
                    "AND username = :username",
            nativeQuery = true
    )
    Optional<ParkingBoy> findByUsername(@Param("username") String username);

    ParkingBoy findParkingBoyByUserId(Integer id);


    @Query(value = "SELECT * FROM (SELECT * FROM parking_boy_parking_lot_list AS pbpll" +
            "    WHERE parking_lot_list_id = :id" +
            ") AS parking_boy_table" +
            "    INNER JOIN parking_boy" +
            "    ON parking_boy_table.parking_boy_id = parking_boy.id" +
            "    WHERE status = :status AND free = 1", nativeQuery = true)
    List<ParkingBoy> getParkingBoyByParkingLot(@Param("id") String id, @Param("status") String status);

    List<ParkingBoy> findAllByStatus(ParkingBoyStatus status);

    @Query(value = "SELECT * FROM (SELECT * FROM parking_boy_parking_boys AS pbpbs" +
            "    WHERE pbpbs.parking_boys_id = :id" +
            ")   AS parking_boys_table" +
            "    INNER JOIN parking_boy" +
            "    ON parking_boys_table.parking_boy_id = parking_boy.id", nativeQuery = true)
    ParkingBoy findManagerBySubordinate(@Param("id") String id);

    ParkingBoy findByUserId(Integer userId);
}
