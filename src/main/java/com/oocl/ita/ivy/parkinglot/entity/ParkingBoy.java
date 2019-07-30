package com.oocl.ita.ivy.parkinglot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oocl.ita.ivy.parkinglot.entity.enums.Gender;
import com.oocl.ita.ivy.parkinglot.entity.enums.ParkingBoyStatus;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class ParkingBoy {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @ManyToMany
    @JoinColumn(name="parking_lot_id")
    private List<ParkingLot> parkingLotList;

    @OneToOne
    @NotNull
    @JoinColumn(name="user_id")
    private User user;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ParkingBoyStatus status = ParkingBoyStatus.STOP;

    @NotNull
    private String name;

    @NotNull
    private Gender gender;

    private int orderNumInOpen;

    private int orderNumInClose;

    private boolean free;


    @Column
    @CreationTimestamp
    @NotNull
    private Date joinTime = new Date();

    @Column
    @CreationTimestamp
    private Date createTime;

    @Column
    @UpdateTimestamp
    private Date updateTime;


    @JsonIgnore
    public boolean hasFreeParkingLot(){
        if(parkingLotList == null || parkingLotList.size() == 0){
            return false;
        }
        return parkingLotList.stream().anyMatch(parkingLot -> parkingLot.getCapacity() > parkingLot.getUsedCapacity());
    }
}
