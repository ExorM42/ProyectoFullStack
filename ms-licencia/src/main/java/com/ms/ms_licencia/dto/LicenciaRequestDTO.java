package com.ms.ms_licencia.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LicenciaRequestDTO {

    @NotNull(message = "El ID de paciente debe ser obligatorio")
    @Positive(message = "El ID de paciente debe ser positivo")
    private Long pacienteId;

    private Long citaId;

    @NotNull(message = "El ID de tipo de licencia debe ser obligatorio")
    @Positive(message = "El ID de tipo de licencia debe ser positivo")
    private Long tipoLicenciaId;

    @NotBlank(message = "El diagnostico debe ser obligatorio")
    @Size(min = 10, max = 500, message = "El diagnostico debe tener entre 10 y 500 caracteres")
    private String diagnostico;

    @NotNull(message = "La fecha de inicio debe ser obligatoria")
    private LocalDate fechaInicio;

    @NotNull(message = "La fecha de termino debe ser obligatoria")
    private LocalDate fechaFin;

    @Size(max = 1000, message = "La observacion no debe superar los 1000 caracteres")
    private String observaciones;
}