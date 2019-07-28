package com.oocl.ita.ivy.parkinglot.service;

import com.oocl.ita.ivy.parkinglot.entity.ParkingBoy;
import com.oocl.ita.ivy.parkinglot.repository.ParkingBoyRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import static com.oocl.ita.ivy.parkinglot.entity.enums.Gender.FEMALE;
import static org.junit.Assert.*;
import static reactor.core.publisher.Mono.when;

@RunWith(SpringRunner.class)
public class ParkingBoyServiceTest {

    @TestConfiguration
    static class ParkingBoyServiceTestContextConfiguration{
        @Bean
        public ParkingBoyService parkingBoyService() {
            return new ParkingBoyService();
        }
    }

    @MockBean
    private ParkingBoyRepository parkingBoyRepository;

    @Autowired
    private ParkingBoyService parkingBoyService;

    private ParkingBoy generateParkingBoy() {
        ParkingBoy parkingBoy = new ParkingBoy();
        parkingBoy.setId("1");
        parkingBoy.setGender(FEMALE);
        parkingBoy.setName("123");
        return parkingBoy;
    }


}