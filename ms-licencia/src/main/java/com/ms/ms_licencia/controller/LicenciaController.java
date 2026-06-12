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

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/licencias")
public class LicenciaController {

    private final LicenciaService licenciaService;
    private final JwtService jwtService;

    public LicenciaController(LicenciaService licenciaService, JwtService jwtService) {
        this.licenciaService = licenciaService;
        this.jwtService = jwtService;
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<LicenciaResponseDTO>>> obtenerTodas() {
        return ResponseEntity.ok(ApiResponseDTO.ok("Licencias obtenidas", licenciaService.obtenerTodas()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<LicenciaResponseDTO>> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponseDTO.ok("Licencia encontrada", licenciaService.obtenerPorId(id)));
    }

    @GetMapping("/folio/{folio}")
    public ResponseEntity<ApiResponseDTO<LicenciaResponseDTO>> obtenerPorFolio(@PathVariable String folio) {
        return ResponseEntity.ok(ApiResponseDTO.ok("Licencia encontrada", licenciaService.obtenerPorFolio(folio)));
    }

    @GetMapping("/mis-licencias")
    public ResponseEntity<ApiResponseDTO<List<LicenciaResponseDTO>>> obtenerMisLicencias(
            @RequestHeader("Authorization") String authorizationHeader) {

        String token = extraerToken(authorizationHeader);
        Long pacienteId = jwtService.obtenerPacienteId(token);

        return ResponseEntity.ok(ApiResponseDTO.ok("Licencias del paciente",
                licenciaService.obtenerPorPaciente(pacienteId)));
    }

    @GetMapping("/mis-emisiones")
    public ResponseEntity<ApiResponseDTO<List<LicenciaResponseDTO>>> obtenerMisEmisiones(
            @RequestHeader("Authorization") String authorizationHeader) {

        String token = extraerToken(authorizationHeader);
        Long doctorId = jwtService.obtenerDoctorId(token);

        return ResponseEntity.ok(ApiResponseDTO.ok("Licencias emitidas por el doctor",
                licenciaService.obtenerPorDoctor(doctorId)));
    }

    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<ApiResponseDTO<List<LicenciaResponseDTO>>> obtenerPorPaciente(
            @PathVariable Long pacienteId) {
        return ResponseEntity.ok(ApiResponseDTO.ok("Licencias del paciente",
                licenciaService.obtenerPorPaciente(pacienteId)));
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<ApiResponseDTO<List<LicenciaResponseDTO>>> obtenerPorDoctor(
            @PathVariable Long doctorId) {
        return ResponseEntity.ok(ApiResponseDTO.ok("Licencias del doctor",
                licenciaService.obtenerPorDoctor(doctorId)));
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<LicenciaResponseDTO>> emitir(
            @RequestHeader("Authorization") String authorizationHeader,
            @Valid @RequestBody LicenciaRequestDTO dto) {

        String token = extraerToken(authorizationHeader);
        Long doctorId = jwtService.obtenerDoctorId(token);

        LicenciaResponseDTO emitida = licenciaService.emitir(dto, doctorId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.ok("Licencia emitida exitosamente", emitida));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<ApiResponseDTO<LicenciaResponseDTO>> actualizarEstado(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long id,
            @Valid @RequestBody ActualizarEstadoLicenciaDTO dto) {

        String token = extraerToken(authorizationHeader);
        Long doctorId = jwtService.obtenerDoctorId(token);

        return ResponseEntity.ok(ApiResponseDTO.ok("Estado actualizado",
                licenciaService.actualizarEstado(id, dto, doctorId)));
    }

    @PatchMapping("/{id}/anular")
    public ResponseEntity<ApiResponseDTO<Void>> anular(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long id) {

        String token = extraerToken(authorizationHeader);
        Long doctorId = jwtService.obtenerDoctorId(token);

        licenciaService.anular(id, doctorId);
        return ResponseEntity.ok(ApiResponseDTO.ok("Licencia anulada exitosamente", null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> eliminar(@PathVariable Long id) {
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