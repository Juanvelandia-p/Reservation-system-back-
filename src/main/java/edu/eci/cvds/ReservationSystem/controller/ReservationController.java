package edu.eci.cvds.ReservationSystem.controller;

import edu.eci.cvds.ReservationSystem.servicios.*;
import edu.eci.cvds.ReservationSystem.controller.model.ReservationDTO;
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
    public Reservation createReservation(@RequestBody ReservationDTO reservation) {
        return makeReservationService.makeReservation(reservation.lab(), reservation.reserveDate(), reservation.reserveTime(), reservation.userName());
    }

    // // Endpoint para obtener una reserva por su id
    // @GetMapping("/{id}")
    // public Reservation getReservation(@PathVariable Long id) {
    //     return makeReservationService.getReservationById(id);
    // }
}
