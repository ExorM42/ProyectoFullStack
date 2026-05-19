package com.ms.ms_licencia.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ms.ms_licencia.model.LicenciaMedica;

public interface LicenciaMedicaRepository extends JpaRepository<LicenciaMedica, Long> {

    Optional<LicenciaMedica> findByFolio(String folio);
    boolean existsByFolio(String folio);

    List<LicenciaMedica> findByPacienteId(Long pacienteId);
    List<LicenciaMedica> findByDoctorId(Long doctorId);
    List<LicenciaMedica> findByEstado(String estado);
    List<LicenciaMedica> findByPacienteIdAndEstado(Long pacienteId, String estado);

    @Query("SELECT COUNT(lic) > 0 FROM LicenciaMedica lic WHERE lic.pacienteId = :pacienteId " +
           "AND lic.estado NOT IN ('ANULADA', 'RECHAZADA') " +
           "AND lic.fechaInicio <= :fechaFin AND lic.fechaFin >= :fechaInicio")
    boolean existeLicenciaVigenteEnPeriodo(@Param("pacienteId") Long pacienteId,
                                           @Param("fechaInicio") LocalDate fechaInicio,
                                           @Param("fechaFin") LocalDate fechaFin);

    @Query("SELECT COALESCE(SUM(lic.diasReposo), 0) FROM LicenciaMedica lic " +
           "WHERE lic.pacienteId = :pacienteId " +
           "AND lic.estado NOT IN ('ANULADA', 'RECHAZADA') " +
           "AND EXTRACT(YEAR FROM lic.fechaInicio) = :anio")
    Integer totalDiasReposoAnio(@Param("pacienteId") Long pacienteId,
                                @Param("anio") int anio);
}
