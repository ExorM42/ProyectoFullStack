package com.clinica.mspaciente.service;

import com.clinica.mspaciente.dto.PrevisionResponseDTO;
import com.clinica.mspaciente.exception.ResourceNotFoundException;
import com.clinica.mspaciente.model.Prevision;
import com.clinica.mspaciente.repository.PrevisionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrevisionService {

    private static final Logger log = LoggerFactory.getLogger(PrevisionService.class);

    private final PrevisionRepository previsionRepository;

    public PrevisionService(PrevisionRepository previsionRepository) {
        this.previsionRepository = previsionRepository;
    }

    public List<PrevisionResponseDTO> listarActivas() {
        log.info("[PREVISION] Consultando previsiones activas");
        List<Prevision> previsiones = previsionRepository.findByActivoTrue();
        log.info("[PREVISION] Se encontraron {} previsiones activas", previsiones.size());
        return previsiones.stream().map(this::mapToDTO).toList();
    }

    public PrevisionResponseDTO obtenerPorId(Long id) {
        log.info("[PREVISION] Buscando prevision con ID: {}", id);
        Prevision prevision = previsionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prevision", id));
        return mapToDTO(prevision);
    }

    private PrevisionResponseDTO mapToDTO(Prevision prevision) {
        PrevisionResponseDTO dto = new PrevisionResponseDTO();
        dto.setId(prevision.getId());
        dto.setNombre(prevision.getNombre());
        dto.setTipo(prevision.getTipo());
        dto.setDescripcion(prevision.getDescripcion());
        dto.setActivo(prevision.getActivo());
        return dto;
    }
}
