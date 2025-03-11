package edu.eci.cvds.ReservationSystem.exception;

public class ReservationNotFoundException extends RuntimeException {
    public static final String NOT_FOUND = "Reserva inexistente";
    public static final String DELETE_ERROR = "Error eliminando la reserva";
    public static final String DUPLICATE_LAB = "Ya existe un laboratorio con este nombre y bloque";
    public static final String CONFLICT = "El laboratorio ya está reservado en esta fecha y hora";
    public static final String LAB_NOT_FOUND = "Laboratorio no registrado en el sistema";

    public ReservationNotFoundException(String message) {
        super(message);
    }
}
