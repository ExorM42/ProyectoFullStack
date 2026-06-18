package com.clinica.mspaciente.service;

import com.clinica.mspaciente.dto.PrevisionResponseDTO;
import com.clinica.mspaciente.exception.ResourceNotFoundException;
import com.clinica.mspaciente.model.Prevision;
import com.clinica.mspaciente.repository.PrevisionRepository;
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
class PrevisionServiceTest {

    @Mock
    private PrevisionRepository previsionRepository;

    @InjectMocks
    private PrevisionService previsionService;

    @Test
    void listarActivasRetornaPrevisiones() {
        when(previsionRepository.findByActivoTrue()).thenReturn(List.of(previsionActiva()));

        List<PrevisionResponseDTO> response = previsionService.listarActivas();

        assertEquals(1, response.size());
        assertEquals("FONASA Tramo B", response.get(0).getNombre());
    }

    @Test
    void obtenerPorIdRetornaPrevision() {
        when(previsionRepository.findById(1L)).thenReturn(Optional.of(previsionActiva()));

        PrevisionResponseDTO response = previsionService.obtenerPorId(1L);

        assertEquals(1L, response.getId());
        assertEquals("FONASA", response.getTipo());
    }

    @Test
    void obtenerPorIdNoEncontradoLanzaResourceNotFoundException() {
        when(previsionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> previsionService.obtenerPorId(99L));
    }

    private Prevision previsionActiva() {
        Prevision prevision = new Prevision();
        prevision.setId(1L);
        prevision.setNombre("FONASA Tramo B");
        prevision.setTipo("FONASA");
        prevision.setDescripcion("Fonasa tramo B");
        prevision.setActivo(true);
        return prevision;
    }
}
