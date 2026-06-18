package com.clinica.msdoctor.controller;

import com.clinica.msdoctor.dto.ApiResponseDTO;
import com.clinica.msdoctor.dto.DoctorRequestDTO;
import com.clinica.msdoctor.dto.DoctorResponseDTO;
import com.clinica.msdoctor.service.DoctorService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/doctores")
@Tag(name = "Doctores", description = "Operaciones para gestionar doctores.")
public class DoctorController {

    private static final Logger log = LoggerFactory.getLogger(DoctorController.class);

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @Operation(summary = "Listar doctores activos", description = "Obtiene todos los doctores activos registrados.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Doctores obtenidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<DoctorResponseDTO>>> obtenerTodos() {
        log.info("[CONTROLLER] GET /api/doctores");
        return ResponseEntity.ok(ApiResponseDTO.ok("Doctores obtenidos", doctorService.obtenerTodos()));
    }

    @Operation(summary = "Obtener doctor por ID", description = "Busca un doctor por su identificador.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Doctor encontrado"),
            @ApiResponse(responseCode = "400", description = "ID invalido"),
            @ApiResponse(responseCode = "404", description = "Doctor no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<DoctorResponseDTO>> obtenerPorId(
            @Parameter(description = "ID del doctor", example = "1", schema = @Schema(minimum = "1"))
            @PathVariable @Positive Long id) {
        log.info("[CONTROLLER] GET /api/doctores/{}", id);
        return ResponseEntity.ok(ApiResponseDTO.ok("Doctor encontrado", doctorService.obtenerPorId(id)));
    }

    @Operation(summary = "Obtener doctor por RUT", description = "Busca un doctor por su RUT.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Doctor encontrado"),
            @ApiResponse(responseCode = "404", description = "Doctor no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/rut/{rut}")
    public ResponseEntity<ApiResponseDTO<DoctorResponseDTO>> obtenerPorRut(
            @Parameter(description = "RUT del doctor", example = "11222333-4")
            @PathVariable String rut) {
        log.info("[CONTROLLER] GET /api/doctores/rut/{}", rut);
        return ResponseEntity.ok(ApiResponseDTO.ok("Doctor encontrado", doctorService.obtenerPorRut(rut)));
    }

    @Operation(summary = "Obtener doctores por especialidad", description = "Lista doctores asociados a una especialidad activa.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Doctores por especialidad obtenidos"),
            @ApiResponse(responseCode = "400", description = "ID de especialidad invalido"),
            @ApiResponse(responseCode = "404", description = "Especialidad no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/especialidad/{especialidadId}")
    public ResponseEntity<ApiResponseDTO<List<DoctorResponseDTO>>> obtenerPorEspecialidad(
            @Parameter(description = "ID de la especialidad", example = "1", schema = @Schema(minimum = "1"))
            @PathVariable @Positive Long especialidadId) {
        log.info("[CONTROLLER] GET /api/doctores/especialidad/{}", especialidadId);
        return ResponseEntity.ok(ApiResponseDTO.ok("Doctores por especialidad",
                doctorService.obtenerPorEspecialidad(especialidadId)));
    }

    @Operation(summary = "Crear doctor", description = "Crea un doctor con validaciones de negocio.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Doctor creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos o regla de negocio incumplida"),
            @ApiResponse(responseCode = "404", description = "Especialidad no encontrada"),
            @ApiResponse(responseCode = "409", description = "RUT, email o registro medico duplicado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<ApiResponseDTO<DoctorResponseDTO>> crear(@Valid @RequestBody DoctorRequestDTO dto) {
        log.info("[CONTROLLER] POST /api/doctores");
        DoctorResponseDTO creado = doctorService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.ok("Doctor creado exitosamente", creado));
    }

    @Operation(summary = "Actualizar doctor", description = "Actualiza todos los datos modificables de un doctor.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Doctor actualizado"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos o regla de negocio incumplida"),
            @ApiResponse(responseCode = "404", description = "Doctor o especialidad no encontrada"),
            @ApiResponse(responseCode = "409", description = "RUT, email o registro medico duplicado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<DoctorResponseDTO>> actualizar(
            @Parameter(description = "ID del doctor", example = "1", schema = @Schema(minimum = "1"))
            @PathVariable @Positive Long id,
            @Valid @RequestBody DoctorRequestDTO dto) {
        log.info("[CONTROLLER] PUT /api/doctores/{}", id);
        return ResponseEntity.ok(ApiResponseDTO.ok("Doctor actualizado", doctorService.actualizar(id, dto)));
    }

    @Operation(summary = "Desactivar doctor", description = "Realiza baja logica del doctor.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Doctor desactivado"),
            @ApiResponse(responseCode = "400", description = "ID invalido"),
            @ApiResponse(responseCode = "404", description = "Doctor no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<ApiResponseDTO<Void>> desactivar(
            @Parameter(description = "ID del doctor", example = "1", schema = @Schema(minimum = "1"))
            @PathVariable @Positive Long id) {
        log.info("[CONTROLLER] PATCH /api/doctores/{}/desactivar", id);
        doctorService.desactivar(id);
        return ResponseEntity.ok(ApiResponseDTO.ok("Doctor desactivado", null));
    }

    @Operation(summary = "Eliminar doctor", description = "Elimina físicamente a un doctor.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Doctor eliminado exitosamente"),
            @ApiResponse(responseCode = "400", description = "ID invalido"),
            @ApiResponse(responseCode = "404", description = "Doctor no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable @Positive Long id) {
        log.info("[CONTROLLER] DELETE /api/doctores/{}", id);
        doctorService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
