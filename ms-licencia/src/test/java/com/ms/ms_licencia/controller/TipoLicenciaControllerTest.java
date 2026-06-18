package com.ms.ms_licencia.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.ms.ms_licencia.dto.TipoLicenciaResponseDTO;
import com.ms.ms_licencia.service.TipoLicenciaService;

@WebMvcTest(TipoLicenciaController.class)
public class TipoLicenciaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TipoLicenciaService tipoLicenciaService;

    @Test
    void whenListar_thenReturnOk() throws Exception {

        // GIVEN
        List<TipoLicenciaResponseDTO> tipos = List.of(new TipoLicenciaResponseDTO());

        // WHEN
        when(tipoLicenciaService.obtenerTodos()).thenReturn(tipos);

        mockMvc.perform(get("/api/tipos-licencia"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)));
    }

    @Test
    void whenObtenerPorId_thenReturnOk() throws Exception {

        // GIVEN
        TipoLicenciaResponseDTO tipo = new TipoLicenciaResponseDTO();
        tipo.setId(1L);

        // WHEN
        when(tipoLicenciaService.obtenerPorId(1L)).thenReturn(tipo);

        mockMvc.perform(get("/api/tipos-licencia/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1L));
    }
}
