package edu.eci.cvds.ReservationSystem.modelTests;

import edu.eci.cvds.ReservationSystem.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTests {

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User("Juan Pérez", "juan.perez@example.com", "1234password");
    }

    @Test
    public void testGetPassword() {
        assertEquals("1234password", user.getPassword(), "El password debería ser '1234password'");
    }

    @Test
    public void testSetPassword() {
        user.setPassword("newpassword123");
        assertEquals("newpassword123", user.getPassword(), "El password debería ser 'newpassword123'");
    }

    @Test
    public void testSetPasswordNull() {
        user.setPassword(null);
        assertNull(user.getPassword(), "El password debería ser nulo");
    }
}
