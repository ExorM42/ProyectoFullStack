package com.ms.ms_cita.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ms.ms_cita.client.DoctorClient;
import com.ms.ms_cita.client.PacienteClient;
import com.ms.ms_cita.dto.ActualizarEstadoCitaDTO;
import com.ms.ms_cita.dto.CitaRequestDTO;
import com.ms.ms_cita.dto.CitaResponseDTO;
import com.ms.ms_cita.dto.DoctorDTO;
import com.ms.ms_cita.dto.PacienteDTO;
import com.ms.ms_cita.dto.RemoteApiWrapper;
import com.ms.ms_cita.exception.ResourceNotFoundException;
import com.ms.ms_cita.model.Cita;
import com.ms.ms_cita.repository.CitaRepository;

@ExtendWith(MockitoExtension.class)
public class CitaServiceTest {

    @Mock
    private CitaRepository citaRepo;

    @Mock
    private PacienteClient pacienteClient;

    @Mock
    private DoctorClient doctorClient;

    @InjectMocks
    private CitaService citaService;

    @Test
    void givenCitas_whenFindAll_thenReturnCitas() {

        // GIVEN
        Cita cita1 = new Cita();
        cita1.setId(1L);

        Cita cita2 = new Cita();
        cita2.setId(2L);

        // WHEN
        when(citaRepo.findAll()).thenReturn(List.of(cita1, cita2));
        List<CitaResponseDTO> res = citaService.obtenerTodas();

        // THEN
        assertNotNull(res);
        assertEquals(2, res.size());
        assertEquals(cita1.getId(), res.get(0).getId());
        assertEquals(cita2.getId(), res.get(1).getId());

        verify(citaRepo, times(1)).findAll();
    }

    @Test
    void givenIdCita_whenFindById_thenReturnCita() {

        // GIVEN
        Long citaId = 1L;
        Cita cita = new Cita();
        cita.setId(citaId);
        cita.setPacienteId(1L);
        cita.setDoctorId(2L);
        cita.setEstado("PENDIENTE");

        // WHEN
        when(citaRepo.findById(citaId)).thenReturn(Optional.of(cita));
        CitaResponseDTO res = citaService.obtenerPorId(citaId);

        // THEN
        assertNotNull(res);
        assertEquals(cita.getId(), res.getId());
        assertEquals(cita.getEstado(), res.getEstado());

        verify(citaRepo, times(1)).findById(citaId);
    }

    @Test
    void givenIdCita_whenFindByIdNotFound_thenThrowResourceNotFoundException() {

        // GIVEN
        Long citaId = 99L;

        // WHEN
        when(citaRepo.findById(citaId)).thenReturn(Optional.empty());

        // THEN
        assertThrows(ResourceNotFoundException.class, () -> {
            citaService.obtenerPorId(citaId);
        });

        verify(citaRepo, times(1)).findById(citaId);
    }

    @Test
    void givenPacienteId_whenFindByPacienteId_thenReturnCitas() {

        // GIVEN
        Long pacienteId = 1L;
        Cita cita = new Cita();
        cita.setId(1L);
        cita.setPacienteId(pacienteId);

        // WHEN
        when(citaRepo.findByPacienteId(pacienteId)).thenReturn(List.of(cita));
        List<CitaResponseDTO> res = citaService.obtenerPorPaciente(pacienteId);

        // THEN
        assertNotNull(res);
        assertEquals(1, res.size());
        assertEquals(pacienteId, res.get(0).getPacienteId());

        verify(citaRepo, times(1)).findByPacienteId(pacienteId);
    }

    @Test
    void givenDoctorId_whenFindByDoctorId_thenReturnCitas() {

        // GIVEN
        Long doctorId = 2L;
        Cita cita = new Cita();
        cita.setId(1L);
        cita.setDoctorId(doctorId);

        // WHEN
        when(citaRepo.findByDoctorId(doctorId)).thenReturn(List.of(cita));
        List<CitaResponseDTO> res = citaService.obtenerPorDoctor(doctorId);

        // THEN
        assertNotNull(res);
        assertEquals(1, res.size());
        assertEquals(doctorId, res.get(0).getDoctorId());

        verify(citaRepo, times(1)).findByDoctorId(doctorId);
    }

