package com.ms.ms_cita.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class PacienteDTO {

    private Long id;
    private String rut;
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
    private String genero;
    private String email;
    private String telefono;
    private Boolean activo;
    private String previsionNombre;

}
