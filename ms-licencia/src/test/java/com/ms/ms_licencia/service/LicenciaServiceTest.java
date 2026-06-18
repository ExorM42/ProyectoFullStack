package com.ms.ms_licencia.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ms.ms_licencia.client.CitaClient;
import com.ms.ms_licencia.client.DoctorClient;
import com.ms.ms_licencia.client.PacienteClient;
import com.ms.ms_licencia.dto.ActualizarEstadoLicenciaDTO;
import com.ms.ms_licencia.dto.CitaDTO;
import com.ms.ms_licencia.dto.DoctorDTO;
import com.ms.ms_licencia.dto.LicenciaRequestDTO;
import com.ms.ms_licencia.dto.LicenciaResponseDTO;
import com.ms.ms_licencia.dto.PacienteDTO;
import com.ms.ms_licencia.dto.RemoteApiWrapper;
import com.ms.ms_licencia.exception.LicenciaConflictoException;
import com.ms.ms_licencia.exception.ResourceNotFoundException;
import com.ms.ms_licencia.model.LicenciaMedica;
import com.ms.ms_licencia.model.TipoLicencia;
import com.ms.ms_licencia.repository.LicenciaMedicaRepository;
import com.ms.ms_licencia.repository.TipoLicenciaRepository;

@ExtendWith(MockitoExtension.class)
public class LicenciaServiceTest {

    @Mock
    private LicenciaMedicaRepository licenciaRepo;

    @Mock
    private TipoLicenciaRepository tipoLicenciaRepo;

    @Mock
    private PacienteClient pacienteClient;

    @Mock
    private DoctorClient doctorClient;

    @Mock
    private CitaClient citaClient;

    @InjectMocks
    private LicenciaService licenciaService;


    @Test
    void givenIdLicencia_whenFindById_thenReturnLicencia(){

        //GIVEN
        Long licenciaId = 1L;

        LicenciaMedica licencia = new LicenciaMedica();
        licencia.setId(licenciaId);
        licencia.setFolio("LIC-999");
        licencia.setPacienteId(1L);
        licencia.setDoctorId(2L);
        licencia.setEstado("EMITIDA");

        //WHEN
        when(licenciaRepo.findById(licenciaId)).thenReturn(Optional.of(licencia));
        LicenciaResponseDTO resultado = licenciaService.obtenerPorId(licenciaId);

        //THEN
        assertNotNull(resultado);
        assertEquals(licencia.getId(), resultado.getId());
        assertEquals(licencia.getFolio(), resultado.getFolio());
        assertEquals(licencia.getEstado(), resultado.getEstado());

        verify(licenciaRepo, times(1)).findById(licenciaId);
        

    }

    @Test
    void givenFolio_whenFindByFolio_thenReturnLicencia(){

        //GIVEN
        String folio = "LIC-999";

        LicenciaMedica licencia = new LicenciaMedica();
        licencia.setId(1L);
        licencia.setFolio(folio);
        licencia.setPacienteId(2L);
        licencia.setDoctorId(3L);
        licencia.setEstado("EMITIDA");

        //WHEN
        when(licenciaRepo.findByFolio(folio)).thenReturn(Optional.of(licencia));
        LicenciaResponseDTO resultado = licenciaService.obtenerPorFolio(folio);

        // THEN

        assertNotNull(resultado);
        assertEquals(licencia.getId(), resultado.getId());
        assertEquals(licencia.getFolio(), resultado.getFolio());
        assertEquals(licencia.getEstado(), resultado.getEstado());

        verify(licenciaRepo, times(1)).findByFolio(folio);
    }

    @Test
    void givenPacienteId_whenFindByPacienteId_thenReturnLicencias(){

        //GIVEN
        Long pacienteId = 1L;

        LicenciaMedica licencia = new LicenciaMedica();

        licencia.setId(2L);
        licencia.setFolio("LIC-999");
        licencia.setPacienteId(pacienteId);
        licencia.setDoctorId(3L);
        licencia.setEstado("EMITIDA");

        // WHEN
        when(licenciaRepo.findByPacienteId(pacienteId)).thenReturn(List.of(licencia));

        List<LicenciaResponseDTO> resultado = licenciaService.obtenerPorPaciente(pacienteId);

        // THEN
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(licencia.getId(), resultado.get(0).getId());
        assertEquals(licencia.getPacienteId(), resultado.get(0).getPacienteId());
        assertEquals(licencia.getFolio(), resultado.get(0).getFolio());

        verify(licenciaRepo, times(1)).findByPacienteId(pacienteId);


    }

