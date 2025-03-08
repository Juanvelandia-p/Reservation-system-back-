package edu.eci.cvds.ReservationSystem.servicios;


import edu.eci.cvds.ReservationSystem.controller.model.ReservationDTO;
import edu.eci.cvds.ReservationSystem.model.*;
import edu.eci.cvds.ReservationSystem.mongoConnection.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MakeReservationService {

    @Autowired
    private ReservationRepository reservationRepository;


    /**
     * Método para realizar una nueva reserva
     * 
     * @param id El ID de la reserva
     * @param lab El laboratorio a reservar
     * @param reserveDate La fecha de la reservas
     * @param reserveTime El tiempo de la reserva (horario específico)
     * @param userName El nombre del usuario que realiza la reserva
     * @return La reserva creada
     */
    public Reservation makeReservation(Reservation reservation) {
        // Validar disponibilidad
        boolean exists = reservationRepository.existsByLabAndReserveDateAndReserveTime(
            reservation.getLab(), 
            reservation.getReserveDate(), 
            reservation.getReserveTime()
        );
        
        if (exists) {
            throw new RuntimeException("El laboratorio ya está reservado en esta fecha y hora");
        }
        
        return reservationRepository.save(reservation);
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Reservation getReservationById(String id) {
        return reservationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
    }

    public void cancelReservation(String id) {
        reservationRepository.deleteById(id);
    }

    public boolean isReserved(Laboratory lab, LocalDate date, int time) {
    return reservationRepository.existsByLabAndReserveDateAndReserveTime(lab, date, time);
}
}
