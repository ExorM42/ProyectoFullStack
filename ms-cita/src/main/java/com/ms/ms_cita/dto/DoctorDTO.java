package com.ms.ms_cita.dto;

import lombok.Data;

@Data
public class DoctorDTO {

    private Long id;
    private String rut;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private Boolean activo;
    private Long especialidadId;
    private String especialidadNombre;

}
