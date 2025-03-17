package edu.eci.cvds.ReservationSystem.modelTests;

import edu.eci.cvds.ReservationSystem.model.HoursRange;
import edu.eci.cvds.ReservationSystem.model.Laboratory;
import edu.eci.cvds.ReservationSystem.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LaboratoryTests {

    private Laboratory lab;

    @BeforeEach
    public void setUp() {
        lab = new Laboratory();
    }

    @Test
    public void testSetId() {
        lab.setId("12345");
        assertNotNull(lab.getId(), "El id del lab no deberia ser nulo.");
    }

}
