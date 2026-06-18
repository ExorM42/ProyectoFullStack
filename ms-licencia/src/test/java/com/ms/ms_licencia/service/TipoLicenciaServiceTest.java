package com.ms.ms_licencia.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ms.ms_licencia.dto.TipoLicenciaResponseDTO;
import com.ms.ms_licencia.exception.ResourceNotFoundException;
import com.ms.ms_licencia.model.TipoLicencia;
import com.ms.ms_licencia.repository.TipoLicenciaRepository;

@ExtendWith(MockitoExtension.class)
public class TipoLicenciaServiceTest {

    @Mock
    private TipoLicenciaRepository tipoLicenciaRepo;

    @InjectMocks
    private TipoLicenciaService tipoLicenciaService;

    @Test
    void givenTiposLicencia_whenFindAll_thenReturnTiposLicencia() {

        // GIVEN
        TipoLicencia tipo1 = new TipoLicencia();
        tipo1.setId(1L);

        TipoLicencia tipo2 = new TipoLicencia();
        tipo2.setId(2L);

        // WHEN
        when(tipoLicenciaRepo.findAll()).thenReturn(List.of(tipo1, tipo2));
        List<TipoLicenciaResponseDTO> res = tipoLicenciaService.obtenerTodos();

        // THEN
        assertNotNull(res);
        assertEquals(2, res.size());
        assertEquals(tipo1.getId(), res.get(0).getId());
        assertEquals(tipo2.getId(), res.get(1).getId());

        verify(tipoLicenciaRepo, times(1)).findAll();
    }

    @Test
    void givenIdTipoLicencia_whenFindById_thenReturnTipoLicencia() {

        // GIVEN
        Long tipoId = 1L;
        TipoLicencia tipo = new TipoLicencia();
        tipo.setId(tipoId);
        tipo.setNombre("Licencia medica");

        // WHEN
        when(tipoLicenciaRepo.findById(tipoId)).thenReturn(Optional.of(tipo));
        TipoLicenciaResponseDTO res = tipoLicenciaService.obtenerPorId(tipoId);

        // THEN
        assertNotNull(res);
        assertEquals(tipo.getId(), res.getId());
        assertEquals(tipo.getNombre(), res.getNombre());

        verify(tipoLicenciaRepo, times(1)).findById(tipoId);
    }

    @Test
    void givenIdTipoLicencia_whenFindByIdNotFound_thenThrowResourceNotFoundException() {

        // GIVEN
        Long tipoId = 99L;

        // WHEN
        when(tipoLicenciaRepo.findById(tipoId)).thenReturn(Optional.empty());

        // THEN
        assertThrows(ResourceNotFoundException.class, () -> {
            tipoLicenciaService.obtenerPorId(tipoId);
        });

        verify(tipoLicenciaRepo, times(1)).findById(tipoId);
    }
}