    @Test
    void givenDoctorId_whenFindByDoctorId_thenReturnLicencias(){

        //GIVEN
        Long doctorId = 3L;

        LicenciaMedica licencia = new LicenciaMedica();

        licencia.setId(1L);
        licencia.setFolio("LIC-999");
        licencia.setPacienteId(2L);
        licencia.setDoctorId(doctorId);
        licencia.setEstado("EMITIDA");


        //WHEN
        when(licenciaRepo.findByDoctorId(doctorId)).thenReturn(List.of(licencia));
        List<LicenciaResponseDTO> resultado = licenciaService.obtenerPorDoctor(doctorId);

        //THEN
        assertNotNull(resultado);

        assertEquals(1, resultado.size());
        assertEquals(licencia.getId(), resultado.get(0).getId());
        assertEquals(licencia.getDoctorId(), resultado.get(0).getDoctorId());
        assertEquals(licencia.getFolio(), resultado.get(0).getFolio());

        verify(licenciaRepo, times(1)).findByDoctorId(doctorId);


    }

    @Test
    void givenLicencias_whenFindAll_thenReturnAllLicencias(){

        //GIVEN
        LicenciaMedica lic1 = new LicenciaMedica();
        LicenciaMedica lic2 = new LicenciaMedica();

        //WHEN
        when(licenciaRepo.findAll()).thenReturn(List.of(lic1, lic2));
        List<LicenciaResponseDTO> resultado = licenciaService.obtenerTodas();

        //THEN
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(lic1.getId(), resultado.get(0).getId());
        assertEquals(lic2.getId(), resultado.get(1).getId());

        verify(licenciaRepo, times(1)).findAll();
    }

    @Test
    void givenLicenciaId_whenFindByIdNotFound_thenThrowResourceNotFoundException(){

        //GIVEN
        Long licenciaId = 5L;

        //WHEN
        when(licenciaRepo.findById(licenciaId)).thenReturn(Optional.empty());

        //THEN
        assertThrows(ResourceNotFoundException.class, () -> {licenciaService.obtenerPorId(licenciaId);});

        verify(licenciaRepo, times(1)).findById(licenciaId);
    }

    @Test
    void givenFolio_whenFindByFolioNotFound_thenThrowResourceNotFoundException(){

        //GIVEN
        String folio = "LIC-998";

        //WHEN
        when(licenciaRepo.findByFolio(folio)).thenReturn(Optional.empty());

        //THEN
        assertThrows(ResourceNotFoundException.class, () -> {
                licenciaService.obtenerPorFolio(folio);
        });

        verify(licenciaRepo, times(1)).findByFolio(folio);
    }

    @Test
    void givenIdLicencia_whenExistsById_thenDeleteLicencia(){

        //GIVEN
        Long licenciaId = 1L;

        //WHEN
        when(licenciaRepo.existsById(licenciaId)).thenReturn(true);

        licenciaService.eliminar(licenciaId);

        //THEN
        verify(licenciaRepo, times(1)).existsById(licenciaId);
        verify(licenciaRepo, times(1)).deleteById(licenciaId);
    }

    @Test
    void givenIdLicencia_whenExistByIdFalse_thenThrowResourceNotFoundException(){

        //GIVEN
        Long licenciaId = 50L;

        //WHEN
        when(licenciaRepo.existsById(licenciaId)).thenReturn(false);

        // THEN

        assertThrows(ResourceNotFoundException.class, () -> {
            licenciaService.eliminar(licenciaId);
        });
        
        verify(licenciaRepo, times(1)).existsById(licenciaId);
        verify(licenciaRepo, times(0)).deleteById(licenciaId);
    }

    @Test
    void givenLicenciaAndDoctorId_whenAnular_thenEstadoAnulada(){

        //GIVEN
        Long licenciaId = 1L;
        Long doctorId = 2L;

        LicenciaMedica lic = new LicenciaMedica();
        lic.setId(licenciaId); 
        lic.setDoctorId(doctorId);
        lic.setEstado("EMITIDA");

        //WHEN
        when(licenciaRepo.findById(licenciaId)).thenReturn(Optional.of(lic));
        licenciaService.anular(licenciaId, doctorId);

        //THEN
        assertEquals("ANULADA", lic.getEstado());
        verify(licenciaRepo, times(1)).findById(licenciaId);
        verify(licenciaRepo, times(1)).save(lic);
    }

