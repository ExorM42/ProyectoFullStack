package com.ms.ms_licencia.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class LicenciaResponseDTO {

    private Long id;
    private String folio;


    private Long pacienteId;
    private String pacienteNombre;
    private String pacienteApellido;
    private String pacienteRut;


    private Long doctorId;
    private String doctorNombre;
    private String doctorApellido;
    private String doctorEspecialidad;

    private Long citaId;


    private Long tipoLicenciaId;
    private String tipoLicenciaCodigo;
    private String tipoLicenciaNombre;

    private String diagnostico;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Integer diasReposo;
    private String estado;
    private String observaciones;
    private LocalDateTime fechaEmision;

}
