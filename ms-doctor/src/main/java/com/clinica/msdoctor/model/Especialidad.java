package com.clinica.msdoctor.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Entidad que representa la especialidad médica de un doctor.
 */
@Entity
@Table(name = "especialidad")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Especialidad {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_especialidad_gen")
    @SequenceGenerator(name = "seq_especialidad_gen", sequenceName = "seq_especialidad", allocationSize = 1)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    @Column(length = 255)
    private String descripcion;

    @Column(nullable = false)
    private Boolean activo = true;

    // Una especialidad puede tener muchos doctores
    @OneToMany(mappedBy = "especialidad", fetch = FetchType.LAZY)
    private List<Doctor> doctores;
}
