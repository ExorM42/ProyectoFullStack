package com.ms.ms_auth.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ms.ms_auth.dto.AuthResponseDTO;
import com.ms.ms_auth.dto.LoginRequestDTO;
import com.ms.ms_auth.dto.RegisterRequestDTO;
import com.ms.ms_auth.model.Rol;
import com.ms.ms_auth.model.Usuario;
import com.ms.ms_auth.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    private JwtService jwtService;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secret", "12345678901234567890123456789012345678901234567890");
        ReflectionTestUtils.setField(jwtService, "expiration", 14400000L);
        authService = new AuthService(usuarioRepo, passwordEncoder, jwtService);
    }

    @Test
    void givenPacienteRegister_whenRegistrar_thenReturnToken() {

        // GIVEN
        RegisterRequestDTO dto = new RegisterRequestDTO();
        dto.setEmail("paciente@test.com");
        dto.setPassword("123456");
        dto.setRol(Rol.PACIENTE);
        dto.setPacienteId(1L);

        Usuario guardado = new Usuario();
        guardado.setId(1L);
        guardado.setEmail(dto.getEmail());
        guardado.setPassword("encoded");
        guardado.setRol(Rol.PACIENTE);
        guardado.setPacienteId(1L);
        guardado.setActivo(true);

        // WHEN
        when(usuarioRepo.existsByEmail(dto.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("encoded");
        when(usuarioRepo.save(any(Usuario.class))).thenReturn(guardado);

        AuthResponseDTO res = authService.registrar(dto);

        // THEN
        assertNotNull(res);
        assertNotNull(res.getToken());
        assertEquals(Rol.PACIENTE, res.getRol());
        assertEquals(1L, res.getPacienteId());

        verify(usuarioRepo, times(1)).existsByEmail(dto.getEmail());
        verify(usuarioRepo, times(1)).save(any(Usuario.class));
    }

    @Test
    void givenEmailExistente_whenRegistrar_thenThrowIllegalStateException() {

        // GIVEN
        RegisterRequestDTO dto = new RegisterRequestDTO();
        dto.setEmail("paciente@test.com");
        dto.setPassword("123456");
        dto.setRol(Rol.PACIENTE);
        dto.setPacienteId(1L);

        // WHEN
        when(usuarioRepo.existsByEmail(dto.getEmail())).thenReturn(true);

        // THEN
        assertThrows(IllegalStateException.class, () -> {
            authService.registrar(dto);
        });

        verify(usuarioRepo, times(1)).existsByEmail(dto.getEmail());
    }

    @Test
    void givenPacienteSinId_whenRegistrar_thenThrowIllegalStateException() {

        // GIVEN
        RegisterRequestDTO dto = new RegisterRequestDTO();
        dto.setEmail("paciente@test.com");
        dto.setPassword("123456");
        dto.setRol(Rol.PACIENTE);

        // WHEN
        when(usuarioRepo.existsByEmail(dto.getEmail())).thenReturn(false);

        // THEN
        assertThrows(IllegalStateException.class, () -> {
            authService.registrar(dto);
        });

        verify(usuarioRepo, times(1)).existsByEmail(dto.getEmail());
    }

    @Test
    void givenLoginCorrecto_whenLogin_thenReturnToken() {

        // GIVEN
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setEmail("doctor@test.com");
        dto.setPassword("123456");

        Usuario usuario = new Usuario();
        usuario.setEmail(dto.getEmail());
        usuario.setPassword("encoded");
        usuario.setRol(Rol.DOCTOR);
        usuario.setDoctorId(2L);
        usuario.setActivo(true);

        // WHEN
        when(usuarioRepo.findByEmail(dto.getEmail())).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(dto.getPassword(), usuario.getPassword())).thenReturn(true);

        AuthResponseDTO res = authService.login(dto);

        // THEN
        assertNotNull(res);
        assertNotNull(res.getToken());
        assertEquals(Rol.DOCTOR, res.getRol());
        assertEquals(2L, res.getDoctorId());

        verify(usuarioRepo, times(1)).findByEmail(dto.getEmail());
    }

    @Test
    void givenPasswordIncorrecta_whenLogin_thenThrowIllegalStateException() {

        // GIVEN
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setEmail("doctor@test.com");
        dto.setPassword("mala");

        Usuario usuario = new Usuario();
        usuario.setEmail(dto.getEmail());
        usuario.setPassword("encoded");
        usuario.setRol(Rol.DOCTOR);
        usuario.setDoctorId(2L);
        usuario.setActivo(true);

        // WHEN
        when(usuarioRepo.findByEmail(dto.getEmail())).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(dto.getPassword(), usuario.getPassword())).thenReturn(false);

        // THEN
        assertThrows(IllegalStateException.class, () -> {
            authService.login(dto);
        });

        verify(usuarioRepo, times(1)).findByEmail(dto.getEmail());
    }
}
