package com.clinica.msdoctor.service;

import com.clinica.msdoctor.dto.EspecialidadResponseDTO;
import com.clinica.msdoctor.exception.ResourceNotFoundException;
import com.clinica.msdoctor.model.Especialidad;
import com.clinica.msdoctor.repository.EspecialidadRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EspecialidadService {

    private static final Logger log = LoggerFactory.getLogger(EspecialidadService.class);

    private final EspecialidadRepository especialidadRepository;

    public EspecialidadService(EspecialidadRepository especialidadRepository) {
        this.especialidadRepository = especialidadRepository;
    }

    public List<EspecialidadResponseDTO> listarActivas() {
        log.info("[ESPECIALIDAD] Consultando especialidades activas");
        List<Especialidad> especialidades = especialidadRepository.findByActivoTrue();
        log.info("[ESPECIALIDAD] Se encontraron {} especialidades activas", especialidades.size());
        return especialidades.stream().map(this::mapToDTO).toList();
    }

    public EspecialidadResponseDTO obtenerPorId(Long id) {
        log.info("[ESPECIALIDAD] Buscando especialidad con ID: {}", id);
        Especialidad especialidad = especialidadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Especialidad", id));
        return mapToDTO(especialidad);
    }

    private EspecialidadResponseDTO mapToDTO(Especialidad especialidad) {
        EspecialidadResponseDTO dto = new EspecialidadResponseDTO();
        dto.setId(especialidad.getId());
        dto.setNombre(especialidad.getNombre());
        dto.setDescripcion(especialidad.getDescripcion());
        dto.setActivo(especialidad.getActivo());
        return dto;
    }
}
