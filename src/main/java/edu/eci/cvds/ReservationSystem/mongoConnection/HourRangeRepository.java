package edu.eci.cvds.ReservationSystem.mongoConnection;

import edu.eci.cvds.ReservationSystem.model.HoursRange;
import edu.eci.cvds.ReservationSystem.model.Laboratory;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Repositorio para la entidad HoursRange.
 * Permite realizar operaciones CRUD sobre la colección "hourranges".
 */
public interface HourRangeRepository extends MongoRepository<HoursRange, String> {

    /**
     * Verifica si existe un rango de horas específico.
     *
     * @param aviableHours Rango de horas a verificar.
     * @return true si existe, false en caso contrario.
     */
    boolean existsByAviableHours(String aviableHours);

    /**
     * Busca un rango de horas por su valor.
     *
     * @param aviableHours Rango de horas a buscar.
     * @return Optional conteniendo el HoursRange si se encuentra.
     */
    Optional<HoursRange> findByAviableHours(String aviableHours);
}
