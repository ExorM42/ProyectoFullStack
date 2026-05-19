package com.ms.ms_cita.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ActualizarEstadoCitaDTO {

    @NotBlank(message = "El estado es obligatorio")
    @Pattern(
        regexp = "PENDIENTE|CONFIRMADA|CANCELADA|COMPLETADA|NO_ASISTIO",
        message = "Estado invalido. Valores permitidos : PENDIENTE, CONFIRMADA, CANCELADA, COMPLETADA, NO_ASISTIO"
    )
    private String estado;

    private String observaciones;

}
