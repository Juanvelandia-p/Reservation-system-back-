package edu.eci.cvds.ReservationSystem.mongoConnection;

import edu.eci.cvds.ReservationSystem.model.Laboratory;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface LaboratoryRepository extends MongoRepository<Laboratory, String> {
    boolean existsByNameAndBlock(String name, String block);
    Optional<Laboratory> findByNameAndBlock(String name, String block);
}