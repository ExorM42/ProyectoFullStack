package com.ms.ms_auth.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.ms.ms_auth.dto.AuthResponseDTO;
import com.ms.ms_auth.dto.LoginRequestDTO;
import com.ms.ms_auth.dto.RegisterRequestDTO;
import com.ms.ms_auth.model.Rol;
import com.ms.ms_auth.service.AuthService;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Test
    void whenRegistrar_thenReturnOk() throws Exception {

        // GIVEN
        AuthResponseDTO response = new AuthResponseDTO("token", Rol.PACIENTE, 1L, null);

        // WHEN
        when(authService.registrar(org.mockito.ArgumentMatchers.any(RegisterRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/registro")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"test@test.com\",\"password\":\"123456\",\"rol\":\"PACIENTE\",\"pacienteId\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.token").value("token"));
    }

    @Test
    void whenLogin_thenReturnOk() throws Exception {

        // GIVEN
        AuthResponseDTO response = new AuthResponseDTO("token", Rol.PACIENTE, 1L, null);

        // WHEN
        when(authService.login(org.mockito.ArgumentMatchers.any(LoginRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"test@test.com\",\"password\":\"123456\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.token").value("token"));
    }
}
