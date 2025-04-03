package edu.eci.cvds.ReservationSystem.mongoConnection;

import edu.eci.cvds.ReservationSystem.model.Laboratory;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Repositorio para la entidad Laboratory.
 * Permite realizar operaciones CRUD sobre la colección "laboratories".
 */
public interface LaboratoryRepository extends MongoRepository<Laboratory, String> {

    /**
     * Verifica si existe un laboratorio con el mismo nombre y bloque.
     *
     * @param name Nombre del laboratorio.
     * @param block Bloque del laboratorio.
     * @return true si existe, false en caso contrario.
     */
    boolean existsByNameAndBlock(String name, String block);

    /**
     * Busca un laboratorio por su nombre.
     *
     * @param name Nombre del laboratorio a buscar.
     * @return Optional conteniendo el Laboratory si se encuentra.
     */
    Optional<Laboratory> findByName(String name);
}