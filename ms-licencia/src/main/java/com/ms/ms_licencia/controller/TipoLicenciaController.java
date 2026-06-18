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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;

@Tag(name = "Tipos de licencia", description = "Operaciones relacionadas con los tipos de licencia medica")
@RestController
@RequestMapping("/api/tipos-licencia")
@Validated
public class TipoLicenciaController {

    private final TipoLicenciaService tipoLicenciaService;

    public TipoLicenciaController(TipoLicenciaService tipoLicenciaService) {
        this.tipoLicenciaService = tipoLicenciaService;
    }

    @Operation(summary = "Obtener todos los tipos de licencia", description = "Devuelve una lista de tipos de licencia registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tipos de licencia obtenidos correctamente")
    })
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<TipoLicenciaResponseDTO>>> listar() {
        return ResponseEntity.ok(ApiResponseDTO.ok("Tipos de licencia obtenidos", tipoLicenciaService.obtenerTodos()));
    }

    @Operation(summary = "Obtener tipo de licencia por ID", description = "Devuelve el detalle de un tipo de licencia especifico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tipo de licencia encontrado"),
            @ApiResponse(responseCode = "404", description = "Tipo de licencia no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<TipoLicenciaResponseDTO>> obtenerPorId(
            @Parameter(description = "ID del tipo de licencia", example = "1") @PathVariable @Positive(message = "El ID debe ser mayor que cero") Long id) {
        return ResponseEntity.ok(ApiResponseDTO.ok("Tipo de licencia encontrado", tipoLicenciaService.obtenerPorId(id)));
    }
}