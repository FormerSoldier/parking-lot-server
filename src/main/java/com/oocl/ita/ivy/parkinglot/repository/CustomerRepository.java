package com.oocl.ita.ivy.parkinglot.repository;

import com.oocl.ita.ivy.parkinglot.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, String> {

    /*@Query(value="SELECT customer.id, phone, point, is_vip, times, user.id, user.name, user." nativeQuery = true)
    Optional<Customer> findByUsername(String username);*/

    @Override
    @Query(value="SELECT * FROM customer INNER JOIN user_master ON user_master.id = :id AND user_master.id = customer.user_id",nativeQuery = true)
    Optional<Customer> findById(@Param("id") String id);
}
