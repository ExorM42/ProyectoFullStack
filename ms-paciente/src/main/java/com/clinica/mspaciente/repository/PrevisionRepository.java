package com.clinica.mspaciente.repository;

import com.clinica.mspaciente.model.Prevision;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio JPA para operaciones CRUD de Prevision.
 */
public interface PrevisionRepository extends JpaRepository<Prevision, Long> {

    // Buscar previsiones activas
    List<Prevision> findByActivoTrue();

    // Buscar por tipo

    List<Prevision> findByTipo(String tipo);


}
