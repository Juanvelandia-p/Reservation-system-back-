package edu.eci.cvds.ReservationSystem.servicios;


import edu.eci.cvds.ReservationSystem.controller.model.ReservationDTO;
import edu.eci.cvds.ReservationSystem.exception.ReservationNotFoundException;
import edu.eci.cvds.ReservationSystem.model.*;
import edu.eci.cvds.ReservationSystem.mongoConnection.LaboratoryRepository;
import edu.eci.cvds.ReservationSystem.mongoConnection.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MakeReservationService {

    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private LaboratoryRepository laboratoryRepository;
    /**
     * Método para realizar una nueva reserva
     * 
     * @param lab El laboratorio a reservar
     * @param reserveDate La fecha de la reservas
     * @param reserveTime El tiempo de la reserva (horario específico)
     * @param userName El nombre del usuario que realiza la reserva
     * @return La reserva creada
     */
    public Reservation makeReservation(Reservation reservation) {
        Laboratory lab = laboratoryRepository.findByNameAndBlock(reservation.getLab().getName(), reservation.getLab().getBlock())
            .orElseThrow(() -> new ReservationNotFoundException(ReservationNotFoundException.LAB_NOT_FOUND));
    
        // Verificar disponibilidad
        boolean ocupado = reservationRepository.existsByLabAndReserveDateAndReserveTime(lab, reservation.getReserveDate(), reservation.getReserveTime());
        
        if (ocupado) {
            throw new ReservationNotFoundException(ReservationNotFoundException.CONFLICT);
        }
        
        // Crear reserva
        reservation.setLab(lab);
        return reservationRepository.save(reservation);
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Reservation getReservationById(String id) {
        return reservationRepository.findById(id)
            .orElseThrow(() -> new ReservationNotFoundException(ReservationNotFoundException.NOT_FOUND));
    }

    public void cancelReservation(String id) {
        if (!reservationRepository.existsById(id)) {
            throw new ReservationNotFoundException(ReservationNotFoundException.NOT_FOUND);
        }
        try {
            reservationRepository.deleteById(id);
        } catch (Exception e) {
            throw new ReservationNotFoundException(ReservationNotFoundException.DELETE_ERROR);
        }
    }



    public boolean isReserved(Laboratory lab, LocalDate date, int time) {
    return reservationRepository.existsByLabAndReserveDateAndReserveTime(lab, date, time);
}
}
