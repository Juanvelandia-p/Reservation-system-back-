package edu.eci.cvds.ReservationSystem.serviceTests;

import edu.eci.cvds.ReservationSystem.exception.ReservationNotFoundException;
import edu.eci.cvds.ReservationSystem.model.Laboratory;
import edu.eci.cvds.ReservationSystem.model.Reservation;
import edu.eci.cvds.ReservationSystem.servicios.MakeReservationService;
import edu.eci.cvds.ReservationSystem.mongoConnection.LaboratoryRepository;
import edu.eci.cvds.ReservationSystem.mongoConnection.ReservationRepository;
import edu.eci.cvds.ReservationSystem.mongoConnection.HourRangeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MakeReservationServiceTests {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private LaboratoryRepository laboratoryRepository;

    @Mock
    private HourRangeRepository hoursRangeRepository;

    @InjectMocks
    private MakeReservationService makeReservationService;

    private Laboratory laboratory;
    private Reservation reservation;
    private LocalDate reserveDate;
    private String reserveTime;

    @BeforeEach
    void setUp() {
        laboratory = new Laboratory("Lab A", "Block 1");
        laboratory.setId("Lab123");
        reservation = new Reservation("Lab A", LocalDate.now(), "10:00-12:00", "user123");
        reservation.setId("1");
    }

    @Test
    void shouldThrowExceptionWhenLabNotFound() {
        when(laboratoryRepository.findByName("Lab A")).thenReturn(Optional.empty());

        Exception exception = assertThrows(ReservationNotFoundException.class,
                () -> makeReservationService.makeReservation(reservation));

        assertEquals(ReservationNotFoundException.LAB_NOT_FOUND, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenTimeNotAvailable() {
        when(laboratoryRepository.findByName("Lab A")).thenReturn(Optional.of(laboratory));
        when(hoursRangeRepository.existsByAviableHours("10:00-12:00")).thenReturn(false);

        Exception exception = assertThrows(ReservationNotFoundException.class,
                () -> makeReservationService.makeReservation(reservation));

        assertEquals(ReservationNotFoundException.TIME_NOT_FOUND, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenReservationConflict() {
        when(laboratoryRepository.findByName("Lab A")).thenReturn(Optional.of(laboratory));
        when(hoursRangeRepository.existsByAviableHours("10:00-12:00")).thenReturn(true);
        when(reservationRepository.existsByLabAndReserveDateAndReserveTime(laboratory.getName(), reservation.getReserveDate(), "10:00-12:00"))
                .thenReturn(true);

        Exception exception = assertThrows(ReservationNotFoundException.class,
                () -> makeReservationService.makeReservation(reservation));

        assertEquals(ReservationNotFoundException.CONFLICT, exception.getMessage());
    }

    @Test
    void shouldCreateReservationSuccessfully() {
        when(laboratoryRepository.findByName("Lab A")).thenReturn(Optional.of(laboratory));
        when(hoursRangeRepository.existsByAviableHours("10:00-12:00")).thenReturn(true);
        when(reservationRepository.existsByLabAndReserveDateAndReserveTime(laboratory.getName(), reservation.getReserveDate(), "10:00-12:00"))
                .thenReturn(false);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        Reservation createdReservation = makeReservationService.makeReservation(reservation);

        assertNotNull(createdReservation);
        assertEquals("Lab A", createdReservation.getLab());
        assertEquals("1", createdReservation.getId());
    }

    @Test
    void testGetAllReservations() {
        List<Reservation> reservations = Arrays.asList(reservation, new Reservation("BlockA", reserveDate, "12:00 - 14:00", "Pedro"));
        when(reservationRepository.findAll()).thenReturn(reservations);

        List<Reservation> result = makeReservationService.getAllReservations();

        assertEquals(2, result.size());
    }

    @Test
    void testCancelReservation_Success() {
        String reservationId = "123";
        when(reservationRepository.existsById(reservationId)).thenReturn(true);
        doNothing().when(reservationRepository).deleteById(reservationId);

        assertDoesNotThrow(() -> makeReservationService.cancelReservation(reservationId));
        verify(reservationRepository, times(1)).deleteById(reservationId);
    }

    @Test
    void testCancelReservation_NotFound() {
        String reservationId = "123";
        when(reservationRepository.existsById(reservationId)).thenReturn(false);

        Exception exception = assertThrows(ReservationNotFoundException.class, () -> {
            makeReservationService.cancelReservation(reservationId);
        });

        assertEquals(ReservationNotFoundException.NOT_FOUND, exception.getMessage());
    }

    @Test
    void testIsReserved_True() {
        when(reservationRepository.existsByLabAndReserveDateAndReserveTime(laboratory.getName(), reserveDate, reserveTime)).thenReturn(true);

        boolean result = makeReservationService.isReserved(laboratory.getName(), reserveDate, reserveTime);

        assertTrue(result);
    }

    @Test
    void testIsReserved_False() {
        when(reservationRepository.existsByLabAndReserveDateAndReserveTime(laboratory.getName(), reserveDate, reserveTime)).thenReturn(false);

        boolean result = makeReservationService.isReserved(laboratory.getName(), reserveDate, reserveTime);

        assertFalse(result);
    }

    @Test
    void testGetReservationById_Success() {
        when(reservationRepository.findById(reservation.getId())).thenReturn(Optional.of(reservation));

        Reservation result = makeReservationService.getReservationById(reservation.getId());

        assertNotNull(result);
        assertEquals(reservation.getId(), result.getId());
        assertEquals(reservation.getReserveDate(), result.getReserveDate());
    }

    @Test
    void testGetReservationById_NotFound() {
        when(reservationRepository.findById("invalidId")).thenReturn(Optional.empty());

        Exception exception = assertThrows(ReservationNotFoundException.class, () -> {
            makeReservationService.getReservationById("invalidId");
        });

        assertEquals(ReservationNotFoundException.NOT_FOUND, exception.getMessage());
    }

    @Test
    void testCancelReservation_DeleteError() {
        String reservationId = "12345";

        when(reservationRepository.existsById(reservationId)).thenReturn(true);
        doThrow(new RuntimeException("Database error")).when(reservationRepository).deleteById(reservationId);

        Exception exception = assertThrows(ReservationNotFoundException.class, () -> {
            makeReservationService.cancelReservation(reservationId);
        });

        assertEquals(ReservationNotFoundException.DELETE_ERROR, exception.getMessage());
    }
}
