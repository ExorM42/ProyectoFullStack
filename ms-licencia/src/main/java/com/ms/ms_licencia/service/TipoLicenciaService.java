package com.ms.ms_licencia.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ms.ms_licencia.dto.TipoLicenciaResponseDTO;
import com.ms.ms_licencia.exception.ResourceNotFoundException;
import com.ms.ms_licencia.mapper.LicenciaMapper;
import com.ms.ms_licencia.model.TipoLicencia;
import com.ms.ms_licencia.repository.TipoLicenciaRepository;

@Service
public class TipoLicenciaService {


    private final TipoLicenciaRepository tipoLicenciaRepository;

    public TipoLicenciaService(TipoLicenciaRepository tipoLicenciaRepository) {
        this.tipoLicenciaRepository = tipoLicenciaRepository;
    }

    public List<TipoLicenciaResponseDTO> obtenerTodos(){
        return tipoLicenciaRepository.findAll().stream().map(LicenciaMapper::toDTO).collect(Collectors.toList());
    }

    public TipoLicenciaResponseDTO obtenerPorId(Long id){
        TipoLicencia tipoLicencia = tipoLicenciaRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("TipoLicencia", id));
        return LicenciaMapper.toDTO(tipoLicencia);
    }
    



}
