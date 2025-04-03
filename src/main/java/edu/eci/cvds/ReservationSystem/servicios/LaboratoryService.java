package edu.eci.cvds.ReservationSystem.servicios;

import edu.eci.cvds.ReservationSystem.exception.ReservationNotFoundException;
import edu.eci.cvds.ReservationSystem.model.Laboratory;
import edu.eci.cvds.ReservationSystem.mongoConnection.LaboratoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio para la gestión de laboratorios.
 * Proporciona métodos para agregar y consultar laboratorios.
 */
@Service
public class LaboratoryService {

    @Autowired
    private LaboratoryRepository laboratoryRepository;

    /**
     * Agrega un nuevo laboratorio.
     * Si ya existe un laboratorio con el mismo nombre y bloque, lanza una excepción.
     *
     * @param laboratory Objeto Laboratory a agregar.
     * @return El laboratorio guardado.
     * @throws ReservationNotFoundException Si el laboratorio ya existe.
     */
    public Laboratory addLaboratory(Laboratory laboratory) {
        if (laboratoryRepository.existsByNameAndBlock(laboratory.getName(), laboratory.getBlock())) {
            throw new ReservationNotFoundException(ReservationNotFoundException.DUPLICATE_LAB);
        }
        return laboratoryRepository.save(laboratory);
    }

     /**
     * Obtiene la lista de todos los laboratorios.
     *
     * @return Lista de objetos Laboratory.
     */
    public List<Laboratory> getAllLaboratories() {
        return laboratoryRepository.findAll();
    }
}