package com.clinica.msdoctor.repository;

import com.clinica.msdoctor.model.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EspecialidadRepository extends JpaRepository<Especialidad, Long> {
    List<Especialidad> findByActivoTrue();
    boolean existsByNombre(String nombre);
}
