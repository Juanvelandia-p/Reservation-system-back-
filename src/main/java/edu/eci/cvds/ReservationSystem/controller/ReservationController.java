package edu.eci.cvds.ReservationSystem.controller;

import edu.eci.cvds.ReservationSystem.servicios.*;
import edu.eci.cvds.ReservationSystem.controller.model.ReservationDTO;
import edu.eci.cvds.ReservationSystem.exception.ReservationNotFoundException;
import edu.eci.cvds.ReservationSystem.model.Laboratory;
import edu.eci.cvds.ReservationSystem.model.Reservation;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private MakeReservationService reservationService;

    @PostMapping
    public ResponseEntity<Reservation> createReservation(@RequestBody Reservation Reservas) {
        return ResponseEntity.ok(reservationService.makeReservation(Reservas));
    }

    // Para obtener todas las reservas
    @GetMapping("/all")
    public ResponseEntity<List<Reservation>> getAllReservations() {
        return ResponseEntity.ok(reservationService.getAllReservations());
    }

    @GetMapping
    public ResponseEntity<?> getReservation(@RequestParam(required = false) String id) {
        if (id != null) {
            try {
                return ResponseEntity.ok(reservationService.getReservationById(id));
            } catch (ReservationNotFoundException ex) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
            }
        } else {
            return ResponseEntity.ok(reservationService.getAllReservations());
        }
    }

    @DeleteMapping
    public ResponseEntity<String> cancelReservation(@RequestParam String id) {
        try {
            reservationService.cancelReservation(id);
            return ResponseEntity.ok("Reserva eliminada exitosamente");
        } catch (ReservationNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleConflict(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @GetMapping("/availability")
    public ResponseEntity<Boolean> checkAvailability(
        @RequestParam String labName,
        @RequestParam String block,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
        @RequestParam int time) {
        
        Laboratory lab = new Laboratory(labName, block);
        boolean available = !reservationService.isReserved(lab, date, time);
        return ResponseEntity.ok(available);
    }

    @ExceptionHandler(ReservationNotFoundException.class)
    public ResponseEntity<String> handleReservationNotFound(ReservationNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
    }
}
