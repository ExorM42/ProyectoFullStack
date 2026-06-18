package com.clinica.msdoctor.repository;

import com.clinica.msdoctor.model.HorarioDoctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HorarioDoctorRepository extends JpaRepository<HorarioDoctor, Long> {
    List<HorarioDoctor> findByDoctorId(Long doctorId);
    List<HorarioDoctor> findByDoctorIdAndDiaSemana(Long doctorId, String diaSemana);
}
