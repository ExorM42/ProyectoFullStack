package com.ms.ms_auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ms.ms_auth.service.AuthService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ms.ms_auth.dto.ApiResponseDTO;
import com.ms.ms_auth.dto.AuthResponseDTO;
import com.ms.ms_auth.dto.LoginRequestDTO;
import com.ms.ms_auth.dto.RegisterRequestDTO;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/registro")
    public ResponseEntity<ApiResponseDTO<AuthResponseDTO>> registrar(@Valid @RequestBody RegisterRequestDTO dto){
        return ResponseEntity.ok(ApiResponseDTO.ok(true, "Usuario registrado correctamente", authService.registrar(dto)));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO<AuthResponseDTO>> login(@Valid @RequestBody LoginRequestDTO dto){
        return ResponseEntity.ok(ApiResponseDTO.ok(true, "Login exitoso", authService.login(dto)));
    }
    
    

    

}
