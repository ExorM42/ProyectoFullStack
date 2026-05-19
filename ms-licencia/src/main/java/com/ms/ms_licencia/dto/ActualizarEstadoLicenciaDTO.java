package com.ms.ms_licencia.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class ActualizarEstadoLicenciaDTO {

    @NotBlank(message = "El estado debe ser obligatorio")
    @Pattern(
            regexp = "EMITIDA|APROBADA|RECHAZADA|ANULADA",
            message = "Estado invalido. Solo se permite lo siguiente: EMITIDA, APROBADA, RECHAZADA, ANULADA"
    )
    private String estado;

    private String observaciones;

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
