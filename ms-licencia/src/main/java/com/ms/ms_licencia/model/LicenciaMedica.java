package com.ms.ms_licencia.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "licencia_medica")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LicenciaMedica {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator= "seq_licencia_gen")
    @SequenceGenerator(name = "seq_licencia_gen", sequenceName= "seq_licencia", allocationSize=1)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String folio;

    @Column(name = "paciente_id", nullable= false)
    private Long pacienteId;

    @Column(name= "doctor_id", nullable=false)
    private Long doctorId;

    @Column(name= "cita_id")
    private Long citaId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tipo_licencia_id", nullable = false)
    private TipoLicencia tipoLicencia;

    @Column(nullable= false, length= 500)
    private String diagnostico;

    @Column(name= "fecha_inicio", nullable= false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    @Column(name= "fecha_reposo", nullable= false)
    private Integer diasReposo;

    @Column(nullable= false, length = 20)
    private String estado = "EMITIDA";

    @Column(length = 1000)
    private String observaciones;

    @Column(name = "fecha_emision")
    private LocalDateTime fechaEmision;

    @PrePersist
    public void prePersist(){
        this.fechaEmision = LocalDateTime.now();
    }



}