    @Test
    void givenDoctorIncorrecto_whenAnular_thenThrowIllegalStateException(){

        //GIVEN
        Long licenciaId = 1L;
        Long doctorId = 2l;
        Long doctorX = 3L;

        LicenciaMedica lic = new LicenciaMedica();
        lic.setId(licenciaId);
        lic.setDoctorId(doctorId);
        lic.setEstado("EMITIDA");

        //WHEN
        when(licenciaRepo.findById(licenciaId)).thenReturn(Optional.of(lic));

        //THEN
        assertThrows(IllegalStateException.class, () -> {
            licenciaService.anular(licenciaId, doctorX);
        });

        verify(licenciaRepo, times(1)).findById(licenciaId);
        verify(licenciaRepo, times(0)).save(lic);
    }

    @Test
    void givenEstado_whenActualizarEstado_thenReturnLicenciaActualizada(){

        //GIVEN
        Long licenciaId = 1L;
        Long doctorId = 2L;

        LicenciaMedica lic = new LicenciaMedica();
        lic.setId(licenciaId);
        lic.setDoctorId(doctorId);
        lic.setPacienteId(3L);
        lic.setEstado("EMITIDA");

        ActualizarEstadoLicenciaDTO dto = new ActualizarEstadoLicenciaDTO();
        dto.setEstado("APROBADA");
        dto.setObservaciones("Lic aprobada");
        
        //WHEN
        when(licenciaRepo.findById(licenciaId)).thenReturn(Optional.of(lic));
        when(licenciaRepo.save(lic)).thenReturn(lic);

        LicenciaResponseDTO result = licenciaService.actualizarEstado(licenciaId, dto, doctorId);

        //THEN
        assertNotNull(result);
        assertEquals("APROBADA", result.getEstado());
        assertEquals(dto.getObservaciones(), result.getObservaciones());

        verify(licenciaRepo, times(1)).findById(licenciaId);
        verify(licenciaRepo, times(1)).save(lic);
    }

    @Test
    void givenLicenciaRequest_whenEmitir_thenReturnLicenciaEmitida() {

        // GIVEN
        Long pacienteId = 1L;
        Long doctorId = 2L;
        Long tipoId = 3L;

        LicenciaRequestDTO dto = new LicenciaRequestDTO();
        dto.setPacienteId(pacienteId);
        dto.setTipoLicenciaId(tipoId);
        dto.setFechaInicio(LocalDate.now().plusDays(1));
        dto.setFechaFin(LocalDate.now().plusDays(3));
        dto.setDiagnostico("Reposo medico");

        TipoLicencia tipo = new TipoLicencia();
        tipo.setId(tipoId);
        tipo.setDiasMaximos(10);

        PacienteDTO paciente = new PacienteDTO();
        paciente.setId(pacienteId);
        paciente.setActivo(true);

        DoctorDTO doctor = new DoctorDTO();
        doctor.setId(doctorId);
        doctor.setActivo(true);

        RemoteApiWrapper<PacienteDTO> pacienteWrapper = new RemoteApiWrapper<>();
        pacienteWrapper.setSuccess(true);
        pacienteWrapper.setData(paciente);

        RemoteApiWrapper<DoctorDTO> doctorWrapper = new RemoteApiWrapper<>();
        doctorWrapper.setSuccess(true);
        doctorWrapper.setData(doctor);

        LicenciaMedica guardada = new LicenciaMedica();
        guardada.setId(1L);
        guardada.setFolio("LIC-TEST");
        guardada.setPacienteId(pacienteId);
        guardada.setDoctorId(doctorId);
        guardada.setTipoLicencia(tipo);
        guardada.setEstado("EMITIDA");
        guardada.setDiasReposo(3);

        // WHEN
        when(tipoLicenciaRepo.findById(tipoId)).thenReturn(Optional.of(tipo));
        when(licenciaRepo.existeLicenciaVigenteEnPeriodo(pacienteId, dto.getFechaInicio(), dto.getFechaFin())).thenReturn(false);
        when(pacienteClient.obtenerPorId(pacienteId)).thenReturn(pacienteWrapper);
        when(doctorClient.obtenerPorId(doctorId)).thenReturn(doctorWrapper);
        when(licenciaRepo.totalDiasReposoAnio(pacienteId, LocalDate.now().getYear())).thenReturn(0);
        when(licenciaRepo.existsByFolio(any(String.class))).thenReturn(false);
        when(licenciaRepo.save(any(LicenciaMedica.class))).thenReturn(guardada);

        LicenciaResponseDTO res = licenciaService.emitir(dto, doctorId);

        // THEN
        assertNotNull(res);
        assertEquals(guardada.getId(), res.getId());
        assertEquals(pacienteId, res.getPacienteId());
        assertEquals(doctorId, res.getDoctorId());

        verify(licenciaRepo, times(1)).save(any(LicenciaMedica.class));
    }

