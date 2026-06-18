package com.clinica.msdoctor.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * Entidad principal que representa a un doctor del sistema clínico.
 */
@Entity
@Table(name = "doctor")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_doctor_gen")
    @SequenceGenerator(name = "seq_doctor_gen", sequenceName = "seq_doctor", allocationSize = 1)
    private Long id;

    @Column(nullable = false, unique = true, length = 12)
    private String rut;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String apellido;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false, length = 20)
    private String telefono;

    // Número de registro en el colegio médico de Chile
    @Column(name = "numero_registro", nullable = false, unique = true, length = 50)
    private String numeroRegistro;

    // Relación ManyToOne con Especialidad
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "especialidad_id", nullable = false)
    private Especialidad especialidad;

    @Column(nullable = false)
    private Boolean activo = true;

    @Column(name = "fecha_contratacion", nullable = false)
    private LocalDate fechaContratacion;

    // Relación OneToMany con HorarioDoctor (cascade: si se elimina el doctor, se eliminan sus horarios)
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<HorarioDoctor> horarios;
}
