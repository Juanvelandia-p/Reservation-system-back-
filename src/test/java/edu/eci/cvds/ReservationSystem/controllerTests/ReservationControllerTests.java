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
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

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
        Laboratory lab = new Laboratory("Lab1", "BlockA");
        reservation = new Reservation(lab, LocalDate.of(2025, 3, 15), "10:00-12:00", "Juan Perez");
    }

    @Test
    void testCreateReservation_Success() throws Exception {
        when(reservationService.makeReservation(any(Reservation.class))).thenReturn(reservation);

        mockMvc.perform(post("/api/reservations")
                .contentType(MediaType.APPLICATION_JSON).content("{\"lab\":{\"name\":\"Lab1\",\"block\":\"BlockA\"},\"reserveDate\":\"2025-03-15\",\"reserveTime\":\"10:00-12:00\",\"userName\":\"Juan Perez\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lab.name").value("Lab1"))
                .andExpect(jsonPath("$.lab.block").value("BlockA"))
                .andExpect(jsonPath("$.reserveDate[0]").value(2025))
                .andExpect(jsonPath("$.reserveDate[1]").value(3))
                .andExpect(jsonPath("$.reserveDate[2]").value(15))
                .andExpect(jsonPath("$.reserveTime").value("10:00-12:00"))
                .andExpect(jsonPath("$.userName").value("Juan Perez"));
    }

    @Test
    void testGetAllReservations() throws Exception {
        List<Reservation> reservations = Arrays.asList(reservation);
        when(reservationService.getAllReservations()).thenReturn(reservations);

        mockMvc.perform(get("/api/reservations/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].lab.name").value("Lab1"))
                .andExpect(jsonPath("$[0].lab.block").value("BlockA"));
    }

    @Test
    void testGetReservationById_Success() throws Exception {
        when(reservationService.getReservationById("1")).thenReturn(reservation);

        mockMvc.perform(get("/api/reservations?id=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lab.name").value("Lab1"));
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
    void testCancelReservation_NotFound() throws Exception {
        doThrow(new ReservationNotFoundException("Reserva no encontrada"))
                .when(reservationService).cancelReservation("1");

        mockMvc.perform(delete("/api/reservations?id=1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Reserva no encontrada"));
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
    

}
