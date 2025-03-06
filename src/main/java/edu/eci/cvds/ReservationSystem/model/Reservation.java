package edu.eci.cvds.ReservationSystem.model;

import java.time.LocalDate;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Entity;

@Entity
public class Reservation {

    @Id
    private Long id;
    public Reservation(Long id, Laboratory lab, LocalDate reserveDate, int reserveTime, String userName) {
        this.id = id;
        this.lab = lab;
        this.reserveDate = reserveDate;
        this.reserveTime = reserveTime;
        this.userName = userName;
    }
    private Laboratory lab;
    private LocalDate reserveDate;
    private int reserveTime;
    private String userName;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Laboratory getLab() {
        return lab;
    }
    public void setLab(Laboratory lab) {
        this.lab = lab;
    }
    public LocalDate getReserveDate() {
        return reserveDate;
    }
    public void setReserveDate(LocalDate reserveDate) {
        this.reserveDate = reserveDate;
    }
    public int getReserveTime() {
        return reserveTime;
    }
    public void setReserveTime(int reserveTime) {
        this.reserveTime = reserveTime;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

}
