package com.ms.ms_licencia.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
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

import com.ms.ms_licencia.dto.ActualizarEstadoLicenciaDTO;
import com.ms.ms_licencia.dto.ApiResponseDTO;
import com.ms.ms_licencia.dto.LicenciaRequestDTO;
import com.ms.ms_licencia.dto.LicenciaResponseDTO;
import com.ms.ms_licencia.security.JwtService;
import com.ms.ms_licencia.service.LicenciaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Licencias", description = "Operaciones relacionadas con licencias medicas")
@RestController
@RequestMapping("/api/licencias")
public class LicenciaController {

    private final LicenciaService licenciaService;
    private final JwtService jwtService;

    public LicenciaController(LicenciaService licenciaService, JwtService jwtService) {
        this.licenciaService = licenciaService;
        this.jwtService = jwtService;
    }

    @Operation(summary = "Obtener todas las licencias", description = "Devuelve una lista de todas las licencias medicas registradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Licencias obtenidas correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<LicenciaResponseDTO>>> obtenerTodas() {
        return ResponseEntity.ok(ApiResponseDTO.ok("Licencias obtenidas", licenciaService.obtenerTodas()));
    }

    @Operation(summary = "Obtener licencia por ID", description = "Devuelve los detalles de una licencia especifica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Licencia encontrada"),
            @ApiResponse(responseCode = "404", description = "Licencia no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<LicenciaResponseDTO>> obtenerPorId(
            @Parameter(description = "ID de la licencia", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponseDTO.ok("Licencia encontrada", licenciaService.obtenerPorId(id)));
    }

