package com.clinica.mspaciente.controller;

import com.clinica.mspaciente.dto.ApiResponseDTO;
import com.clinica.mspaciente.dto.PrevisionResponseDTO;
import com.clinica.mspaciente.service.PrevisionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/previsiones")
@Tag(name = "Previsiones", description = "Catalogo de previsiones de salud.")
public class PrevisionController {

    private static final Logger log = LoggerFactory.getLogger(PrevisionController.class);

    private final PrevisionService previsionService;

    public PrevisionController(PrevisionService previsionService) {
        this.previsionService = previsionService;
    }

    @Operation(summary = "Listar previsiones activas", description = "Obtiene todas las previsiones activas.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Previsiones obtenidas"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<PrevisionResponseDTO>>> listar() {
        log.info("[CONTROLLER] GET /api/previsiones");
        return ResponseEntity.ok(ApiResponseDTO.ok("Previsiones obtenidas", previsionService.listarActivas()));
    }

    @Operation(summary = "Obtener prevision por ID", description = "Busca una prevision por su identificador.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Prevision encontrada"),
            @ApiResponse(responseCode = "400", description = "ID invalido"),
            @ApiResponse(responseCode = "404", description = "Prevision no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<PrevisionResponseDTO>> obtenerPorId(
            @Parameter(description = "ID de la prevision", example = "1", schema = @Schema(minimum = "1"))
            @PathVariable @Positive Long id) {
        log.info("[CONTROLLER] GET /api/previsiones/{}", id);
        PrevisionResponseDTO prevision = previsionService.obtenerPorId(id);
        return ResponseEntity.ok(ApiResponseDTO.ok("Prevision encontrada", prevision));
    }
}