    @Test
    void givenCitaRequest_whenCrearCita_thenReturnCitaCreada() {

        // GIVEN
        Long pacienteId = 1L;
        Long doctorId = 2L;

        CitaRequestDTO dto = new CitaRequestDTO();
        dto.setDoctorId(doctorId);
        dto.setFechaHoraCita(LocalDateTime.now().plusDays(1));
        dto.setMotivoConsulta("Dolor de cabeza persistente");
        dto.setDuracionMinutos(30);

        PacienteDTO paciente = new PacienteDTO();
        paciente.setId(pacienteId);
        paciente.setActivo(true);
        paciente.setNombre("Diego");

        DoctorDTO doctor = new DoctorDTO();
        doctor.setId(doctorId);
        doctor.setActivo(true);
        doctor.setNombre("Doctor");

        RemoteApiWrapper<PacienteDTO> pacienteWrapper = new RemoteApiWrapper<>();
        pacienteWrapper.setSuccess(true);
        pacienteWrapper.setData(paciente);

        RemoteApiWrapper<DoctorDTO> doctorWrapper = new RemoteApiWrapper<>();
        doctorWrapper.setSuccess(true);
        doctorWrapper.setData(doctor);

        Cita guardada = new Cita();
        guardada.setId(10L);
        guardada.setPacienteId(pacienteId);
        guardada.setDoctorId(doctorId);
        guardada.setEstado("PENDIENTE");

        // WHEN
        when(pacienteClient.obtenerPorId(pacienteId)).thenReturn(pacienteWrapper);
        when(doctorClient.obtenerPorId(doctorId)).thenReturn(doctorWrapper);
        when(citaRepo.save(any(Cita.class))).thenReturn(guardada);

        CitaResponseDTO res = citaService.crearCita(dto, pacienteId);

        // THEN
        assertNotNull(res);
        assertEquals(guardada.getId(), res.getId());
        assertEquals(pacienteId, res.getPacienteId());
        assertEquals(doctorId, res.getDoctorId());

        verify(citaRepo, times(1)).save(any(Cita.class));
    }

    @Test
    void givenEstadoDTO_whenActualizarEstado_thenReturnCitaActualizada() {

        // GIVEN
        Long citaId = 1L;
        Cita cita = new Cita();
        cita.setId(citaId);
        cita.setEstado("PENDIENTE");

        ActualizarEstadoCitaDTO dto = new ActualizarEstadoCitaDTO();
        dto.setEstado("CONFIRMADA");
        dto.setObservaciones("Confirmada por el doctor");

        // WHEN
        when(citaRepo.findById(citaId)).thenReturn(Optional.of(cita));
        when(citaRepo.save(cita)).thenReturn(cita);

        CitaResponseDTO res = citaService.actualizarEstado(citaId, dto);

        // THEN
        assertNotNull(res);
        assertEquals("CONFIRMADA", res.getEstado());
        assertEquals(dto.getObservaciones(), res.getObservaciones());

        verify(citaRepo, times(1)).save(cita);
    }

    @Test
    void givenCitaAndPacienteId_whenCancelar_thenEstadoCancelada() {

        // GIVEN
        Long citaId = 1L;
        Long pacienteId = 1L;
        Cita cita = new Cita();
        cita.setId(citaId);
        cita.setPacienteId(pacienteId);
        cita.setEstado("PENDIENTE");

        // WHEN
        when(citaRepo.findById(citaId)).thenReturn(Optional.of(cita));
        citaService.cancelar(citaId, pacienteId);

        // THEN
        assertEquals("CANCELADA", cita.getEstado());
        verify(citaRepo, times(1)).save(cita);
    }

    @Test
    void givenOtroPaciente_whenCancelar_thenThrowIllegalStateException() {

        // GIVEN
        Long citaId = 1L;
        Long pacienteId = 1L;
        Long otroPacienteId = 99L;
        Cita cita = new Cita();
        cita.setId(citaId);
        cita.setPacienteId(pacienteId);
        cita.setEstado("PENDIENTE");

        // WHEN
        when(citaRepo.findById(citaId)).thenReturn(Optional.of(cita));

        // THEN
        assertThrows(IllegalStateException.class, () -> {
            citaService.cancelar(citaId, otroPacienteId);
        });

        verify(citaRepo, times(1)).findById(citaId);
    }

