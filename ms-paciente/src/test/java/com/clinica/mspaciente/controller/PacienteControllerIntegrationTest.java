package com.clinica.mspaciente.controller;

import com.clinica.mspaciente.dto.PacienteResponseDTO;
import com.clinica.mspaciente.service.PacienteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PacienteController.class)
class PacienteControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PacienteService pacienteService;

    @Test
    void obtenerTodosRespondeOkConJson() throws Exception {
        PacienteResponseDTO paciente = new PacienteResponseDTO();
        paciente.setId(1L);
        paciente.setRut("12345678-9");
        paciente.setNombre("Juan");
        paciente.setApellido("Perez");
        paciente.setFechaNacimiento(LocalDate.of(1990, 4, 10));
        paciente.setActivo(true);
        when(pacienteService.obtenerTodos()).thenReturn(List.of(paciente));

        mockMvc.perform(get("/api/pacientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].rut").value("12345678-9"));
    }

    @Test
    void crearConBodyInvalidoRespondeBadRequest() throws Exception {
        mockMvc.perform(post("/api/pacientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }
}

