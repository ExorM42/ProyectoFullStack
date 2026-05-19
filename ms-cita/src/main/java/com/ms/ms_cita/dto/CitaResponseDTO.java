package com.ms.ms_cita.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CitaResponseDTO {

    private Long id;
    private Long pacienteId;
    private String pacienteNombre;
    private String pacienteApellido;
    private String pacienteRut;
    private Long doctorId;
    private String doctorNombre;
    private String doctorApellido;
    private String doctorEspecialidad;
    private LocalDateTime fechaHoraCita;
    private String motivoConsulta;
    private String estado;
    private String observaciones;
    private Integer duracionMinutos;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;

    

}
