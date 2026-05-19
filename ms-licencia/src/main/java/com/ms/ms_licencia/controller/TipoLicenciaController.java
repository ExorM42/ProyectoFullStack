package com.ms.ms_licencia.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ms.ms_licencia.dto.ApiResponseDTO;
import com.ms.ms_licencia.dto.TipoLicenciaResponseDTO;
import com.ms.ms_licencia.service.TipoLicenciaService;

import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/api/tipos-licencia")
@Validated
public class TipoLicenciaController {

    private final TipoLicenciaService tipoLicenciaService;

    public TipoLicenciaController(TipoLicenciaService tipoLicenciaService) {
        this.tipoLicenciaService = tipoLicenciaService;
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<TipoLicenciaResponseDTO>>> listar() {
        return ResponseEntity.ok(ApiResponseDTO.ok("Tipos de licencia obtenidos",
                tipoLicenciaService.obtenerTodos()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<TipoLicenciaResponseDTO>> obtenerPorId(
            @PathVariable @Positive(message = "El ID debe ser mayor que cero") Long id) {
        return ResponseEntity.ok(ApiResponseDTO.ok("Tipo de licencia encontrado",
                tipoLicenciaService.obtenerPorId(id)));
    }

}
