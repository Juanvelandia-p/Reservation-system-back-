package edu.eci.cvds.ReservationSystem.controller.model;

import java.time.LocalDate;

import edu.eci.cvds.ReservationSystem.model.Laboratory;

public record ReservationDTO(Laboratory lab, LocalDate reserveDate,  int reserveTime, String userName ) {
}