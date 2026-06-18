package com.clinica.msdoctor.repository;

import com.clinica.msdoctor.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para operaciones CRUD de Doctor.
 */
@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    Optional<Doctor> findByRut(String rut);
    boolean existsByRut(String rut);
    boolean existsByEmail(String email);
    boolean existsByNumeroRegistro(String numeroRegistro);
    List<Doctor> findByActivoTrue();
    List<Doctor> findByEspecialidadId(Long especialidadId);
    List<Doctor> findByApellidoContainingIgnoreCase(String apellido);
}
