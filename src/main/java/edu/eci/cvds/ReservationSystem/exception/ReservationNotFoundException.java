package edu.eci.cvds.ReservationSystem.exception;

public class ReservationNotFoundException extends RuntimeException {
    public static final String NOT_FOUND = "Reserva inexistente";
    public static final String DELETE_ERROR = "Error eliminando la reserva";

    public ReservationNotFoundException(String message) {
        super(message);
    }
}
