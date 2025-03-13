package edu.eci.cvds.ReservationSystem.mongoConnection;

import edu.eci.cvds.ReservationSystem.model.HoursRange;
import edu.eci.cvds.ReservationSystem.model.Laboratory;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface HourRangeRepository extends MongoRepository<HoursRange, String> {
    boolean existsByAviableHours(String aviableHours);
    Optional<HoursRange> findByAviableHours(String aviableHours);
}
