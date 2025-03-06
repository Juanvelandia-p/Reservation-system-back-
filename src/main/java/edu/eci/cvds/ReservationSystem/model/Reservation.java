package edu.eci.cvds.ReservationSystem.model;

import java.time.LocalDate;

import org.springframework.data.annotation.*;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "reserves")
public class Reservation {

    @Id
    private Long id;
    private Laboratory lab;
    private LocalDate reserveDate;
    private int reserveTime;
    private String userName;

    public Reservation(Long id,String laboratory, LocalDate reserveDate, int reserveTime, String name){

    }

    public void setLab(Laboratory lab) {
        this.lab = lab;
    }

    public void setReserveTime(int reserveTime) {
        this.reserveTime = reserveTime;
    }

    public void setReserveDate(LocalDate reserveDate) {
        this.reserveDate = reserveDate;
    }
}
