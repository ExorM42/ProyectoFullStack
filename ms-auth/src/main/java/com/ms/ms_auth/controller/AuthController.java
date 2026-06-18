package com.ms.ms_auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ms.ms_auth.dto.ApiResponseDTO;
import com.ms.ms_auth.dto.AuthResponseDTO;
import com.ms.ms_auth.dto.LoginRequestDTO;
import com.ms.ms_auth.dto.RegisterRequestDTO;
import com.ms.ms_auth.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Autenticacion", description = "Operaciones relacionadas con registro, login y emision de tokens JWT")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Registrar usuario", description = "Crea un usuario de autenticacion para paciente, doctor o administrador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario registrado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos de registro invalidos"),
            @ApiResponse(responseCode = "409", description = "El email ya se encuentra registrado")
    })
    @PostMapping("/registro")
    public ResponseEntity<ApiResponseDTO<AuthResponseDTO>> registrar(@Valid @RequestBody RegisterRequestDTO dto) {
        return ResponseEntity.ok(ApiResponseDTO.ok(true, "Usuario registrado correctamente", authService.registrar(dto)));
    }

    @Operation(summary = "Iniciar sesion", description = "Valida las credenciales del usuario y devuelve un token JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login exitoso"),
            @ApiResponse(responseCode = "401", description = "Credenciales invalidas")
    })
    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO<AuthResponseDTO>> login(@Valid @RequestBody LoginRequestDTO dto) {
        return ResponseEntity.ok(ApiResponseDTO.ok(true, "Login exitoso", authService.login(dto)));
    }
}