package com.clinica.msdoctor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Respuesta con datos de una especialidad medica.")
public class EspecialidadResponseDTO {

    @Schema(description = "Identificador de la especialidad.", example = "1")
    private Long id;

    @Schema(description = "Nombre de la especialidad.", example = "Medicina General")
    private String nombre;

    @Schema(description = "Descripcion de la especialidad.", example = "Atencion medica general")
    private String descripcion;

    @Schema(description = "Indica si la especialidad esta activa.", example = "true")
    private Boolean activo;
}
