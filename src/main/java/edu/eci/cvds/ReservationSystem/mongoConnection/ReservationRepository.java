package edu.eci.cvds.ReservationSystem.mongoConnection;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import edu.eci.cvds.ReservationSystem.model.Laboratory;
import edu.eci.cvds.ReservationSystem.model.Reservation;
import edu.eci.cvds.ReservationSystem.model.User;

/**
 * Repositorio para la entidad Reservation.
 * Permite realizar operaciones CRUD sobre la colección "reservations".
 */
@Repository
public interface ReservationRepository extends MongoRepository<Reservation, String> {

    /**
     * Verifica si existe una reserva para un laboratorio, fecha y horario específico.
     *
     * @param lab Nombre del laboratorio.
     * @param reserveDate Fecha de la reserva.
     * @param reserveTime Hora de la reserva.
     * @return true si existe la reserva, false en caso contrario.
     */
    boolean existsByLabAndReserveDateAndReserveTime(
        String lab,
        LocalDate reserveDate, 
        String reserveTime
    );
}


