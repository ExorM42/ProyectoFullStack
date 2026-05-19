package com.ms.ms_cita.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cita")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_cita_gen" )
    @SequenceGenerator(name = "seq_cita_gen", sequenceName = "seq_cita", allocationSize=1)
    private Long id;

    @Column(name = "paciente_id", nullable = false)
    private Long pacienteId;

    @Column(name = "doctor_id", nullable = false)
    private Long doctorId;

    @Column(name = "fecha_hora_cita", nullable = false)
    private LocalDateTime fechaHoraCita;

    @Column(name = "motivo_consulta", nullable = false, length = 500)
    private String motivoConsulta;

    @Column(nullable = false, length = 20)
    private String estado = "PENDIENTE";

    @Column(length = 1000)
    private String observaciones;

    @Column(name = "duracion_minutos")
    private Integer duracionMinutos = 30;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @PrePersist
    public void prePersist(){
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate(){
        this.fechaActualizacion = LocalDateTime.now();
    }


    
}
