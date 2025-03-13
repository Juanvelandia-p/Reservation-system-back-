package edu.eci.cvds.ReservationSystem.model;

import java.time.LocalDate;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

@Document(collection = "reservations")
public class Reservation {
    @Id
    private String id;
    private LocalDate reserveDate;
    private String reserveTime;
    private String userName;
    @DBRef // Referencia al documento de Laboratory
    private Laboratory lab;

    // Constructor sin ID (MongoDB lo genera)
    public Reservation(Laboratory lab, LocalDate reserveDate, String reserveTime, String userName) {
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
    public String getReserveTime() {
        return reserveTime;
    }
    public void setReserveTime(String reserveTime) {
        this.reserveTime = reserveTime;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

}
