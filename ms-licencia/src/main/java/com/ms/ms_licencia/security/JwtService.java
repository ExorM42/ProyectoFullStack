package com.ms.ms_licencia.security;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public Claims obtenerClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Long obtenerPacienteId(String token) {
        Object pacienteId = obtenerClaims(token).get("pacienteId");

        if (pacienteId == null) {
            throw new IllegalStateException("El token no contiene pacienteId");
        }

        return Long.valueOf(pacienteId.toString());
    }

    public Long obtenerDoctorId(String token) {
        Object doctorId = obtenerClaims(token).get("doctorId");

        if (doctorId == null) {
            throw new IllegalStateException("El token no contiene doctorId");
        }

        return Long.valueOf(doctorId.toString());
    }

    public String obtenerRol(String token) {
        Object rol = obtenerClaims(token).get("rol");

        if (rol == null) {
            throw new IllegalStateException("El token no contiene rol");
        }

        return rol.toString();
    }
}