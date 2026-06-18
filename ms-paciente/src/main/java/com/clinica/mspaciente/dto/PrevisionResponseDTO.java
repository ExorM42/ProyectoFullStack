package com.clinica.mspaciente.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Respuesta con datos de una prevision de salud.")
public class PrevisionResponseDTO {

    @Schema(description = "Identificador de la prevision.", example = "1")
    private Long id;

    @Schema(description = "Nombre comercial o tramo de la prevision.", example = "FONASA Tramo B")
    private String nombre;

    @Schema(description = "Tipo de prevision.", example = "FONASA")
    private String tipo;

    @Schema(description = "Descripcion de la prevision.", example = "Fonasa tramo B")
    private String descripcion;

    @Schema(description = "Indica si la prevision esta activa.", example = "true")
    private Boolean activo;
}
