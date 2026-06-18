package com.clinica.msdoctor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "Respuesta con datos publicos de un doctor.")
public class DoctorResponseDTO {

    @Schema(description = "Identificador unico del doctor.", example = "1")
    private Long id;

    @Schema(description = "RUT del doctor.", example = "11222333-4")
    private String rut;

    @Schema(description = "Nombre del doctor.", example = "Ana")
    private String nombre;

    @Schema(description = "Apellido del doctor.", example = "Gomez")
    private String apellido;

    @Schema(description = "Email del doctor.", example = "ana.gomez@clinica.local")
    private String email;

    @Schema(description = "Telefono del doctor.", example = "+56987654321")
    private String telefono;

    @Schema(description = "Numero unico de registro medico.", example = "REG-MED-1001")
    private String numeroRegistro;

    @Schema(description = "Indica si el doctor esta activo.", example = "true")
    private Boolean activo;

    @Schema(description = "Fecha de contratacion.", example = "2022-03-01")
    private LocalDate fechaContratacion;

    @Schema(description = "ID de la especialidad asociada.", example = "1")
    private Long especialidadId;

    @Schema(description = "Nombre de la especialidad asociada.", example = "Medicina General")
    private String especialidadNombre;
}
