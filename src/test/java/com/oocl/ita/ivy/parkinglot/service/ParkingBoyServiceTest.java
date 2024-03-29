package com.oocl.ita.ivy.parkinglot.service;

import com.oocl.ita.ivy.parkinglot.entity.ParkingBoy;
import com.oocl.ita.ivy.parkinglot.repository.ParkingBoyRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

import static com.oocl.ita.ivy.parkinglot.entity.enums.Gender.FEMALE;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

//@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ParkingBoyServiceTest {

    @TestConfiguration
    static class ParkingBoyServiceTestContextConfiguration{
        @Bean
        public ParkingBoyService parkingBoyService() {
            return new ParkingBoyService();
        }
    }

    @Mock
    private ParkingBoyRepository parkingBoyRepository;

    @InjectMocks
    private ParkingBoyService parkingBoyService;

    @Mock
    private UserService userService;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    private ParkingBoy generateParkingBoy() {
        ParkingBoy parkingBoy = new ParkingBoy();
        parkingBoy.setId("1");
        parkingBoy.setGender(FEMALE);
        parkingBoy.setName("123");
        return parkingBoy;
    }

    @Test
    public void should_return_parking_boy_when_save_parking_boy() {
        ParkingBoy parkingBoy =  new ParkingBoy();
        parkingBoy.setId("1");
        parkingBoy.setGender(FEMALE);
        parkingBoy.setName("123");
        when(parkingBoyRepository.save(any())).thenReturn(parkingBoy);

        ParkingBoy savePkb = parkingBoyService.save(parkingBoy);

        assertEquals(parkingBoy.getId(), savePkb.getId());
        verify(parkingBoyRepository).save(any());
    }

    @Test
    public void should_return_all_parking_boy_when_fetch_all() {
        when(parkingBoyRepository.findAll()).thenReturn(new ArrayList<ParkingBoy>());
        parkingBoyService.findAll();
        verify(parkingBoyRepository).findAll();
    }

    @Test
    public void should_return_parking_boy_when_fetch_parking_boy_by_id() throws Exception {
        ParkingBoy parkingBoy = generateParkingBoy();
        when(parkingBoyRepository.findById(anyString())).thenReturn(java.util.Optional.of(new ParkingBoy()));

        parkingBoyService.findById(parkingBoy.getId());
        verify(parkingBoyRepository).findById(anyString());
    }

    @Test
    public void should_return_parking_boy_when_update_parking_boy_by_id() {
        ParkingBoy parkingBoy = generateParkingBoy();
        when(parkingBoyRepository.save(any())).thenReturn(parkingBoy);
    }

}