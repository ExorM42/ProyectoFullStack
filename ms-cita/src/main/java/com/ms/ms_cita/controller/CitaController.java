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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Citas", description = "Operaciones relacionadas con la gestion de citas medicas")
@RestController
@RequestMapping("/api/citas")
public class CitaController {

    private final CitaService citaService;
    private final JwtService jwtService;

    public CitaController(CitaService citaService, JwtService jwtService) {
        this.citaService = citaService;
        this.jwtService = jwtService;
    }

    @Operation(summary = "Obtener todas las citas", description = "Devuelve una lista de todas las citas registradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Citas obtenidas correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<CitaResponseDTO>>> obtenerTodasLasCitas() {
        return ResponseEntity.ok(ApiResponseDTO.ok("Citas obtenidas: ", citaService.obtenerTodas()));
    }

    @Operation(summary = "Obtener citas de hoy", description = "Devuelve las citas agendadas para el dia actual")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Citas de hoy obtenidas correctamente")
    })
    @GetMapping("/hoy")
    public ResponseEntity<ApiResponseDTO<List<CitaResponseDTO>>> obtenerCitasHoy() {
        return ResponseEntity.ok(ApiResponseDTO.ok("Citas de hoy :", citaService.obtenerCitasHoy()));
    }

    @Operation(summary = "Obtener cita por ID", description = "Devuelve los detalles de una cita especifica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cita encontrada"),
            @ApiResponse(responseCode = "404", description = "Cita no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<CitaResponseDTO>> obtenerCitaPorId(
            @Parameter(description = "ID de la cita", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponseDTO.ok("Cita encontrada: ", citaService.obtenerPorId(id)));
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Obtener mis citas", description = "Obtiene las citas del paciente autenticado mediante el token JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Citas del paciente obtenidas correctamente"),
            @ApiResponse(responseCode = "401", description = "Token no enviado o invalido")
    })
    @GetMapping("/mis-citas")
    public ResponseEntity<ApiResponseDTO<List<CitaResponseDTO>>> obtenerMisCitas(
            @Parameter(description = "Token JWT con formato Bearer", example = "Bearer eyJhbGciOi...") @RequestHeader("Authorization") String authorizationHeader) {
        String token = extraerToken(authorizationHeader);
        Long pacienteId = jwtService.obtenerPacienteId(token);
        return ResponseEntity.ok(ApiResponseDTO.ok("Citas del paciente: ", citaService.obtenerPorPaciente(pacienteId)));
    }

    @Operation(summary = "Obtener citas por doctor", description = "Devuelve las citas asociadas a un doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Citas del doctor obtenidas correctamente")
    })
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<ApiResponseDTO<List<CitaResponseDTO>>> obtenerPorDoctor(
            @Parameter(description = "ID del doctor", example = "2") @PathVariable Long doctorId) {
        return ResponseEntity.ok(ApiResponseDTO.ok("Citas del doctor : ", citaService.obtenerPorDoctor(doctorId)));
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Crear cita", description = "Agenda una cita para el paciente autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cita agendada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "401", description = "Token no enviado o invalido")
    })
    @PostMapping
    public ResponseEntity<ApiResponseDTO<CitaResponseDTO>> crearCita(
            @Parameter(description = "Token JWT con formato Bearer", example = "Bearer eyJhbGciOi...") @RequestHeader("Authorization") String authorizationHeader,
            @Valid @RequestBody CitaRequestDTO dto) {
        String token = extraerToken(authorizationHeader);
        Long pacienteId = jwtService.obtenerPacienteId(token);
        CitaResponseDTO citaCreada = citaService.crearCita(dto, pacienteId);
        return ResponseEntity.ok(ApiResponseDTO.ok("Cita agendada de manera exitosa", citaCreada));
    }

    @Operation(summary = "Actualizar estado de cita", description = "Actualiza el estado y observaciones de una cita")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Cita no encontrada")
    })
    @PatchMapping("/{id}/estado")
    public ResponseEntity<ApiResponseDTO<CitaResponseDTO>> actualizarEstado(
            @Parameter(description = "ID de la cita", example = "1") @PathVariable Long id,
            @Valid @RequestBody ActualizarEstadoCitaDTO dto) {
        return ResponseEntity.ok(ApiResponseDTO.ok("Estado de cita actualizado de manera exitosa", citaService.actualizarEstado(id, dto)));
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Cancelar cita", description = "Cancela una cita perteneciente al paciente autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cita cancelada correctamente"),
            @ApiResponse(responseCode = "401", description = "Token no enviado o invalido"),
            @ApiResponse(responseCode = "404", description = "Cita no encontrada")
    })
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<ApiResponseDTO<Void>> cancelarCita(
            @Parameter(description = "Token JWT con formato Bearer", example = "Bearer eyJhbGciOi...") @RequestHeader("Authorization") String authorizationHeader,
            @Parameter(description = "ID de la cita", example = "1") @PathVariable Long id) {
        String token = extraerToken(authorizationHeader);
        Long pacienteId = jwtService.obtenerPacienteId(token);
        citaService.cancelar(id, pacienteId);
        return ResponseEntity.ok(ApiResponseDTO.ok("Cita cancelada de manera exitosa", null));
    }

    @Operation(summary = "Eliminar cita", description = "Elimina una cita existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cita eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Cita no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> eliminarCita(
            @Parameter(description = "ID de la cita", example = "1") @PathVariable Long id) {
        citaService.eliminar(id);
        return ResponseEntity.ok(ApiResponseDTO.ok("Cita eliminada de manera exitosa", null));
    }

    private String extraerToken(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Token no enviado o invalido");
        }
        return authorizationHeader.substring(7);
    }
}