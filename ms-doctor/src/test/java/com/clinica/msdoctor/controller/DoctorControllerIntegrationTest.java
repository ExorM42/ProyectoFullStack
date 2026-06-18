package com.clinica.msdoctor.controller;

import com.clinica.msdoctor.dto.DoctorResponseDTO;
import com.clinica.msdoctor.service.DoctorService;
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

@WebMvcTest(DoctorController.class)
class DoctorControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DoctorService doctorService;

    @Test
    void obtenerTodosRespondeOkConJson() throws Exception {
        DoctorResponseDTO doctor = new DoctorResponseDTO();
        doctor.setId(1L);
        doctor.setRut("11222333-4");
        doctor.setNombre("Ana");
        doctor.setApellido("Gomez");
        doctor.setNumeroRegistro("REG-MED-1001");
        doctor.setFechaContratacion(LocalDate.of(2022, 3, 1));
        doctor.setActivo(true);
        when(doctorService.obtenerTodos()).thenReturn(List.of(doctor));

        mockMvc.perform(get("/api/doctores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].numeroRegistro").value("REG-MED-1001"));
    }

    @Test
    void crearConBodyInvalidoRespondeBadRequest() throws Exception {
        mockMvc.perform(post("/api/doctores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }
}

