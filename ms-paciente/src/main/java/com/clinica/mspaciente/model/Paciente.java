package com.clinica.mspaciente.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidad principal que representa a un paciente del sistema clínico.
 */
@Entity
@Table(name = "paciente")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_paciente_gen")
    @SequenceGenerator(name = "seq_paciente_gen", sequenceName = "seq_paciente", allocationSize = 1)
    private Long id;

    // RUT único del paciente
    @Column(nullable = false, unique = true, length = 12)
    private String rut;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String apellido;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    // Género: MASCULINO, FEMENINO, OTRO
    @Column(nullable = false, length = 20)
    private String genero;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false, length = 20)
    private String telefono;

    @Column(length = 255)
    private String direccion;

    // Relación ManyToOne con Prevision
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "prevision_id")
    private Prevision prevision;

    @Column(nullable = false)
    private Boolean activo = true;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro = LocalDateTime.now();

    // Ciclo de vida: asignar fecha de registro automáticamente
    @PrePersist
    public void prePersist() {
        this.fechaRegistro = LocalDateTime.now();
    }
    @Column(nullable = false)
    private Integer edad;

    @Column(name = "ACOMPANADO", nullable = false)
    private Boolean acompanado;
}