    @Test
    void givenFechaFinIncorrecta_whenEmitir_thenThrowIllegalStateException() {

        // GIVEN
        LicenciaRequestDTO dto = new LicenciaRequestDTO();
        dto.setFechaInicio(LocalDate.now().plusDays(5));
        dto.setFechaFin(LocalDate.now().plusDays(1));

        // THEN
        assertThrows(IllegalStateException.class, () -> {
            licenciaService.emitir(dto, 2L);
        });
    }

    @Test
    void givenDiasReposoMayorAlMaximo_whenEmitir_thenThrowLicenciaConflictoException() {

        // GIVEN
        Long tipoId = 1L;
        LicenciaRequestDTO dto = new LicenciaRequestDTO();
        dto.setPacienteId(1L);
        dto.setTipoLicenciaId(tipoId);
        dto.setFechaInicio(LocalDate.now().plusDays(1));
        dto.setFechaFin(LocalDate.now().plusDays(10));

        TipoLicencia tipo = new TipoLicencia();
        tipo.setId(tipoId);
        tipo.setDiasMaximos(3);

        // WHEN
        when(tipoLicenciaRepo.findById(tipoId)).thenReturn(Optional.of(tipo));

        // THEN
        assertThrows(LicenciaConflictoException.class, () -> {
            licenciaService.emitir(dto, 2L);
        });

        verify(tipoLicenciaRepo, times(1)).findById(tipoId);
    }

    @Test
    void givenLicenciaVigente_whenEmitir_thenThrowLicenciaConflictoException() {

        // GIVEN
        Long pacienteId = 1L;
        Long tipoId = 1L;
        LicenciaRequestDTO dto = new LicenciaRequestDTO();
        dto.setPacienteId(pacienteId);
        dto.setTipoLicenciaId(tipoId);
        dto.setFechaInicio(LocalDate.now().plusDays(1));
        dto.setFechaFin(LocalDate.now().plusDays(2));

        TipoLicencia tipo = new TipoLicencia();
        tipo.setId(tipoId);
        tipo.setDiasMaximos(10);

        // WHEN
        when(tipoLicenciaRepo.findById(tipoId)).thenReturn(Optional.of(tipo));
        when(licenciaRepo.existeLicenciaVigenteEnPeriodo(pacienteId, dto.getFechaInicio(), dto.getFechaFin())).thenReturn(true);

        // THEN
        assertThrows(LicenciaConflictoException.class, () -> {
            licenciaService.emitir(dto, 2L);
        });

        verify(licenciaRepo, times(1)).existeLicenciaVigenteEnPeriodo(pacienteId, dto.getFechaInicio(), dto.getFechaFin());
    }

    @Test
    void givenLicenciaAnulada_whenActualizarEstado_thenThrowIllegalStateException() {

        // GIVEN
        Long licenciaId = 1L;
        Long doctorId = 2L;

        LicenciaMedica lic = new LicenciaMedica();
        lic.setId(licenciaId);
        lic.setDoctorId(doctorId);
        lic.setEstado("ANULADA");

        ActualizarEstadoLicenciaDTO dto = new ActualizarEstadoLicenciaDTO();
        dto.setEstado("APROBADA");

        // WHEN
        when(licenciaRepo.findById(licenciaId)).thenReturn(Optional.of(lic));

        // THEN
        assertThrows(IllegalStateException.class, () -> {
            licenciaService.actualizarEstado(licenciaId, dto, doctorId);
        });

        verify(licenciaRepo, times(1)).findById(licenciaId);
    }

