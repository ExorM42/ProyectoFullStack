package com.clinica.mspaciente.controller;

import com.clinica.mspaciente.dto.ApiResponseDTO;
import com.clinica.mspaciente.dto.PacienteRequestDTO;
import com.clinica.mspaciente.dto.PacienteResponseDTO;
import com.clinica.mspaciente.service.PacienteService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/pacientes")
@Tag(name = "Pacientes", description = "Operaciones para gestionar pacientes.")
public class PacienteController {

    private static final Logger log = LoggerFactory.getLogger(PacienteController.class);

    private final PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @Operation(summary = "Listar pacientes activos", description = "Obtiene todos los pacientes activos registrados.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pacientes obtenidos correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<PacienteResponseDTO>>> obtenerTodos() {
        log.info("[CONTROLLER] GET /api/pacientes");
        List<PacienteResponseDTO> pacientes = pacienteService.obtenerTodos();
        return ResponseEntity.ok(ApiResponseDTO.ok("Pacientes obtenidos exitosamente", pacientes));
    }

    @Operation(summary = "Obtener paciente por ID", description = "Busca un paciente por su identificador.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Paciente encontrado"),
            @ApiResponse(responseCode = "400", description = "ID invalido"),
            @ApiResponse(responseCode = "404", description = "Paciente no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<PacienteResponseDTO>> obtenerPorId(
            @Parameter(description = "ID del paciente", example = "1", schema = @Schema(minimum = "1"))
            @PathVariable @Positive Long id) {
        log.info("[CONTROLLER] GET /api/pacientes/{}", id);
        PacienteResponseDTO paciente = pacienteService.obtenerPorId(id);
        return ResponseEntity.ok(ApiResponseDTO.ok("Paciente encontrado", paciente));
    }

    @Operation(summary = "Obtener pacientes por edad", description = "Busca pacientes activos por edad exacta.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pacientes encontrados"),
            @ApiResponse(responseCode = "400", description = "Edad invalida"),
            @ApiResponse(responseCode = "404", description = "No existen pacientes con la edad solicitada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/edad/{edad}")
    public ResponseEntity<ApiResponseDTO<List<PacienteResponseDTO>>> obtenerPorEdad(
            @Parameter(description = "Edad exacta del paciente", example = "36", schema = @Schema(minimum = "0", maximum = "120"))
            @PathVariable @Min(0) @Max(120) Integer edad) {
        log.info("[CONTROLLER] GET /api/pacientes/edad/{}", edad);
        List<PacienteResponseDTO> pacientes = pacienteService.obtenerPorEdad(edad);
        return ResponseEntity.ok(ApiResponseDTO.ok("Pacientes encontrados por edad", pacientes));
    }

    @Operation(summary = "Obtener paciente por RUT", description = "Busca un paciente por su RUT.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Paciente encontrado"),
            @ApiResponse(responseCode = "404", description = "Paciente no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/rut/{rut}")
    public ResponseEntity<ApiResponseDTO<PacienteResponseDTO>> obtenerPorRut(
            @Parameter(description = "RUT del paciente", example = "12345678-9")
            @PathVariable String rut) {
        log.info("[CONTROLLER] GET /api/pacientes/rut/{}", rut);
        PacienteResponseDTO paciente = pacienteService.obtenerPorRut(rut);
        return ResponseEntity.ok(ApiResponseDTO.ok("Paciente encontrado", paciente));
    }

    @Operation(summary = "Buscar pacientes por apellido", description = "Busca pacientes cuyo apellido contenga el texto indicado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Busqueda completada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/buscar")
    public ResponseEntity<ApiResponseDTO<List<PacienteResponseDTO>>> buscarPorApellido(
            @Parameter(description = "Texto del apellido a buscar", example = "Perez")
            @RequestParam String apellido) {
        log.info("[CONTROLLER] GET /api/pacientes/buscar?apellido={}", apellido);
        List<PacienteResponseDTO> pacientes = pacienteService.buscarPorApellido(apellido);
        return ResponseEntity.ok(ApiResponseDTO.ok("Busqueda completada", pacientes));
    }

    @Operation(summary = "Crear paciente", description = "Crea un paciente con validaciones de negocio.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Paciente creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos o regla de negocio incumplida"),
            @ApiResponse(responseCode = "404", description = "Prevision no encontrada"),
            @ApiResponse(responseCode = "409", description = "RUT o email duplicado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<ApiResponseDTO<PacienteResponseDTO>> crear(
            @Valid @RequestBody PacienteRequestDTO dto) {
        log.info("[CONTROLLER] POST /api/pacientes");
        PacienteResponseDTO creado = pacienteService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.ok("Paciente creado exitosamente", creado));
    }

    @Operation(summary = "Actualizar paciente", description = "Actualiza todos los datos modificables de un paciente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Paciente actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos o regla de negocio incumplida"),
            @ApiResponse(responseCode = "404", description = "Paciente o prevision no encontrada"),
            @ApiResponse(responseCode = "409", description = "RUT o email duplicado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<PacienteResponseDTO>> actualizar(
            @Parameter(description = "ID del paciente", example = "1", schema = @Schema(minimum = "1"))
            @PathVariable @Positive Long id,
            @Valid @RequestBody PacienteRequestDTO dto) {
        log.info("[CONTROLLER] PUT /api/pacientes/{}", id);
        PacienteResponseDTO actualizado = pacienteService.actualizar(id, dto);
        return ResponseEntity.ok(ApiResponseDTO.ok("Paciente actualizado exitosamente", actualizado));
    }

    @Operation(summary = "Desactivar paciente", description = "Realiza baja logica del paciente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Paciente desactivado exitosamente"),
            @ApiResponse(responseCode = "400", description = "ID invalido"),
            @ApiResponse(responseCode = "404", description = "Paciente no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<ApiResponseDTO<Void>> desactivar(
            @Parameter(description = "ID del paciente", example = "1", schema = @Schema(minimum = "1"))
            @PathVariable @Positive Long id) {
        log.info("[CONTROLLER] PATCH /api/pacientes/{}/desactivar", id);
        pacienteService.desactivar(id);
        return ResponseEntity.ok(ApiResponseDTO.ok("Paciente desactivado exitosamente", null));
    }

    @Operation(summary = "Eliminar paciente", description = "Elimina físicamente a un paciente.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Paciente eliminado exitosamente"),
            @ApiResponse(responseCode = "400", description = "ID invalido"),
            @ApiResponse(responseCode = "404", description = "Paciente no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable @Positive Long id) {
        log.info("[CONTROLLER] DELETE /api/pacientes/{}", id);
        pacienteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
