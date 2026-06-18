package com.clinica.mspaciente.repository;

import com.clinica.mspaciente.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para operaciones CRUD de Paciente.
 */
public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    // Buscar por RUT
    Optional<Paciente> findByRut(String rut);

    // Verificar si existe un RUT
    boolean existsByRut(String rut);

    // Verificar si existe un email
    boolean existsByEmail(String email);

    boolean existsByEdad(Integer edad);

     boolean existsByAcompanado(Boolean acompanado);
    // Buscar pacientes activos
    List<Paciente> findByActivoTrue();

    // Buscar por apellido (ignorar mayúsculas)
    List<Paciente> findByApellidoContainingIgnoreCase(String apellido);

    // Buscar por previsión
    List<Paciente> findByPrevisionId(Long previsionId);

    // Buscar por edad
    List<Paciente> findByEdad(Integer edad);
    // Buscar por edad y activo
    List<Paciente> findByEdadAndActivoTrue(Integer edad);
     // BUSCAR POR ACOMPAÑADO
    List<Paciente> findByAcompanado(Boolean acompanado);

}
