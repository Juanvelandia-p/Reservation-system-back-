package edu.eci.cvds.ReservationSystem.controller;

import edu.eci.cvds.ReservationSystem.servicios.*;
import edu.eci.cvds.ReservationSystem.model.Laboratory;
import edu.eci.cvds.ReservationSystem.model.Reservation;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private MakeReservationService makeReservationService;

    // Endpoint para crear una nueva reserva
    @PostMapping
    public Reservation createReservation(@RequestParam Long id,
                                         @RequestParam Laboratory lab,
                                         @RequestParam LocalDate reserveDate,
                                         @RequestParam int reserveTime,
                                         @RequestParam String userName) {
        return makeReservationService.makeReservation(id, lab, reserveDate, reserveTime, userName);
    }

    // // Endpoint para obtener una reserva por su id
    // @GetMapping("/{id}")
    // public Reservation getReservation(@PathVariable Long id) {
    //     return makeReservationService.getReservationById(id);
    // }
}
