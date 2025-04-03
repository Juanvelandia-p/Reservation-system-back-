package edu.eci.cvds.ReservationSystem.controller;

import edu.eci.cvds.ReservationSystem.exception.ReservationNotFoundException;
import edu.eci.cvds.ReservationSystem.model.Laboratory;
import edu.eci.cvds.ReservationSystem.servicios.LaboratoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de laboratorios.
 * Define endpoints para crear y consultar laboratorios.
 */
@RestController
@RequestMapping("/api/laboratories")
public class LaboratoryController {

    @Autowired
    private LaboratoryService laboratoryService;

    /**
     * Crea un nuevo laboratorio.
     *
     * @param laboratory Objeto Laboratory recibido en el cuerpo de la solicitud.
     * @return ResponseEntity con el laboratorio creado y estado HTTP 201 (CREATED) o
     *         un error en caso de conflicto (estado 409).
     */
    @PostMapping
    public ResponseEntity<?> createLaboratory(@RequestBody Laboratory laboratory) {
        try {
            Laboratory createdLab = laboratoryService.addLaboratory(laboratory);
            return new ResponseEntity<>(createdLab, HttpStatus.CREATED);
        } catch (ReservationNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        }
    }

     /**
     * Obtiene la lista de todos los laboratorios.
     *
     * @return ResponseEntity con la lista de laboratorios y estado HTTP 200.
     */
    @GetMapping
    public ResponseEntity<List<Laboratory>> getAllLaboratories() {
        return ResponseEntity.ok(laboratoryService.getAllLaboratories());
    }
}