package com.ms.ms_cita.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ms.ms_cita.model.Cita;

@Repository
public interface CitaRepository extends JpaRepository <Cita, Long>{


    List<Cita> findByPacienteId(Long pacienteId);
    List<Cita> findByDoctorId(Long doctorId);
    List<Cita> findByEstado(String estado);
    List<Cita> findByPacienteIdAndEstado(Long pacienteId, String estado);

    @Query("SELECT COUNT(c) > 0 FROM Cita c WHERE c.doctorId = :doctorId " +
           "AND c.estado NOT IN ('CANCELADA', 'NO_ASISTIO') " +
           "AND c.fechaHoraCita BETWEEN :inicio AND :fin")
    boolean existeCitaEnHorario(@Param("doctorId") Long doctorId,
                                @Param("inicio") LocalDateTime inicio,
                                @Param("fin") LocalDateTime fin);
    
    List<Cita> findByDoctorIdAndFechaHoraCitaBetween(Long doctorId,
                                                     LocalDateTime inicio,
                                                    LocalDateTime fin);
    
                                                    
     @Query("SELECT c FROM Cita c WHERE c.fechaHoraCita >= :inicioHoy AND c.fechaHoraCita < :finHoy ORDER BY c.fechaHoraCita")
     List<Cita> findCitasHoy(@Param("inicioHoy") LocalDateTime inicioHoy, @Param("finHoy") LocalDateTime finHoy);                                                      
}