    @Test
    void givenDoctorIncorrecto_whenActualizarEstado_thenThrowIllegalStateException() {

        // GIVEN
        Long licenciaId = 1L;
        Long doctorId = 2L;

        LicenciaMedica lic = new LicenciaMedica();
        lic.setId(licenciaId);
        lic.setDoctorId(99L);
        lic.setEstado("EMITIDA");

        ActualizarEstadoLicenciaDTO dto = new ActualizarEstadoLicenciaDTO();
        dto.setEstado("APROBADA");

        // WHEN
        when(licenciaRepo.findById(licenciaId)).thenReturn(Optional.of(lic));

        // THEN
        assertThrows(IllegalStateException.class, () -> {
            licenciaService.actualizarEstado(licenciaId, dto, doctorId);
        });

        verify(licenciaRepo, times(1)).findById(licenciaId);
    }

    @Test
    void givenLicenciaRequestConCita_whenEmitir_thenReturnLicenciaEmitida() {

        // GIVEN
        Long pacienteId = 1L;
        Long doctorId = 2L;
        Long tipoId = 3L;
        Long citaId = 4L;

        LicenciaRequestDTO dto = new LicenciaRequestDTO();
        dto.setPacienteId(pacienteId);
        dto.setTipoLicenciaId(tipoId);
        dto.setCitaId(citaId);
        dto.setFechaInicio(LocalDate.now().plusDays(1));
        dto.setFechaFin(LocalDate.now().plusDays(2));
        dto.setDiagnostico("Reposo medico");

        TipoLicencia tipo = new TipoLicencia();
        tipo.setId(tipoId);
        tipo.setDiasMaximos(10);

        PacienteDTO paciente = new PacienteDTO();
        paciente.setId(pacienteId);
        paciente.setActivo(true);

        DoctorDTO doctor = new DoctorDTO();
        doctor.setId(doctorId);
        doctor.setActivo(true);

        CitaDTO cita = new CitaDTO();
        cita.setId(citaId);
        cita.setPacienteId(pacienteId);
        cita.setDoctorId(doctorId);
        cita.setEstado("COMPLETADA");

        RemoteApiWrapper<PacienteDTO> pacienteWrapper = new RemoteApiWrapper<>();
        pacienteWrapper.setSuccess(true);
        pacienteWrapper.setData(paciente);

        RemoteApiWrapper<DoctorDTO> doctorWrapper = new RemoteApiWrapper<>();
        doctorWrapper.setSuccess(true);
        doctorWrapper.setData(doctor);

        RemoteApiWrapper<CitaDTO> citaWrapper = new RemoteApiWrapper<>();
        citaWrapper.setSuccess(true);
        citaWrapper.setData(cita);

        LicenciaMedica guardada = new LicenciaMedica();
        guardada.setId(1L);
        guardada.setFolio("LIC-TEST");
        guardada.setPacienteId(pacienteId);
        guardada.setDoctorId(doctorId);
        guardada.setTipoLicencia(tipo);
        guardada.setEstado("EMITIDA");
        guardada.setDiasReposo(2);

        // WHEN
        when(tipoLicenciaRepo.findById(tipoId)).thenReturn(Optional.of(tipo));
        when(licenciaRepo.existeLicenciaVigenteEnPeriodo(pacienteId, dto.getFechaInicio(), dto.getFechaFin())).thenReturn(false);
        when(pacienteClient.obtenerPorId(pacienteId)).thenReturn(pacienteWrapper);
        when(doctorClient.obtenerPorId(doctorId)).thenReturn(doctorWrapper);
        when(citaClient.obtenerPorId(citaId)).thenReturn(citaWrapper);
        when(licenciaRepo.totalDiasReposoAnio(pacienteId, LocalDate.now().getYear())).thenReturn(0);
        when(licenciaRepo.existsByFolio(any(String.class))).thenReturn(false);
        when(licenciaRepo.save(any(LicenciaMedica.class))).thenReturn(guardada);

        LicenciaResponseDTO res = licenciaService.emitir(dto, doctorId);

        // THEN
        assertNotNull(res);
        assertEquals(guardada.getId(), res.getId());

        verify(citaClient, times(1)).obtenerPorId(citaId);
        verify(licenciaRepo, times(1)).save(any(LicenciaMedica.class));
    }

