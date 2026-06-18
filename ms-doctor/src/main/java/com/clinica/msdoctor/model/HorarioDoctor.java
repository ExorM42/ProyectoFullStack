package com.clinica.msdoctor.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

/**
 * Entidad que representa el horario de atención de un doctor.
 */
@Entity
@Table(name = "horario_doctor")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HorarioDoctor {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_horario_gen")
    @SequenceGenerator(name = "seq_horario_gen", sequenceName = "seq_horario", allocationSize = 1)
    private Long id;

    // Relación ManyToOne con Doctor
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    // Día de la semana: LUNES, MARTES, MIERCOLES, JUEVES, VIERNES, SABADO
    @Column(name = "dia_semana", nullable = false, length = 15)
    private String diaSemana;

    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;

    @Column(name = "hora_fin", nullable = false)
    private LocalTime horaFin;
}
