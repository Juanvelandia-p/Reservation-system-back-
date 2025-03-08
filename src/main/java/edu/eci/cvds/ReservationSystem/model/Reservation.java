package edu.eci.cvds.ReservationSystem.model;

import java.time.LocalDate;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

import javax.persistence.Entity;

@Document(collection = "reservations")
public class Reservation {
    @Id
    private String id;
    private Laboratory lab;
    private LocalDate reserveDate;
    private int reserveTime;
    private String userName;

    // Constructor sin ID (MongoDB lo genera)
    public Reservation(Laboratory lab, LocalDate reserveDate, int reserveTime, String userName) {
        this.lab = lab;
        this.reserveDate = reserveDate;
        this.reserveTime = reserveTime;
        this.userName = userName;
    }


    public String getId() {
        return id;
    }
    public Reservation() {}
    public void setId(String id) {
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
