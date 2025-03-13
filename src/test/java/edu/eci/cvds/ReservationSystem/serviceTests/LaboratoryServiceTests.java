package edu.eci.cvds.ReservationSystem.serviceTests;

import edu.eci.cvds.ReservationSystem.exception.ReservationNotFoundException;
import edu.eci.cvds.ReservationSystem.model.Laboratory;
import edu.eci.cvds.ReservationSystem.servicios.LaboratoryService;
import edu.eci.cvds.ReservationSystem.mongoConnection.LaboratoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class LaboratoryServiceTests {

    @Mock
    private LaboratoryRepository laboratoryRepository;

    @InjectMocks
    private LaboratoryService laboratoryService;

    private Laboratory lab;

    @BeforeEach
    void setUp() {
        lab = new Laboratory("Lab1", "BlockA");
    }

    @Test
    void testAddLaboratory_Success() {
        when(laboratoryRepository.existsByNameAndBlock(lab.getName(), lab.getBlock())).thenReturn(false);
        when(laboratoryRepository.save(lab)).thenReturn(lab);

        Laboratory result = laboratoryService.addLaboratory(lab);

        assertNotNull(result);
        assertEquals("Lab1", result.getName());
        assertEquals("BlockA", result.getBlock());

        verify(laboratoryRepository, times(1)).existsByNameAndBlock(lab.getName(), lab.getBlock());
        verify(laboratoryRepository, times(1)).save(lab);
    }

    @Test
    void testAddLaboratory_ThrowsExceptionWhenDuplicate() {
        when(laboratoryRepository.existsByNameAndBlock(lab.getName(), lab.getBlock())).thenReturn(true);

        ReservationNotFoundException exception = assertThrows(ReservationNotFoundException.class, () -> {
            laboratoryService.addLaboratory(lab);
        });

        assertEquals(ReservationNotFoundException.DUPLICATE_LAB, exception.getMessage());

        verify(laboratoryRepository, times(1)).existsByNameAndBlock(lab.getName(), lab.getBlock());
        verify(laboratoryRepository, never()).save(any());
    }

    @Test
    void testGetAllLaboratories() {
        List<Laboratory> labs = Arrays.asList(
                new Laboratory("Lab1", "BlockA"),
                new Laboratory("Lab2", "BlockB")
        );
        when(laboratoryRepository.findAll()).thenReturn(labs);

        List<Laboratory> result = laboratoryService.getAllLaboratories();

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(laboratoryRepository, times(1)).findAll();
    }
}
