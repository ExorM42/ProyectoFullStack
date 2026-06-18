package com.clinica.mspaciente.service;

import com.clinica.mspaciente.dto.PacienteRequestDTO;
import com.clinica.mspaciente.dto.PacienteResponseDTO;
import com.clinica.mspaciente.exception.DuplicateResourceException;
import com.clinica.mspaciente.exception.ResourceNotFoundException;
import com.clinica.mspaciente.model.Paciente;
import com.clinica.mspaciente.model.Prevision;
import com.clinica.mspaciente.repository.PacienteRepository;
import com.clinica.mspaciente.repository.PrevisionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PacienteServiceTest {

    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private PrevisionRepository previsionRepository;

    @InjectMocks
    private PacienteService pacienteService;

    @Test
    void obtenerTodosRetornaPacientesActivos() {
        when(pacienteRepository.findByActivoTrue()).thenReturn(List.of(pacienteExistente()));

        List<PacienteResponseDTO> response = pacienteService.obtenerTodos();

        assertEquals(1, response.size());
        assertEquals("12345678-9", response.get(0).getRut());
    }

    @Test
    void obtenerPorRutRetornaPaciente() {
        Paciente paciente = pacienteExistente();
        when(pacienteRepository.findByRut("12345678-9")).thenReturn(Optional.of(paciente));

        PacienteResponseDTO response = pacienteService.obtenerPorRut("12345678-9");

        assertEquals(paciente.getId(), response.getId());
        assertEquals(paciente.getRut(), response.getRut());
    }

    @Test
    void obtenerPorEdadRetornaPacientesActivos() {
        when(pacienteRepository.findByEdadAndActivoTrue(36)).thenReturn(List.of(pacienteExistente()));

        List<PacienteResponseDTO> response = pacienteService.obtenerPorEdad(36);

        assertEquals(1, response.size());
        assertEquals(36, response.get(0).getEdad());
    }

    @Test
    void crearPacienteCorrectamenteGuardaPaciente() {
        PacienteRequestDTO dto = pacienteAdultoRequest();
        Prevision prevision = previsionActiva();

        when(pacienteRepository.existsByRut(dto.getRut())).thenReturn(false);
        when(pacienteRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(previsionRepository.findById(dto.getPrevisionId())).thenReturn(Optional.of(prevision));
        when(pacienteRepository.save(any(Paciente.class))).thenAnswer(invocation -> {
            Paciente paciente = invocation.getArgument(0);
            paciente.setId(10L);
            paciente.setFechaRegistro(LocalDateTime.now());
            return paciente;
        });

        PacienteResponseDTO response = pacienteService.crear(dto);

        assertEquals(10L, response.getId());
        assertEquals(dto.getRut(), response.getRut());
        assertEquals(dto.getEdad(), response.getEdad());
        assertEquals(prevision.getId(), response.getPrevisionId());
        verify(pacienteRepository).save(any(Paciente.class));
    }

    @Test
    void crearPacienteConRutDuplicadoLanzaDuplicateResourceException() {
        PacienteRequestDTO dto = pacienteAdultoRequest();

        when(pacienteRepository.existsByRut(dto.getRut())).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> pacienteService.crear(dto));
    }

    @Test
    void crearPacienteConEmailDuplicadoLanzaDuplicateResourceException() {
        PacienteRequestDTO dto = pacienteAdultoRequest();
        when(pacienteRepository.existsByRut(dto.getRut())).thenReturn(false);
        when(pacienteRepository.existsByEmail(dto.getEmail())).thenReturn(true);
        
        assertThrows(DuplicateResourceException.class, () -> pacienteService.crear(dto));
    }

    @Test
    void crearPacienteConFechaFuturaLanzaIllegalArgumentException() {
        PacienteRequestDTO dto = pacienteAdultoRequest();
        dto.setFechaNacimiento(LocalDate.now().plusDays(1));
        assertThrows(IllegalArgumentException.class, () -> pacienteService.crear(dto));
    }

    @Test
    void crearPacienteConFechaAno1500LanzaIllegalArgumentException() {
        PacienteRequestDTO dto = pacienteAdultoRequest();
        dto.setFechaNacimiento(LocalDate.of(1500, 1, 1));
        assertThrows(IllegalArgumentException.class, () -> pacienteService.crear(dto));
    }

    @Test
    void crearPacienteConEdadMayorA120LanzaIllegalArgumentException() {
        PacienteRequestDTO dto = pacienteAdultoRequest();
        dto.setFechaNacimiento(LocalDate.now().minusYears(125));
        dto.setEdad(125);
        assertThrows(IllegalArgumentException.class, () -> pacienteService.crear(dto));
    }

    @Test
    void crearMenorSinAcompananteLanzaIllegalArgumentException() {
        PacienteRequestDTO dto = pacienteAdultoRequest();
        dto.setFechaNacimiento(LocalDate.now().minusYears(12));
        dto.setEdad(12);
        dto.setAcompanado(false);

        assertThrows(IllegalArgumentException.class, () -> pacienteService.crear(dto));
        verifyNoInteractions(pacienteRepository, previsionRepository);
    }

    @Test
    void crearMayorDe80SinAcompananteLanzaIllegalArgumentException() {
        PacienteRequestDTO dto = pacienteAdultoRequest();
        dto.setFechaNacimiento(LocalDate.now().minusYears(85));
        dto.setEdad(85);
        dto.setAcompanado(false);

        assertThrows(IllegalArgumentException.class, () -> pacienteService.crear(dto));
    }

    @Test
    void crearConEdadDistintaAFechaNacimientoLanzaIllegalArgumentException() {
        PacienteRequestDTO dto = pacienteAdultoRequest();
        dto.setEdad(40);

        assertThrows(IllegalArgumentException.class, () -> pacienteService.crear(dto));
        verifyNoInteractions(pacienteRepository, previsionRepository);
    }

    @Test
    void crearConPrevisionInactivaLanzaIllegalArgumentException() {
        PacienteRequestDTO dto = pacienteAdultoRequest();
        Prevision prevision = previsionActiva();
        prevision.setActivo(false);

        when(pacienteRepository.existsByRut(dto.getRut())).thenReturn(false);
        when(pacienteRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(previsionRepository.findById(dto.getPrevisionId())).thenReturn(Optional.of(prevision));

        assertThrows(IllegalArgumentException.class, () -> pacienteService.crear(dto));
    }

    @Test
    void actualizarPacienteCorrectamenteGuardaCambios() {
        PacienteRequestDTO dto = pacienteAdultoRequest();
        dto.setApellido("Perez Soto");
        Paciente paciente = pacienteExistente();
        Prevision prevision = previsionActiva();

        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(previsionRepository.findById(dto.getPrevisionId())).thenReturn(Optional.of(prevision));
        when(pacienteRepository.save(any(Paciente.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PacienteResponseDTO response = pacienteService.actualizar(1L, dto);

        assertEquals("Perez Soto", response.getApellido());
        verify(pacienteRepository).save(any(Paciente.class));
    }

    @Test
    void actualizarConRutDuplicadoLanzaDuplicateResourceException() {
        PacienteRequestDTO dto = pacienteAdultoRequest();
        dto.setRut("87654321-0");
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(pacienteExistente()));
        when(pacienteRepository.existsByRut(dto.getRut())).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> pacienteService.actualizar(1L, dto));
    }

    @Test
    void actualizarConEmailDuplicadoLanzaDuplicateResourceException() {
        PacienteRequestDTO dto = pacienteAdultoRequest();
        dto.setEmail("otro@example.com");
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(pacienteExistente()));
        when(pacienteRepository.existsByEmail(dto.getEmail())).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> pacienteService.actualizar(1L, dto));
    }

    @Test
    void obtenerPorIdNoEncontradoLanzaResourceNotFoundException() {
        when(pacienteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> pacienteService.obtenerPorId(99L));
    }

    @Test
    void obtenerPorEdadSinResultadosLanzaResourceNotFoundException() {
        when(pacienteRepository.findByEdadAndActivoTrue(36)).thenReturn(List.of());

        assertThrows(ResourceNotFoundException.class, () -> pacienteService.obtenerPorEdad(36));
    }

    @Test
    void obtenerPorEdadInvalidaLanzaIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> pacienteService.obtenerPorEdad(130));
    }

    @Test
    void desactivarPacienteCambiaActivoFalse() {
        Paciente paciente = pacienteExistente();
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));

        pacienteService.desactivar(1L);

        ArgumentCaptor<Paciente> captor = ArgumentCaptor.forClass(Paciente.class);
        verify(pacienteRepository).save(captor.capture());
        assertFalse(captor.getValue().getActivo());
    }

    @Test
    void eliminarPacienteEliminaEntidadExistente() {
        Paciente paciente = pacienteExistente();
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));

        pacienteService.eliminar(1L);

        verify(pacienteRepository).delete(paciente);
    }

    @Test
    void buscarPorApellidoRetornaResultados() {
        when(pacienteRepository.findByApellidoContainingIgnoreCase("Perez"))
                .thenReturn(List.of(pacienteExistente()));

        List<PacienteResponseDTO> response = pacienteService.buscarPorApellido("Perez");

        assertEquals(1, response.size());
        assertEquals("Perez", response.get(0).getApellido());
    }

    private PacienteRequestDTO pacienteAdultoRequest() {
        PacienteRequestDTO dto = new PacienteRequestDTO();
        dto.setRut("12345678-9");
        dto.setNombre("Juan");
        dto.setApellido("Perez");
        dto.setFechaNacimiento(LocalDate.now().minusYears(36));
        dto.setGenero("MASCULINO");
        dto.setEmail("juan.perez@example.com");
        dto.setTelefono("+56912345678");
        dto.setDireccion("Av. Siempre Viva 123");
        dto.setPrevisionId(1L);
        dto.setEdad(36);
        dto.setAcompanado(false);
        return dto;
    }

    private Paciente pacienteExistente() {
        Paciente paciente = new Paciente();
        paciente.setId(1L);
        paciente.setRut("12345678-9");
        paciente.setNombre("Juan");
        paciente.setApellido("Perez");
        paciente.setFechaNacimiento(LocalDate.now().minusYears(36));
        paciente.setGenero("MASCULINO");
        paciente.setEmail("juan.perez@example.com");
        paciente.setTelefono("+56912345678");
        paciente.setDireccion("Av. Siempre Viva 123");
        paciente.setPrevision(previsionActiva());
        paciente.setActivo(true);
        paciente.setEdad(36);
        paciente.setAcompanado(false);
        paciente.setFechaRegistro(LocalDateTime.now());
        return paciente;
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
