package edu.eci.cvds.ReservationSystem.controllerTests;

import edu.eci.cvds.ReservationSystem.exception.ReservationNotFoundException;
import edu.eci.cvds.ReservationSystem.model.Laboratory;
import edu.eci.cvds.ReservationSystem.servicios.LaboratoryService;
import edu.eci.cvds.ReservationSystem.controller.LaboratoryController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class LaboratoryControllerTests {

    private MockMvc mockMvc;

    @Mock
    private LaboratoryService laboratoryService;

    @InjectMocks
    private LaboratoryController laboratoryController;

    private Laboratory lab;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(laboratoryController).build();
        lab = new Laboratory("Lab1", "BlockA");
    }

    @Test
    void testCreateLaboratory_Success() throws Exception {
        when(laboratoryService.addLaboratory(any(Laboratory.class))).thenReturn(lab);

        mockMvc.perform(post("/api/laboratories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Lab1\",\"block\":\"BlockA\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Lab1"))
                .andExpect(jsonPath("$.block").value("BlockA"));
    }

    @Test
    void testCreateLaboratory_Conflict() throws Exception {
        when(laboratoryService.addLaboratory(any(Laboratory.class)))
                .thenThrow(new ReservationNotFoundException(ReservationNotFoundException.DUPLICATE_LAB));

        mockMvc.perform(post("/api/laboratories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Lab1\",\"block\":\"BlockA\"}"))
                .andExpect(status().isConflict())
                .andExpect(content().string(ReservationNotFoundException.DUPLICATE_LAB));
    }

    @Test
    void testGetAllLaboratories() throws Exception {
        List<Laboratory> labs = Arrays.asList(lab, new Laboratory("Lab2", "BlockB"));
        when(laboratoryService.getAllLaboratories()).thenReturn(labs);

        mockMvc.perform(get("/api/laboratories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Lab1"))
                .andExpect(jsonPath("$[0].block").value("BlockA"))
                .andExpect(jsonPath("$[1].name").value("Lab2"))
                .andExpect(jsonPath("$[1].block").value("BlockB"));
    }
}
