package com.ms.ms_licencia.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
public class DoctorDTO {

    private Long id;
    private String rut;
    private String nombre;
    private String apellido;
    private String email;
    private Boolean activo;
    private Long especialidad;
    private String especialidadNombre;

}
