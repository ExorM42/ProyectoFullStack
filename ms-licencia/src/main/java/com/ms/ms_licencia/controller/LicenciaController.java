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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ms.ms_licencia.dto.ActualizarEstadoLicenciaDTO;
import com.ms.ms_licencia.dto.ApiResponseDTO;
import com.ms.ms_licencia.dto.LicenciaRequestDTO;
import com.ms.ms_licencia.dto.LicenciaResponseDTO;
import com.ms.ms_licencia.service.LicenciaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/licencias")
public class LicenciaController {

    private final LicenciaService licenciaService;

    public LicenciaController(LicenciaService licenciaService) {
        this.licenciaService = licenciaService;
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
            @Valid @RequestBody LicenciaRequestDTO dto) {
        LicenciaResponseDTO emitida = licenciaService.emitir(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.ok("Licencia emitida exitosamente", emitida));
    }

    
    @PatchMapping("/{id}/estado")
    public ResponseEntity<ApiResponseDTO<LicenciaResponseDTO>> actualizarEstado(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarEstadoLicenciaDTO dto) {
        return ResponseEntity.ok(ApiResponseDTO.ok("Estado actualizado",
                licenciaService.actualizarEstado(id, dto)));
    }

    
    @PatchMapping("/{id}/anular")
    public ResponseEntity<ApiResponseDTO<Void>> anular(@PathVariable Long id) {
        licenciaService.anular(id);
        return ResponseEntity.ok(ApiResponseDTO.ok("Licencia anulada exitosamente", null));
    }

    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> eliminar(@PathVariable Long id) {
        licenciaService.eliminar(id);
        return ResponseEntity.ok(ApiResponseDTO.ok("Licencia eliminada", null));
    }

}
