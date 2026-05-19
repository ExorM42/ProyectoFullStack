package com.ms.ms_licencia.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CitaDTO {
    private Long id;
    private Long pacienteId;
    private Long doctorId;
    private LocalDateTime fechaHoraCita;
    private String motivoConsulta;
    private String estado;

}
