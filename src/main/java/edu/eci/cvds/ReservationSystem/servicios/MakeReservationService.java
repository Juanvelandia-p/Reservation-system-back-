package edu.eci.cvds.ReservationSystem.servicios;


import edu.eci.cvds.ReservationSystem.exception.ReservationNotFoundException;
import edu.eci.cvds.ReservationSystem.model.*;
import edu.eci.cvds.ReservationSystem.mongoConnection.LaboratoryRepository;
import edu.eci.cvds.ReservationSystem.mongoConnection.ReservationRepository;
import edu.eci.cvds.ReservationSystem.mongoConnection.HourRangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Servicio para la gestión de reservas.
 * Métodos para crear, consultar y cancelar reservas, y para verificar la disponibilidad de un laboratorio.
 */
@Service
public class MakeReservationService {

    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private LaboratoryRepository laboratoryRepository;
    @Autowired
    private HourRangeRepository hoursRangeRepository; // Repositorio para el horario

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
        Laboratory lab = laboratoryRepository.findByName(reservation.getLab())
            .orElseThrow(() -> new ReservationNotFoundException(ReservationNotFoundException.LAB_NOT_FOUND));
       
        boolean time = hoursRangeRepository.existsByAviableHours(reservation.getReserveTime());
        if (!time) {
            throw new ReservationNotFoundException(ReservationNotFoundException.TIME_NOT_FOUND);
        }

        boolean ocupado = reservationRepository.existsByLabAndReserveDateAndReserveTime(reservation.getLab(), reservation.getReserveDate(), reservation.getReserveTime());
        
        if (ocupado) {
            throw new ReservationNotFoundException(ReservationNotFoundException.CONFLICT);
        }

        reservation.setLab(lab.getName());
        return reservationRepository.save(reservation);
    }

    /**
     * Obtiene la lista de todas las reservas.
     *
     * @return Lista de objetos Reservation.
     */
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    /**
     * Obtiene una reserva por su identificador.
     *
     * @param id Identificador de la reserva.
     * @return Reserva encontrada.
     * @throws ReservationNotFoundException Si la reserva no se encuentra.
     */
    public Reservation getReservationById(String id) {
        return reservationRepository.findById(id)
            .orElseThrow(() -> new ReservationNotFoundException(ReservationNotFoundException.NOT_FOUND));
    }

    /**
     * Cancela una reserva a partir de su identificador.
     *
     * @param id Identificador de la reserva a cancelar.
     * @throws ReservationNotFoundException Si la reserva no existe o ocurre un error al eliminarla.
     */
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

    /**
     * Verifica si existe una reserva para un laboratorio, en una fecha y hora específica.
     *
     * @param lab Nombre del laboratorio.
     * @param date Fecha de la reserva.
     * @param time Hora de la reserva.
     * @return true si la reserva existe, false en caso contrario.
     */
    public boolean isReserved(String lab, LocalDate date, String time) {
    return reservationRepository.existsByLabAndReserveDateAndReserveTime(lab, date, time);
    }
}
