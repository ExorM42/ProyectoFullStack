package com.clinica.msdoctor.controller;

import com.clinica.msdoctor.dto.ApiResponseDTO;
import com.clinica.msdoctor.dto.EspecialidadResponseDTO;
import com.clinica.msdoctor.service.EspecialidadService;
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
@RequestMapping("/api/especialidades")
@Tag(name = "Especialidades", description = "Catalogo de especialidades medicas.")
public class EspecialidadController {

    private static final Logger log = LoggerFactory.getLogger(EspecialidadController.class);

    private final EspecialidadService especialidadService;

    public EspecialidadController(EspecialidadService especialidadService) {
        this.especialidadService = especialidadService;
    }

    @Operation(summary = "Listar especialidades activas", description = "Obtiene todas las especialidades activas.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Especialidades obtenidas"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<EspecialidadResponseDTO>>> listar() {
        log.info("[CONTROLLER] GET /api/especialidades");
        return ResponseEntity.ok(ApiResponseDTO.ok("Especialidades obtenidas",
                especialidadService.listarActivas()));
    }

    @Operation(summary = "Obtener especialidad por ID", description = "Busca una especialidad por su identificador.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Especialidad encontrada"),
            @ApiResponse(responseCode = "400", description = "ID invalido"),
            @ApiResponse(responseCode = "404", description = "Especialidad no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<EspecialidadResponseDTO>> obtenerPorId(
            @Parameter(description = "ID de la especialidad", example = "1", schema = @Schema(minimum = "1"))
            @PathVariable @Positive Long id) {
        log.info("[CONTROLLER] GET /api/especialidades/{}", id);
        EspecialidadResponseDTO especialidad = especialidadService.obtenerPorId(id);
        return ResponseEntity.ok(ApiResponseDTO.ok("Especialidad encontrada", especialidad));
    }
}
