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
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;

import static com.oocl.ita.ivy.parkinglot.entity.enums.Gender.FEMALE;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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

    @Test
    public void should_return_parking_boy_when_save_parking_boy() {
        ParkingBoy parkingBoy = generateParkingBoy();
        System.out.println(parkingBoy.getId());
        when(parkingBoyRepository.save(any())).thenReturn(parkingBoy);

        ParkingBoy savePkb = parkingBoyService.save(parkingBoy);

        assertEquals(parkingBoy.getId(), savePkb.getId());
        verify(parkingBoyRepository).save(any());
    }

    @Test
    public void should_return_all_parking_boy_when_fetch_all() {
        when(parkingBoyRepository.findAll()).thenReturn(new ArrayList<ParkingBoy>());
        parkingBoyService.findAllParkingBoys();
        verify(parkingBoyRepository).findAll();
    }

    @Test
    public void should_return_parking_boy_when_fetch_parking_boy_by_id() throws Exception {
        ParkingBoy parkingBoy = generateParkingBoy();
        when(parkingBoyRepository.findById(anyString())).thenReturn(java.util.Optional.of(new ParkingBoy()));

        parkingBoyService.find(parkingBoy.getId());
        verify(parkingBoyRepository).findById(anyString());
    }

    @Test
    public void should_return_parking_boy_when_update_parking_boy_by_id() {
        ParkingBoy parkingBoy = generateParkingBoy();
        when(parkingBoyRepository.save(any())).thenReturn(parkingBoy);
    }
}