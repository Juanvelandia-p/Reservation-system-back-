package edu.eci.cvds.ReservationSystem.controller;

import edu.eci.cvds.ReservationSystem.servicios.*;
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

/**
 * Controlador REST para la gestión de reservas.
 * Permite crear, consultar, cancelar reservas y verificar la disponibilidad de un laboratorio.
 */
@RestController
@RequestMapping("/api/reservations")
@CrossOrigin(origins = "*") 
public class ReservationController {

    @Autowired
    private MakeReservationService reservationService;

    /**
     * Crea una nueva reserva.
     *
     * @param reservation Objeto Reservation recibido en el cuerpo de la solicitud.
     * @return ResponseEntity con la reserva creada y estado HTTP 200.
     */
    @PostMapping
    public ResponseEntity<Reservation> createReservation(@RequestBody Reservation reservation) {
        return ResponseEntity.ok(reservationService.makeReservation(reservation));
    }

    /**
     * Obtiene la lista de todas las reservas.
     *
     * @return ResponseEntity con la lista de reservas y estado HTTP 200.
     */
    @GetMapping("/all")
    public ResponseEntity<List<Reservation>> getAllReservations() {
        return ResponseEntity.ok(reservationService.getAllReservations());
    }

    /**
     * Obtiene una reserva en particular por su identificador.
     *
     * @param id Identificador de la reserva (opcional).
     * @return ResponseEntity con la reserva consultada o la lista completa si no se especifica id.
     */
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

     /**
     * Cancela una reserva a partir de su identificador.
     *
     * @param id Identificador de la reserva a cancelar.
     * @return ResponseEntity con mensaje de éxito o error si no se encuentra la reserva.
     */
    @DeleteMapping
    public ResponseEntity<String> cancelReservation(@RequestParam String id) {
        try {
            reservationService.cancelReservation(id);
            return ResponseEntity.ok("Reserva eliminada exitosamente");
        } catch (ReservationNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    /**
     * Manejador de excepciones para conflictos en tiempo de ejecución.
     *
     * @param ex Excepción capturada.
     * @return ResponseEntity con el mensaje de error y estado HTTP 409.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleConflict(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    /**
     * Verifica la disponibilidad de un laboratorio en una fecha y horario específico.
     *
     * @param labName Nombre del laboratorio.
     * @param block Bloque en el que se encuentra el laboratorio.
     * @param date Fecha de la reserva.
     * @param time Horario específico a consultar.
     * @return ResponseEntity con un valor booleano que indica la disponibilidad (true si está disponible).
     */
    @GetMapping("/availability")
    public ResponseEntity<Boolean> checkAvailability(
        @RequestParam String labName,
        @RequestParam String block,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
        @RequestParam String time) {
        
        Laboratory lab = new Laboratory(labName, block);
        boolean available = !reservationService.isReserved(labName, date, time);
        return ResponseEntity.ok(available);
    }


    /**
     * Manejador general de excepciones.
     *
     * @param ex Excepción capturada.
     * @return ResponseEntity con mensaje de error y estado HTTP 500.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
    }

}
