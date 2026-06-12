package com.ms.ms_cita.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
;

@Data
public class CitaRequestDTO {


    @NotNull(message = "El ID del doctor debe ser obligatorio")
    @Positive(message = "El ID del doctor debe ser positivo")
    private Long doctorId;

    @NotNull(message = "La fecha y hora de la cita debe ser obligatoria")
    @Future(message = "La fecha de la cita no puede ser en el pasado")
    private LocalDateTime fechaHoraCita;

    @NotBlank(message = "El motivo de la consulta es obligatorio")
    @Size(min = 10, max = 500, message = "El motivo de la consulta debe tener entre 10 y 50 caracteres")
    private String motivoConsulta;

    @Min(value = 15, message = "La duracion minima de la cita debe ser de 15 minutos")
    @Max(value = 120, message = "La duracion maxima de la cita debe ser de 120 minutos")
    private Integer duracionMinutos = 30;
}
