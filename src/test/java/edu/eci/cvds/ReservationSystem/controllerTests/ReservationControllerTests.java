package edu.eci.cvds.ReservationSystem.controllerTests;

import edu.eci.cvds.ReservationSystem.exception.ReservationNotFoundException;
import edu.eci.cvds.ReservationSystem.model.Laboratory;
import edu.eci.cvds.ReservationSystem.model.Reservation;
import edu.eci.cvds.ReservationSystem.controller.ReservationController;
import edu.eci.cvds.ReservationSystem.servicios.MakeReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)

public class ReservationControllerTests {

    private MockMvc mockMvc;

    @Mock
    private MakeReservationService reservationService;

    @InjectMocks
    private ReservationController reservationController;

    private Reservation reservation;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(reservationController).build();
        reservation = new Reservation("Lab A", LocalDate.now(), "10:00-12:00", "user123");
        reservation.setId("1");
    }

    @Test
    void shouldCreateReservationSuccessfully() {
        when(reservationService.makeReservation(any(Reservation.class))).thenReturn(reservation);

        ResponseEntity<Reservation> response = reservationController.createReservation(reservation);

        assertNotNull(response.getBody());
        assertEquals("Lab A", response.getBody().getLabName());
        assertEquals("1", response.getBody().getId());
    }

    @Test
    void shouldReturnAllReservations() {
        List<Reservation> reservations = Arrays.asList(reservation);
        when(reservationService.getAllReservations()).thenReturn(reservations);

        ResponseEntity<List<Reservation>> response = reservationController.getAllReservations();

        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Lab A", response.getBody().get(0).getLabName());
    }

    @Test
    void shouldReturnReservationById() {
        when(reservationService.getReservationById("1")).thenReturn(reservation);

        ResponseEntity<?> response = reservationController.getReservation("1");

        assertNotNull(response.getBody());
        assertEquals(Reservation.class, response.getBody().getClass());
        assertEquals("Lab A", ((Reservation) response.getBody()).getLabName());
    }

    @Test
    void shouldReturnNotFoundWhenReservationDoesNotExist() {
        when(reservationService.getReservationById("2")).thenThrow(new ReservationNotFoundException("Reserva no encontrada"));

        ResponseEntity<?> response = reservationController.getReservation("2");

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Reserva no encontrada", response.getBody());
    }

    @Test
    void shouldReturnAllReservationsWhenIdIsNull() {
        List<Reservation> reservations = Arrays.asList(reservation);
        when(reservationService.getAllReservations()).thenReturn(reservations);

        ResponseEntity<?> response = reservationController.getReservation(null);

        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof List);
        assertEquals(1, ((List<?>) response.getBody()).size());
    }

    @Test
    void testGetReservationById_NotFound() throws Exception {
        when(reservationService.getReservationById("1")).thenThrow(new ReservationNotFoundException("Reserva no encontrada"));

        mockMvc.perform(get("/api/reservations?id=1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Reserva no encontrada"));
    }

    @Test
    void testCancelReservation_Success() throws Exception {
        doNothing().when(reservationService).cancelReservation("1");

        mockMvc.perform(delete("/api/reservations?id=1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Reserva eliminada exitosamente"));
    }

    @Test
    void testCheckAvailability_True() throws Exception {
        when(reservationService.isReserved(any(Laboratory.class), any(LocalDate.class), anyString())).thenReturn(false);

        mockMvc.perform(get("/api/reservations/availability")
                        .param("labName", "Lab1")
                        .param("block", "BlockA")
                        .param("date", "2025-03-15")
                        .param("time", "10:00-12:00"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void testCheckAvailability_WhenReserved_ShouldReturnFalse() throws Exception {
        when(reservationService.isReserved(any(Laboratory.class), any(LocalDate.class), anyString()))
                .thenReturn(true); // está reservado, por lo que la disponibilidad debe ser "false"

        mockMvc.perform(get("/api/reservations/availability")
                        .param("labName", "Lab1")
                        .param("block", "BlockA")
                        .param("date", "2025-03-15")
                        .param("time", "10:00-12:00"))
                .andExpect(status().isOk()) // Verificamos que el código de estado sea 200 OK
                .andExpect(content().string("false")); // Verificamos que la respuesta sea "false" (no disponible)
    }
    

    @Test
    void testHandlerReservationNotFound() throws Exception {
        String reservationId = "non-existent-id";
        when(reservationService.getReservationById(reservationId)).thenThrow(new ReservationNotFoundException(ReservationNotFoundException.NOT_FOUND));

        mockMvc.perform(get("/api/reservations?id=" + reservationId))
                .andExpect(status().isNotFound())
                .andExpect(content().string(ReservationNotFoundException.NOT_FOUND));
    }
    @Test
    void testHandlerConflict() {
        RuntimeException exception = new RuntimeException("Error de conflicto");
        ResponseEntity<String> response = reservationController.handleConflict(exception);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Error de conflicto", response.getBody());
    }

    @Test
    public void testCancelReservationNotFound() throws Exception {
        doThrow(new ReservationNotFoundException("Reserva no encontrada")).when(reservationService).cancelReservation(anyString());

        mockMvc.perform(delete("/api/reservations")
                        .param("id", "123"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Reserva no encontrada"));
    }


    @Test
    void testHandleGeneralException() throws Exception {
        Exception genericException = new Exception("Otro error interno");

        ResponseEntity<String> response = reservationController.handleGeneralException(genericException);

        // Verificamos que la respuesta tiene el código de estado 500 (INTERNAL_SERVER_ERROR)
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error interno del servidor", response.getBody());
    }


    @Test
    void testHandleReservationNotFoundException() throws Exception {
        // Preparación: Creamos una excepción personalizada para lanzar desde el servicio
        String nonExistentId = "non-existent-id";
        String errorMessage = "Reserva no encontrada";

        // Simulamos el comportamiento del servicio para lanzar ReservationNotFoundException
        when(reservationService.getReservationById(nonExistentId)).thenThrow(new ReservationNotFoundException(errorMessage));

        // Realizamos la solicitud GET y pasamos un ID inexistente, lo cual provocará que el controlador maneje la excepción
        mockMvc.perform(get("/api/reservations?id=" + nonExistentId))
                .andExpect(status().isNotFound())  // Verificamos que el estado es 404 NOT FOUND
                .andExpect(content().string(errorMessage));  // Verificamos que el mensaje sea el error que definimos
    }

}
