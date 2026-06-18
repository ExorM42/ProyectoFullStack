package com.clinica.mspaciente.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa la previsión de salud de un paciente.
 * Ejemplos: FONASA, ISAPRE, Particular.
 */
@Entity
@Table(name = "prevision")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Prevision {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_prevision_gen")
    @SequenceGenerator(name = "seq_prevision_gen", sequenceName = "seq_prevision", allocationSize = 1)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    // Tipo: FONASA, ISAPRE, PARTICULAR
    @Column(nullable = false, length = 50)
    private String tipo;

    @Column(length = 255)
    private String descripcion;

    @Column(nullable = false)
    private Boolean activo = true;

    // Relación inversa: un tipo de previsión puede tener muchos pacientes
    @OneToMany(mappedBy = "prevision", fetch = FetchType.LAZY)
    private java.util.List<Paciente> pacientes;
}
