package com.ms.ms_cita.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ms.ms_cita.dto.ActualizarEstadoCitaDTO;
import com.ms.ms_cita.dto.ApiResponseDTO;
import com.ms.ms_cita.dto.CitaRequestDTO;
import com.ms.ms_cita.dto.CitaResponseDTO;
import com.ms.ms_cita.security.JwtService;
import com.ms.ms_cita.service.CitaService;

import jakarta.validation.Valid;



@RestController
@RequestMapping("/api/citas")
public class CitaController {

    private final CitaService citaService;
    private final JwtService jwtService;

    public CitaController(CitaService citaService, JwtService jwtService){
        this.citaService = citaService;
        this.jwtService = jwtService;
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<CitaResponseDTO>>> obtenerTodasLasCitas(){
        return ResponseEntity.ok(ApiResponseDTO.ok("Citas obtenidas: ", citaService.obtenerTodas()));
    }

    @GetMapping("/hoy")
    public ResponseEntity<ApiResponseDTO<List<CitaResponseDTO>>> obtenerCitasHoy() {
        return ResponseEntity.ok(ApiResponseDTO.ok("Citas de hoy :", citaService.obtenerCitasHoy()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<CitaResponseDTO>> obtenerCitaPorId(@PathVariable Long id){
        return ResponseEntity.ok(ApiResponseDTO.ok("Cita encontrada: ", citaService.obtenerPorId(id)));
    }

    @GetMapping("/mis-citas")
    public ResponseEntity<ApiResponseDTO<List<CitaResponseDTO>>> obtenerMisCitas(
            @RequestHeader("Authorization") String authorizationHeader) {

        String token = extraerToken(authorizationHeader);
        Long pacienteId = jwtService.obtenerPacienteId(token);

        return ResponseEntity.ok(
                ApiResponseDTO.ok("Citas del paciente: ", citaService.obtenerPorPaciente(pacienteId))
        );
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<ApiResponseDTO<List<CitaResponseDTO>>> obtenerPorDoctor(@PathVariable Long doctorId) {
        return ResponseEntity.ok(ApiResponseDTO.ok("Citas del doctor : ", citaService.obtenerPorDoctor(doctorId)));
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<CitaResponseDTO>> crearCita(
            @RequestHeader("Authorization") String authorizationHeader,
            @Valid @RequestBody CitaRequestDTO dto) {

        String token = extraerToken(authorizationHeader);
        Long pacienteId = jwtService.obtenerPacienteId(token);

        CitaResponseDTO citaCreada = citaService.crearCita(dto, pacienteId);

        return ResponseEntity.ok(ApiResponseDTO.ok("Cita agendada de manera exitosa", citaCreada));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<ApiResponseDTO<CitaResponseDTO>> actualizarEstado(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarEstadoCitaDTO dto) {

        return ResponseEntity.ok(
                ApiResponseDTO.ok("Estado de cita actualizado de manera exitosa", citaService.actualizarEstado(id, dto))
        );
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<ApiResponseDTO<Void>> cancelarCita(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long id) {

        String token = extraerToken(authorizationHeader);
        Long pacienteId = jwtService.obtenerPacienteId(token);

        citaService.cancelar(id, pacienteId);

        return ResponseEntity.ok(ApiResponseDTO.ok("Cita cancelada de manera exitosa", null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> eliminarCita(@PathVariable Long id){
        citaService.eliminar(id);
        return ResponseEntity.ok(ApiResponseDTO.ok("Cita eliminada de manera exitosa", null));
    }

    private String extraerToken(String authorizationHeader){
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Token no enviado o invalido");
        }

        return authorizationHeader.substring(7);
    }
}
