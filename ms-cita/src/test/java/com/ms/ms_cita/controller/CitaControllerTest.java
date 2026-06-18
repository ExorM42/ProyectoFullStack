package com.ms.ms_cita.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.ms.ms_cita.dto.ActualizarEstadoCitaDTO;
import com.ms.ms_cita.dto.CitaRequestDTO;
import com.ms.ms_cita.dto.CitaResponseDTO;
import com.ms.ms_cita.security.JwtService;
import com.ms.ms_cita.service.CitaService;

@WebMvcTest(CitaController.class)
public class CitaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CitaService citaService;

    @MockBean
    private JwtService jwtService;

    @Test
    void whenObtenerTodasLasCitas_thenReturnOk() throws Exception {

        // GIVEN
        List<CitaResponseDTO> citas = List.of(new CitaResponseDTO());

        // WHEN
        when(citaService.obtenerTodas()).thenReturn(citas);

        mockMvc.perform(get("/api/citas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)));
    }

    @Test
    void whenObtenerCitaPorId_thenReturnOk() throws Exception {

        // GIVEN
        CitaResponseDTO cita = new CitaResponseDTO();
        cita.setId(1L);

        // WHEN
        when(citaService.obtenerPorId(1L)).thenReturn(cita);

        mockMvc.perform(get("/api/citas/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1L));
    }

    @Test
    void whenObtenerMisCitas_thenReturnOk() throws Exception {

        // GIVEN
        List<CitaResponseDTO> citas = List.of(new CitaResponseDTO());

        // WHEN
        when(jwtService.obtenerPacienteId("token")).thenReturn(1L);
        when(citaService.obtenerPorPaciente(1L)).thenReturn(citas);

        mockMvc.perform(get("/api/citas/mis-citas")
                .header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)));
    }

    @Test
    void whenCrearCita_thenReturnOk() throws Exception {

        // GIVEN
        CitaResponseDTO cita = new CitaResponseDTO();
        cita.setId(1L);

        // WHEN
        when(jwtService.obtenerPacienteId("token")).thenReturn(1L);
        when(citaService.crearCita(any(CitaRequestDTO.class), org.mockito.ArgumentMatchers.eq(1L))).thenReturn(cita);

        mockMvc.perform(post("/api/citas")
                .header("Authorization", "Bearer token")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"doctorId\":2,\"fechaHoraCita\":\"2030-01-01T10:00:00\",\"motivoConsulta\":\"Consulta general\",\"duracionMinutos\":30}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1L));
    }

    @Test
    void whenActualizarEstado_thenReturnOk() throws Exception {

        // GIVEN
        CitaResponseDTO cita = new CitaResponseDTO();
        cita.setId(1L);

        // WHEN
        when(citaService.actualizarEstado(org.mockito.ArgumentMatchers.eq(1L), any(ActualizarEstadoCitaDTO.class))).thenReturn(cita);

        mockMvc.perform(patch("/api/citas/{id}/estado", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"estado\":\"CONFIRMADA\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1L));
    }

    @Test
    void whenCancelarCita_thenReturnOk() throws Exception {

        // WHEN
        when(jwtService.obtenerPacienteId("token")).thenReturn(1L);
        doNothing().when(citaService).cancelar(1L, 1L);

        mockMvc.perform(patch("/api/citas/{id}/cancelar", 1L)
                .header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void whenEliminarCita_thenReturnOk() throws Exception {

        // WHEN
        doNothing().when(citaService).eliminar(1L);

        mockMvc.perform(delete("/api/citas/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}
