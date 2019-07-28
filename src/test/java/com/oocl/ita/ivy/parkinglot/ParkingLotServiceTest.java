package com.oocl.ita.ivy.parkinglot;

import com.oocl.ita.ivy.parkinglot.entity.ParkingLot;
import com.oocl.ita.ivy.parkinglot.repository.ParkingLotRepository;
import com.oocl.ita.ivy.parkinglot.service.ParkingLotService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class ParkingLotServiceTest {
    @TestConfiguration
    static class ParkingLotServiceContextConfiguration{
        @Bean
        public ParkingLotService parkingLotService(){
            return new ParkingLotService();
        }
    }
    @Autowired
    private ParkingLotService parkingLotService;

    @MockBean
    private ParkingLotRepository parkingLotRepository;

    private ParkingLot generateParkingLot(){
        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setName("6号停车场");
        parkingLot.setCapacity(10);
        parkingLot.setAddress("东岸村");
        return parkingLot;
    }

    @Test
    public void should_return_parkinglot_when_call_add_parkinglot(){
        ParkingLot parkingLot = generateParkingLot();
        when(parkingLotRepository.save(any())).thenReturn(parkingLot);
         ParkingLot parkingLot1 = parkingLotService.addParkingLot(parkingLot);

         assertSame(parkingLot.getId(),parkingLot1.getId());
         verify(parkingLotRepository).save(any());
    }
    @Test
    public void should_return_all_parkinglots_when_call_get_all_parkinglot(){
        when(parkingLotRepository.findAll()).thenReturn(new ArrayList<ParkingLot>());
        parkingLotService.getAllParkingLot();
        verify(parkingLotRepository).findAll();
    }
    @Test
    public void should_return_changed_parkinglot_when_call_modify_parkinglot(){
        ParkingLot parkingLot = generateParkingLot();
        parkingLot.setName("1号停车场");
        when(parkingLotRepository.save(any())).thenReturn(parkingLot);
        ParkingLot parkingLot1 = parkingLotService.modifyParkingLot(parkingLot);
        assertSame(parkingLot.getId(),parkingLot1.getId());
        verify(parkingLotRepository).save(any());
    }


}
