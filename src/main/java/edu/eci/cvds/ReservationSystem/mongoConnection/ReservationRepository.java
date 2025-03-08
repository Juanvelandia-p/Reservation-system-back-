package edu.eci.cvds.ReservationSystem.mongoConnection;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import edu.eci.cvds.ReservationSystem.model.Laboratory;
import edu.eci.cvds.ReservationSystem.model.Reservation;
import edu.eci.cvds.ReservationSystem.model.User;

@Repository
public interface ReservationRepository extends MongoRepository<Reservation, String> {
    boolean existsByLabAndReserveDateAndReserveTime(
        Laboratory lab, 
        LocalDate reserveDate, 
        int reserveTime
    );
}


