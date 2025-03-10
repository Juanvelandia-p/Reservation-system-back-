package edu.eci.cvds.ReservationSystem.servicios;

import edu.eci.cvds.ReservationSystem.exception.ReservationNotFoundException;
import edu.eci.cvds.ReservationSystem.model.Laboratory;
import edu.eci.cvds.ReservationSystem.mongoConnection.LaboratoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LaboratoryService {

    @Autowired
    private LaboratoryRepository laboratoryRepository;

    public Laboratory addLaboratory(Laboratory laboratory) {
        // Validar que no exista un laboratorio con el mismo nombre y bloque
        if (laboratoryRepository.existsByNameAndBlock(laboratory.getName(), laboratory.getBlock())) {
            throw new ReservationNotFoundException(ReservationNotFoundException.DUPLICATE_LAB);
        }
        return laboratoryRepository.save(laboratory);
    }

    public List<Laboratory> getAllLaboratories() {
        return laboratoryRepository.findAll();
    }
}