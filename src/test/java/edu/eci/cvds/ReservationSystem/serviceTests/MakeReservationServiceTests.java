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

    private Laboratory lab;
    private Reservation reservation;
    private LocalDate reserveDate;
    private String reserveTime;

    @BeforeEach
    void setUp() {
        lab = new Laboratory("Lab1", "BlockA");
        reserveDate = LocalDate.of(2025, 3, 12);
        reserveTime = "10:00 - 12:00";
        reservation = new Reservation(lab, reserveDate, reserveTime, "Juan");
    }

    @Test
    void testMakeReservation_Success() {
        when(laboratoryRepository.findByNameAndBlock(lab.getName(), lab.getBlock())).thenReturn(Optional.of(lab));
        when(hoursRangeRepository.existsByAviableHours(reserveTime)).thenReturn(true);
        when(reservationRepository.existsByLabAndReserveDateAndReserveTime(lab, reserveDate, reserveTime)).thenReturn(false);
        when(reservationRepository.save(reservation)).thenReturn(reservation);

        Reservation result = makeReservationService.makeReservation(reservation);

        assertNotNull(result);
        assertEquals(reservation.getReserveDate(), result.getReserveDate());
        assertEquals(reservation.getReserveTime(), result.getReserveTime());
        assertEquals(reservation.getLab(), result.getLab());
    }

    @Test
    void testMakeReservation_Conflict() {
        when(laboratoryRepository.findByNameAndBlock(lab.getName(), lab.getBlock())).thenReturn(Optional.of(lab));
        when(hoursRangeRepository.existsByAviableHours(reserveTime)).thenReturn(false);

        Exception exception = assertThrows(ReservationNotFoundException.class, () -> {
            makeReservationService.makeReservation(reservation);
        });

        assertEquals(ReservationNotFoundException.CONFLICT, exception.getMessage());
    }

    @Test
    void testMakeReservation_LaboratoryNotFound() {
        when(laboratoryRepository.findByNameAndBlock(lab.getName(), lab.getBlock())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ReservationNotFoundException.class, () -> {
            makeReservationService.makeReservation(reservation);
        });

        assertEquals(ReservationNotFoundException.LAB_NOT_FOUND, exception.getMessage());
    }

    @Test
    void testMakeReservation_TimeNotFound() {
        when(laboratoryRepository.findByNameAndBlock(lab.getName(), lab.getBlock())).thenReturn(Optional.of(lab));
        when(hoursRangeRepository.existsByAviableHours(reserveTime)).thenReturn(false);

        Exception exception = assertThrows(ReservationNotFoundException.class, () -> {
            makeReservationService.makeReservation(reservation);
        });

        assertEquals(ReservationNotFoundException.TIME_NOT_FOUND, exception.getMessage());
    }

    @Test
    void testGetAllReservations() {
        List<Reservation> reservations = Arrays.asList(reservation, new Reservation(lab, reserveDate, "12:00 - 14:00", "Pedro"));
        when(reservationRepository.findAll()).thenReturn(reservations);

        List<Reservation> result = makeReservationService.getAllReservations();

        assertEquals(2, result.size());
    }

}