    @Operation(summary = "Obtener licencia por folio", description = "Devuelve una licencia segun su folio")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Licencia encontrada"),
            @ApiResponse(responseCode = "404", description = "Licencia no encontrada")
    })
    @GetMapping("/folio/{folio}")
    public ResponseEntity<ApiResponseDTO<LicenciaResponseDTO>> obtenerPorFolio(
            @Parameter(description = "Folio de la licencia", example = "LIC-20260617-1234") @PathVariable String folio) {
        return ResponseEntity.ok(ApiResponseDTO.ok("Licencia encontrada", licenciaService.obtenerPorFolio(folio)));
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Obtener mis licencias", description = "Obtiene las licencias del paciente autenticado por JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Licencias del paciente obtenidas correctamente"),
            @ApiResponse(responseCode = "401", description = "Token no enviado o invalido")
    })
    @GetMapping("/mis-licencias")
    public ResponseEntity<ApiResponseDTO<List<LicenciaResponseDTO>>> obtenerMisLicencias(
            @Parameter(description = "Token JWT con formato Bearer", example = "Bearer eyJhbGciOi...") @RequestHeader("Authorization") String authorizationHeader) {
        String token = extraerToken(authorizationHeader);
        Long pacienteId = jwtService.obtenerPacienteId(token);
        return ResponseEntity.ok(ApiResponseDTO.ok("Licencias del paciente", licenciaService.obtenerPorPaciente(pacienteId)));
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Obtener mis emisiones", description = "Obtiene las licencias emitidas por el doctor autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Licencias del doctor obtenidas correctamente"),
            @ApiResponse(responseCode = "401", description = "Token no enviado o invalido")
    })
    @GetMapping("/mis-emisiones")
    public ResponseEntity<ApiResponseDTO<List<LicenciaResponseDTO>>> obtenerMisEmisiones(
            @Parameter(description = "Token JWT con formato Bearer", example = "Bearer eyJhbGciOi...") @RequestHeader("Authorization") String authorizationHeader) {
        String token = extraerToken(authorizationHeader);
        Long doctorId = jwtService.obtenerDoctorId(token);
        return ResponseEntity.ok(ApiResponseDTO.ok("Licencias emitidas por el doctor", licenciaService.obtenerPorDoctor(doctorId)));
    }

    @Operation(summary = "Obtener licencias por paciente", description = "Devuelve licencias asociadas a un paciente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Licencias del paciente obtenidas correctamente")
    })
    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<ApiResponseDTO<List<LicenciaResponseDTO>>> obtenerPorPaciente(
            @Parameter(description = "ID del paciente", example = "1") @PathVariable Long pacienteId) {
        return ResponseEntity.ok(ApiResponseDTO.ok("Licencias del paciente", licenciaService.obtenerPorPaciente(pacienteId)));
    }

    @Operation(summary = "Obtener licencias por doctor", description = "Devuelve licencias emitidas por un doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Licencias del doctor obtenidas correctamente")
    })
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<ApiResponseDTO<List<LicenciaResponseDTO>>> obtenerPorDoctor(
            @Parameter(description = "ID del doctor", example = "2") @PathVariable Long doctorId) {
        return ResponseEntity.ok(ApiResponseDTO.ok("Licencias del doctor", licenciaService.obtenerPorDoctor(doctorId)));
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Emitir licencia", description = "Emite una licencia medica usando el doctor autenticado por JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Licencia emitida correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "409", description = "Conflicto con reglas de licencia")
    })
    @PostMapping
    public ResponseEntity<ApiResponseDTO<LicenciaResponseDTO>> emitir(
            @Parameter(description = "Token JWT con formato Bearer", example = "Bearer eyJhbGciOi...") @RequestHeader("Authorization") String authorizationHeader,
            @Valid @RequestBody LicenciaRequestDTO dto) {
        String token = extraerToken(authorizationHeader);
        Long doctorId = jwtService.obtenerDoctorId(token);
        LicenciaResponseDTO emitida = licenciaService.emitir(dto, doctorId);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseDTO.ok("Licencia emitida exitosamente", emitida));
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Actualizar estado de licencia", description = "Actualiza el estado de una licencia emitida por el doctor autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Licencia no encontrada")
    })
    @PatchMapping("/{id}/estado")
    public ResponseEntity<ApiResponseDTO<LicenciaResponseDTO>> actualizarEstado(
            @Parameter(description = "Token JWT con formato Bearer", example = "Bearer eyJhbGciOi...") @RequestHeader("Authorization") String authorizationHeader,
            @Parameter(description = "ID de la licencia", example = "1") @PathVariable Long id,
            @Valid @RequestBody ActualizarEstadoLicenciaDTO dto) {
        String token = extraerToken(authorizationHeader);
        Long doctorId = jwtService.obtenerDoctorId(token);
        return ResponseEntity.ok(ApiResponseDTO.ok("Estado actualizado", licenciaService.actualizarEstado(id, dto, doctorId)));
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Anular licencia", description = "Anula una licencia emitida por el doctor autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Licencia anulada correctamente"),
            @ApiResponse(responseCode = "404", description = "Licencia no encontrada")
    })
    @PatchMapping("/{id}/anular")
    public ResponseEntity<ApiResponseDTO<Void>> anular(
            @Parameter(description = "Token JWT con formato Bearer", example = "Bearer eyJhbGciOi...") @RequestHeader("Authorization") String authorizationHeader,
            @Parameter(description = "ID de la licencia", example = "1") @PathVariable Long id) {
        String token = extraerToken(authorizationHeader);
        Long doctorId = jwtService.obtenerDoctorId(token);
        licenciaService.anular(id, doctorId);
        return ResponseEntity.ok(ApiResponseDTO.ok("Licencia anulada exitosamente", null));
    }

    @Operation(summary = "Eliminar licencia", description = "Elimina una licencia existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Licencia eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Licencia no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> eliminar(
            @Parameter(description = "ID de la licencia", example = "1") @PathVariable Long id) {
        licenciaService.eliminar(id);
        return ResponseEntity.ok(ApiResponseDTO.ok("Licencia eliminada", null));
    }

    private String extraerToken(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Token no enviado o invalido");
        }
        return authorizationHeader.substring(7);
    }
}