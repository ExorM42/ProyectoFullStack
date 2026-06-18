package com.ms.ms_licencia.controller;

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

import com.ms.ms_licencia.dto.ActualizarEstadoLicenciaDTO;
import com.ms.ms_licencia.dto.LicenciaRequestDTO;
import com.ms.ms_licencia.dto.LicenciaResponseDTO;
import com.ms.ms_licencia.security.JwtService;
import com.ms.ms_licencia.service.LicenciaService;

@WebMvcTest(LicenciaController.class)
public class LicenciaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LicenciaService licenciaService;

    @MockBean
    private JwtService jwtService;

    @Test
    void whenObtenerTodas_thenReturnOk() throws Exception {

        // GIVEN
        List<LicenciaResponseDTO> licencias = List.of(new LicenciaResponseDTO());

        // WHEN
        when(licenciaService.obtenerTodas()).thenReturn(licencias);

        mockMvc.perform(get("/api/licencias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)));
    }

    @Test
    void whenObtenerPorId_thenReturnOk() throws Exception {

        // GIVEN
        LicenciaResponseDTO licencia = new LicenciaResponseDTO();
        licencia.setId(1L);

        // WHEN
        when(licenciaService.obtenerPorId(1L)).thenReturn(licencia);

        mockMvc.perform(get("/api/licencias/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1L));
    }

    @Test
    void whenObtenerPorFolio_thenReturnOk() throws Exception {

        // GIVEN
        LicenciaResponseDTO licencia = new LicenciaResponseDTO();
        licencia.setFolio("LIC-1");

        // WHEN
        when(licenciaService.obtenerPorFolio("LIC-1")).thenReturn(licencia);

        mockMvc.perform(get("/api/licencias/folio/{folio}", "LIC-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.folio").value("LIC-1"));
    }

    @Test
    void whenObtenerMisLicencias_thenReturnOk() throws Exception {

        // GIVEN
        List<LicenciaResponseDTO> licencias = List.of(new LicenciaResponseDTO());

        // WHEN
        when(jwtService.obtenerPacienteId("token")).thenReturn(1L);
        when(licenciaService.obtenerPorPaciente(1L)).thenReturn(licencias);

        mockMvc.perform(get("/api/licencias/mis-licencias")
                .header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)));
    }

    @Test
    void whenObtenerMisEmisiones_thenReturnOk() throws Exception {

        // GIVEN
        List<LicenciaResponseDTO> licencias = List.of(new LicenciaResponseDTO());

        // WHEN
        when(jwtService.obtenerDoctorId("token")).thenReturn(2L);
        when(licenciaService.obtenerPorDoctor(2L)).thenReturn(licencias);

        mockMvc.perform(get("/api/licencias/mis-emisiones")
                .header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)));
    }

    @Test
    void whenEmitir_thenReturnCreated() throws Exception {

        // GIVEN
        LicenciaResponseDTO licencia = new LicenciaResponseDTO();
        licencia.setId(1L);

        // WHEN
        when(jwtService.obtenerDoctorId("token")).thenReturn(2L);
        when(licenciaService.emitir(any(LicenciaRequestDTO.class), org.mockito.ArgumentMatchers.eq(2L))).thenReturn(licencia);

        mockMvc.perform(post("/api/licencias")
                .header("Authorization", "Bearer token")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"pacienteId\":1,\"tipoLicenciaId\":1,\"diagnostico\":\"Reposo medico\",\"fechaInicio\":\"2030-01-01\",\"fechaFin\":\"2030-01-02\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").value(1L));
    }

    @Test
    void whenActualizarEstado_thenReturnOk() throws Exception {

        // GIVEN
        LicenciaResponseDTO licencia = new LicenciaResponseDTO();
        licencia.setId(1L);

        // WHEN
        when(jwtService.obtenerDoctorId("token")).thenReturn(2L);
        when(licenciaService.actualizarEstado(org.mockito.ArgumentMatchers.eq(1L), any(ActualizarEstadoLicenciaDTO.class), org.mockito.ArgumentMatchers.eq(2L))).thenReturn(licencia);

        mockMvc.perform(patch("/api/licencias/{id}/estado", 1L)
                .header("Authorization", "Bearer token")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"estado\":\"APROBADA\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1L));
    }

    @Test
    void whenAnular_thenReturnOk() throws Exception {

        // WHEN
        when(jwtService.obtenerDoctorId("token")).thenReturn(2L);
        doNothing().when(licenciaService).anular(1L, 2L);

        mockMvc.perform(patch("/api/licencias/{id}/anular", 1L)
                .header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void whenEliminar_thenReturnOk() throws Exception {

        // WHEN
        doNothing().when(licenciaService).eliminar(1L);

        mockMvc.perform(delete("/api/licencias/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}