    @Test
    void givenIdCita_whenExistsById_thenDeleteCita() {

        // GIVEN
        Long citaId = 1L;

        // WHEN
        when(citaRepo.existsById(citaId)).thenReturn(true);
        citaService.eliminar(citaId);

        // THEN
        verify(citaRepo, times(1)).existsById(citaId);
        verify(citaRepo, times(1)).deleteById(citaId);
    }

    @Test
    void givenCitasHoy_whenFindCitasHoy_thenReturnCitas() {

        // GIVEN
        Cita cita = new Cita();
        cita.setId(1L);
        cita.setFechaHoraCita(LocalDateTime.now());

        // WHEN
        when(citaRepo.findCitasHoy(any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(List.of(cita));
        List<CitaResponseDTO> res = citaService.obtenerCitasHoy();

        // THEN
        assertNotNull(res);
        assertEquals(1, res.size());
        assertEquals(cita.getId(), res.get(0).getId());

        verify(citaRepo, times(1)).findCitasHoy(any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    void givenPacienteInactivo_whenCrearCita_thenThrowIllegalStateException() {

        // GIVEN
        Long pacienteId = 1L;
        CitaRequestDTO dto = new CitaRequestDTO();
        dto.setDoctorId(2L);

        PacienteDTO paciente = new PacienteDTO();
        paciente.setActivo(false);

        RemoteApiWrapper<PacienteDTO> wrapper = new RemoteApiWrapper<>();
        wrapper.setSuccess(true);
        wrapper.setData(paciente);

        // WHEN
        when(pacienteClient.obtenerPorId(pacienteId)).thenReturn(wrapper);

        // THEN
        assertThrows(IllegalStateException.class, () -> {
            citaService.crearCita(dto, pacienteId);
        });

        verify(pacienteClient, times(1)).obtenerPorId(pacienteId);
    }

    @Test
    void givenCitaCompletada_whenActualizarEstado_thenThrowIllegalStateException() {

        // GIVEN
        Long citaId = 1L;
        Cita cita = new Cita();
        cita.setId(citaId);
        cita.setEstado("COMPLETADA");

        ActualizarEstadoCitaDTO dto = new ActualizarEstadoCitaDTO();
        dto.setEstado("CANCELADA");

        // WHEN
        when(citaRepo.findById(citaId)).thenReturn(Optional.of(cita));

        // THEN
        assertThrows(IllegalStateException.class, () -> {
            citaService.actualizarEstado(citaId, dto);
        });

        verify(citaRepo, times(1)).findById(citaId);
    }

    @Test
    void givenCitaCompletada_whenCancelar_thenThrowIllegalStateException() {

        // GIVEN
        Long citaId = 1L;
        Long pacienteId = 1L;
        Cita cita = new Cita();
        cita.setId(citaId);
        cita.setPacienteId(pacienteId);
        cita.setEstado("COMPLETADA");

        // WHEN
        when(citaRepo.findById(citaId)).thenReturn(Optional.of(cita));

        // THEN
        assertThrows(IllegalStateException.class, () -> {
            citaService.cancelar(citaId, pacienteId);
        });

        verify(citaRepo, times(1)).findById(citaId);
    }

    @Test
    void givenCitaCancelada_whenCancelar_thenThrowIllegalStateException() {

        // GIVEN
        Long citaId = 1L;
        Long pacienteId = 1L;
        Cita cita = new Cita();
        cita.setId(citaId);
        cita.setPacienteId(pacienteId);
        cita.setEstado("CANCELADA");

        // WHEN
        when(citaRepo.findById(citaId)).thenReturn(Optional.of(cita));

        // THEN
        assertThrows(IllegalStateException.class, () -> {
            citaService.cancelar(citaId, pacienteId);
        });

        verify(citaRepo, times(1)).findById(citaId);
    }

    @Test
    void givenIdCita_whenExistsByIdFalse_thenThrowResourceNotFoundException() {

        // GIVEN
        Long citaId = 99L;

        // WHEN
        when(citaRepo.existsById(citaId)).thenReturn(false);

        // THEN
        assertThrows(ResourceNotFoundException.class, () -> {
            citaService.eliminar(citaId);
        });

        verify(citaRepo, times(1)).existsById(citaId);
    }
}
