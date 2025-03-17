package edu.eci.cvds.ReservationSystem.modelTests;

import edu.eci.cvds.ReservationSystem.model.HoursRange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HoursRangeTests {

    private HoursRange hoursRange;

    @BeforeEach
    public void setUp() {
        hoursRange = new HoursRange();
    }

    @Test
    public void testConstructor() {
        assertNull(hoursRange.getHoraApertura(), "La hora de apertura debería ser nula");
    }

    @Test
    public void testSetAndGetHoraApertura() {
        hoursRange.setHoraApertura("08:00 AM - 06:00 PM");

        assertEquals("08:00 AM - 06:00 PM", hoursRange.getHoraApertura(), "La hora de apertura debería ser '08:00 AM - 06:00 PM'");
    }

    @Test
    public void testSetHoraAperturaNull() {
        hoursRange.setHoraApertura(null);

        assertNull(hoursRange.getHoraApertura(), "La hora de apertura debería ser nula");
    }
}
