package com.ms.ms_licencia.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tipo_licencia")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoLicencia {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tipo_licencia_gen")
    @SequenceGenerator(name = "seq_tipo_licencia_gen", sequenceName= "seq_tipo_licencia", allocationSize= 1)
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    private String codigo;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 255)
    private String descripcion;

    @Column(name = "dias_maximos", nullable = false)
    private Integer diasMaximos;

    @OneToMany(mappedBy= "tipoLicencia", fetch = FetchType.LAZY)
    private List<LicenciaMedica> licencias;
}
