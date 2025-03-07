package edu.eci.cvds.ReservationSystem.servicios;


import edu.eci.cvds.ReservationSystem.controller.model.ReservationDTO;
import edu.eci.cvds.ReservationSystem.model.*;
import edu.eci.cvds.ReservationSystem.model.Reservation;
import edu.eci.cvds.ReservationSystem.mongoConnection.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

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
        // Crear una nueva reserva
        // Guardar la reserva en la base de datos
        return reservationRepository.save(reservation);
    }
}