    @Test
    void givenCitaDeOtroPaciente_whenEmitir_thenThrowIllegalStateException() {

        // GIVEN
        Long pacienteId = 1L;
        Long doctorId = 2L;
        Long tipoId = 3L;
        Long citaId = 4L;

        LicenciaRequestDTO dto = new LicenciaRequestDTO();
        dto.setPacienteId(pacienteId);
        dto.setTipoLicenciaId(tipoId);
        dto.setCitaId(citaId);
        dto.setFechaInicio(LocalDate.now().plusDays(1));
        dto.setFechaFin(LocalDate.now().plusDays(2));

        TipoLicencia tipo = new TipoLicencia();
        tipo.setId(tipoId);
        tipo.setDiasMaximos(10);

        PacienteDTO paciente = new PacienteDTO();
        paciente.setActivo(true);

        DoctorDTO doctor = new DoctorDTO();
        doctor.setActivo(true);

        CitaDTO cita = new CitaDTO();
        cita.setPacienteId(99L);
        cita.setDoctorId(doctorId);
        cita.setEstado("COMPLETADA");

        RemoteApiWrapper<PacienteDTO> pacienteWrapper = new RemoteApiWrapper<>();
        pacienteWrapper.setSuccess(true);
        pacienteWrapper.setData(paciente);

        RemoteApiWrapper<DoctorDTO> doctorWrapper = new RemoteApiWrapper<>();
        doctorWrapper.setSuccess(true);
        doctorWrapper.setData(doctor);

        RemoteApiWrapper<CitaDTO> citaWrapper = new RemoteApiWrapper<>();
        citaWrapper.setSuccess(true);
        citaWrapper.setData(cita);

        // WHEN
        when(tipoLicenciaRepo.findById(tipoId)).thenReturn(Optional.of(tipo));
        when(licenciaRepo.existeLicenciaVigenteEnPeriodo(pacienteId, dto.getFechaInicio(), dto.getFechaFin())).thenReturn(false);
        when(pacienteClient.obtenerPorId(pacienteId)).thenReturn(pacienteWrapper);
        when(doctorClient.obtenerPorId(doctorId)).thenReturn(doctorWrapper);
        when(citaClient.obtenerPorId(citaId)).thenReturn(citaWrapper);

        // THEN
        assertThrows(IllegalStateException.class, () -> {
            licenciaService.emitir(dto, doctorId);
        });

        verify(citaClient, times(1)).obtenerPorId(citaId);
    }

    @Test
    void givenTipoLicenciaNoExiste_whenEmitir_thenThrowResourceNotFoundException() {

        // GIVEN
        Long tipoId = 99L;
        LicenciaRequestDTO dto = new LicenciaRequestDTO();
        dto.setTipoLicenciaId(tipoId);
        dto.setFechaInicio(LocalDate.now().plusDays(1));
        dto.setFechaFin(LocalDate.now().plusDays(2));

        // WHEN
        when(tipoLicenciaRepo.findById(tipoId)).thenReturn(Optional.empty());

        // THEN
        assertThrows(ResourceNotFoundException.class, () -> {
            licenciaService.emitir(dto, 2L);
        });

        verify(tipoLicenciaRepo, times(1)).findById(tipoId);
    }

    @Test
    void givenLicenciaYaAnulada_whenAnular_thenThrowIllegalStateException() {

        // GIVEN
        Long licenciaId = 1L;
        Long doctorId = 2L;

        LicenciaMedica lic = new LicenciaMedica();
        lic.setId(licenciaId);
        lic.setDoctorId(doctorId);
        lic.setEstado("ANULADA");

        // WHEN
        when(licenciaRepo.findById(licenciaId)).thenReturn(Optional.of(lic));

        // THEN
        assertThrows(IllegalStateException.class, () -> {
            licenciaService.anular(licenciaId, doctorId);
        });

        verify(licenciaRepo, times(1)).findById(licenciaId);
    }

    @Test
    void givenIdLicencia_whenAnularNotFound_thenThrowResourceNotFoundException() {

        // GIVEN
        Long licenciaId = 99L;
        Long doctorId = 2L;

        // WHEN
        when(licenciaRepo.findById(licenciaId)).thenReturn(Optional.empty());

        // THEN
        assertThrows(ResourceNotFoundException.class, () -> {
            licenciaService.anular(licenciaId, doctorId);
        });

        verify(licenciaRepo, times(1)).findById(licenciaId);
    }

    

}
