package com.clinica.msdoctor.service;

import com.clinica.msdoctor.dto.EspecialidadResponseDTO;
import com.clinica.msdoctor.exception.ResourceNotFoundException;
import com.clinica.msdoctor.model.Especialidad;
import com.clinica.msdoctor.repository.EspecialidadRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EspecialidadServiceTest {

    @Mock
    private EspecialidadRepository especialidadRepository;

    @InjectMocks
    private EspecialidadService especialidadService;

    @Test
    void listarActivasRetornaEspecialidades() {
        when(especialidadRepository.findByActivoTrue()).thenReturn(List.of(especialidadActiva()));

        List<EspecialidadResponseDTO> response = especialidadService.listarActivas();

        assertEquals(1, response.size());
        assertEquals("Medicina General", response.get(0).getNombre());
    }

    @Test
    void obtenerPorIdRetornaEspecialidad() {
        when(especialidadRepository.findById(1L)).thenReturn(Optional.of(especialidadActiva()));

        EspecialidadResponseDTO response = especialidadService.obtenerPorId(1L);

        assertEquals(1L, response.getId());
        assertEquals("Medicina General", response.getNombre());
    }

    @Test
    void obtenerPorIdNoEncontradaLanzaResourceNotFoundException() {
        when(especialidadRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> especialidadService.obtenerPorId(99L));
    }

    private Especialidad especialidadActiva() {
        Especialidad especialidad = new Especialidad();
        especialidad.setId(1L);
        especialidad.setNombre("Medicina General");
        especialidad.setDescripcion("Atencion medica general");
        especialidad.setActivo(true);
        return especialidad;
    }
}
