package com.ms.ms_licencia.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ms.ms_licencia.model.TipoLicencia;

@Repository
public interface TipoLicenciaRepository extends JpaRepository<TipoLicencia, Long>{
    Optional<TipoLicencia> findByCodigo(String codigo);

}
