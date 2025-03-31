package edu.eci.cvds.ReservationSystem.modelTests;

import edu.eci.cvds.ReservationSystem.model.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ReservationTests {

    private Reservation reservation;

    @BeforeEach
    void setUp() {
        reservation = new Reservation("Lab A", LocalDate.now(), "10:00-12:00", "user123");
    }

    @Test
    public void testSetId() {
        reservation.setId("12345");
        assertNotNull(reservation.getId(), "El id de la reservación no deberia ser nulo.");
    }

    @Test
    void shouldSetAndGetReserveDate() {
        LocalDate newDate = LocalDate.of(2025, 5, 20);
        reservation.setReserveDate(newDate);
        assertEquals(newDate, reservation.getReserveDate());
    }

    @Test
    void shouldSetAndGetReserveTime() {
        String newTime = "14:00-16:00";
        reservation.setReserveTime(newTime);
        assertEquals(newTime, reservation.getReserveTime());
    }

    @Test
    void shouldSetAndGetUserName() {
        String newUser = "user456";
        reservation.setUserName(newUser);
        assertEquals(newUser, reservation.getUserName());
    }
}
