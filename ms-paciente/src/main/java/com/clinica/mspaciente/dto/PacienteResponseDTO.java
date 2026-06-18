package com.clinica.mspaciente.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Schema(description = "Respuesta con datos publicos de un paciente.")
public class PacienteResponseDTO {

    @Schema(description = "Identificador unico del paciente.", example = "1")
    private Long id;

    @Schema(description = "RUT del paciente.", example = "12345678-9")
    private String rut;

    @Schema(description = "Nombre del paciente.", example = "Juan")
    private String nombre;

    @Schema(description = "Apellido del paciente.", example = "Perez")
    private String apellido;

    @Schema(description = "Fecha de nacimiento del paciente.", example = "1990-04-10")
    private LocalDate fechaNacimiento;

    @Schema(description = "Genero del paciente.", example = "MASCULINO")
    private String genero;

    @Schema(description = "Email del paciente.", example = "juan.perez@example.com")
    private String email;

    @Schema(description = "Telefono del paciente.", example = "+56912345678")
    private String telefono;

    @Schema(description = "Direccion del paciente.", example = "Av. Siempre Viva 123")
    private String direccion;

    @Schema(description = "Indica si el paciente esta activo.", example = "true")
    private Boolean activo;

    @Schema(description = "Fecha de registro del paciente.")
    private LocalDateTime fechaRegistro;

    @Schema(description = "Edad registrada del paciente.", example = "36")
    private Integer edad;

    @Schema(description = "Indica si el paciente viene acompanado.", example = "false")
    private Boolean acompanado;

    @Schema(description = "ID de la prevision asociada.", example = "1")
    private Long previsionId;

    @Schema(description = "Nombre de la prevision asociada.", example = "FONASA Tramo B")
    private String previsionNombre;

    @Schema(description = "Tipo de prevision asociada.", example = "FONASA")
    private String previsionTipo;
}
