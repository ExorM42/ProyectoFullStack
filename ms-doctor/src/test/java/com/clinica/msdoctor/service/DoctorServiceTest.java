package com.clinica.msdoctor.service;

import com.clinica.msdoctor.dto.DoctorRequestDTO;
import com.clinica.msdoctor.dto.DoctorResponseDTO;
import com.clinica.msdoctor.exception.DuplicateResourceException;
import com.clinica.msdoctor.exception.ResourceNotFoundException;
import com.clinica.msdoctor.model.Doctor;
import com.clinica.msdoctor.model.Especialidad;
import com.clinica.msdoctor.repository.DoctorRepository;
import com.clinica.msdoctor.repository.EspecialidadRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
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
class DoctorServiceTest {

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private EspecialidadRepository especialidadRepository;

    @InjectMocks
    private DoctorService doctorService;

    @Test
    void obtenerTodosRetornaDoctoresActivos() {
        when(doctorRepository.findByActivoTrue()).thenReturn(List.of(doctorExistente()));

        List<DoctorResponseDTO> response = doctorService.obtenerTodos();

        assertEquals(1, response.size());
        assertEquals("11222333-4", response.get(0).getRut());
    }

    @Test
    void obtenerPorRutRetornaDoctor() {
        Doctor doctor = doctorExistente();
        when(doctorRepository.findByRut("11222333-4")).thenReturn(Optional.of(doctor));

        DoctorResponseDTO response = doctorService.obtenerPorRut("11222333-4");

        assertEquals(doctor.getId(), response.getId());
        assertEquals(doctor.getRut(), response.getRut());
    }

    @Test
    void obtenerPorEspecialidadRetornaDoctores() {
        when(especialidadRepository.findById(1L)).thenReturn(Optional.of(especialidadActiva()));
        when(doctorRepository.findByEspecialidadId(1L)).thenReturn(List.of(doctorExistente()));

        List<DoctorResponseDTO> response = doctorService.obtenerPorEspecialidad(1L);

        assertEquals(1, response.size());
        assertEquals("Medicina General", response.get(0).getEspecialidadNombre());
    }

    @Test
    void crearDoctorCorrectamenteGuardaDoctor() {
        DoctorRequestDTO dto = doctorRequest();
        Especialidad especialidad = especialidadActiva();

        when(doctorRepository.existsByRut(dto.getRut())).thenReturn(false);
        when(doctorRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(doctorRepository.existsByNumeroRegistro(dto.getNumeroRegistro())).thenReturn(false);
        when(especialidadRepository.findById(dto.getEspecialidadId())).thenReturn(Optional.of(especialidad));
        when(doctorRepository.save(any(Doctor.class))).thenAnswer(invocation -> {
            Doctor doctor = invocation.getArgument(0);
            doctor.setId(7L);
            return doctor;
        });

        DoctorResponseDTO response = doctorService.crear(dto);

        assertEquals(7L, response.getId());
        assertEquals(dto.getRut(), response.getRut());
        assertEquals(especialidad.getId(), response.getEspecialidadId());
        verify(doctorRepository).save(any(Doctor.class));
    }

    @Test
    void crearDoctorConRutDuplicadoLanzaDuplicateResourceException() {
        DoctorRequestDTO dto = doctorRequest();

        when(doctorRepository.existsByRut(dto.getRut())).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> doctorService.crear(dto));
    }

    @Test
    void crearDoctorConEmailDuplicadoLanzaDuplicateResourceException() {
        DoctorRequestDTO dto = doctorRequest();
        when(doctorRepository.existsByRut(dto.getRut())).thenReturn(false);
        when(doctorRepository.existsByEmail(dto.getEmail())).thenReturn(true);
        
        assertThrows(DuplicateResourceException.class, () -> doctorService.crear(dto));
    }

    @Test
    void crearDoctorConEspecialidadInexistenteLanzaResourceNotFoundException() {
        DoctorRequestDTO dto = doctorRequest();
        when(doctorRepository.existsByRut(dto.getRut())).thenReturn(false);
        when(doctorRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(doctorRepository.existsByNumeroRegistro(dto.getNumeroRegistro())).thenReturn(false);
        when(especialidadRepository.findById(dto.getEspecialidadId())).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> doctorService.crear(dto));
    }

    @Test
    void crearDoctorConFechaFuturaLanzaIllegalArgumentException() {
        DoctorRequestDTO dto = doctorRequest();
        dto.setFechaContratacion(LocalDate.now().plusDays(1));

        assertThrows(IllegalArgumentException.class, () -> doctorService.crear(dto));
        verifyNoInteractions(doctorRepository, especialidadRepository);
    }

    @Test
    void crearDoctorConFechaAnteriorAlMinimoLanzaIllegalArgumentException() {
        DoctorRequestDTO dto = doctorRequest();
        dto.setFechaContratacion(LocalDate.of(1999, 12, 31));

        assertThrows(IllegalArgumentException.class, () -> doctorService.crear(dto));
        verifyNoInteractions(doctorRepository, especialidadRepository);
    }

    @Test
    void crearDoctorConEspecialidadInactivaLanzaIllegalArgumentException() {
        DoctorRequestDTO dto = doctorRequest();
        Especialidad especialidad = especialidadActiva();
        especialidad.setActivo(false);

        when(doctorRepository.existsByRut(dto.getRut())).thenReturn(false);
        when(doctorRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(doctorRepository.existsByNumeroRegistro(dto.getNumeroRegistro())).thenReturn(false);
        when(especialidadRepository.findById(dto.getEspecialidadId())).thenReturn(Optional.of(especialidad));

        assertThrows(IllegalArgumentException.class, () -> doctorService.crear(dto));
    }

    @Test
    void actualizarDoctorCorrectamenteGuardaCambios() {
        DoctorRequestDTO dto = doctorRequest();
        dto.setApellido("Gomez Rios");
        Doctor doctor = doctorExistente();

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(especialidadRepository.findById(dto.getEspecialidadId())).thenReturn(Optional.of(especialidadActiva()));
        when(doctorRepository.save(any(Doctor.class))).thenAnswer(invocation -> invocation.getArgument(0));

        DoctorResponseDTO response = doctorService.actualizar(1L, dto);

        assertEquals("Gomez Rios", response.getApellido());
        verify(doctorRepository).save(any(Doctor.class));
    }

    @Test
    void actualizarDoctorInactivoLanzaIllegalArgumentException() {
        Doctor doctor = doctorExistente();
        doctor.setActivo(false);
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        
        DoctorRequestDTO dto = doctorRequest();
        assertThrows(IllegalArgumentException.class, () -> doctorService.actualizar(1L, dto));
    }

    @Test
    void actualizarConRutDuplicadoLanzaDuplicateResourceException() {
        DoctorRequestDTO dto = doctorRequest();
        dto.setRut("22333444-5");
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctorExistente()));
        when(doctorRepository.existsByRut(dto.getRut())).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> doctorService.actualizar(1L, dto));
    }

    @Test
    void actualizarConEmailDuplicadoLanzaDuplicateResourceException() {
        DoctorRequestDTO dto = doctorRequest();
        dto.setEmail("otro@clinica.local");
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctorExistente()));
        when(doctorRepository.existsByEmail(dto.getEmail())).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> doctorService.actualizar(1L, dto));
    }

    @Test
    void actualizarConNumeroRegistroDuplicadoLanzaDuplicateResourceException() {
        DoctorRequestDTO dto = doctorRequest();
        dto.setNumeroRegistro("REG-MED-2000");
        Doctor doctor = doctorExistente();

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(doctorRepository.existsByNumeroRegistro(dto.getNumeroRegistro())).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> doctorService.actualizar(1L, dto));
    }

    @Test
    void obtenerPorIdNoEncontradoLanzaResourceNotFoundException() {
        when(doctorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> doctorService.obtenerPorId(99L));
    }

    @Test
    void desactivarDoctorCambiaActivoFalse() {
        Doctor doctor = doctorExistente();
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));

        doctorService.desactivar(1L);

        ArgumentCaptor<Doctor> captor = ArgumentCaptor.forClass(Doctor.class);
        verify(doctorRepository).save(captor.capture());
        assertFalse(captor.getValue().getActivo());
    }

    @Test
    void eliminarDoctorEliminaEntidadExistente() {
        Doctor doctor = doctorExistente();
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));

        doctorService.eliminar(1L);

        verify(doctorRepository).delete(doctor);
    }

    private DoctorRequestDTO doctorRequest() {
        DoctorRequestDTO dto = new DoctorRequestDTO();
        dto.setRut("11222333-4");
        dto.setNombre("Ana");
        dto.setApellido("Gomez");
        dto.setEmail("ana.gomez@clinica.local");
        dto.setTelefono("+56987654321");
        dto.setNumeroRegistro("REG-MED-1001");
        dto.setEspecialidadId(1L);
        dto.setFechaContratacion(LocalDate.of(2022, 3, 1));
        return dto;
    }

    private Doctor doctorExistente() {
        Doctor doctor = new Doctor();
        doctor.setId(1L);
        doctor.setRut("11222333-4");
        doctor.setNombre("Ana");
        doctor.setApellido("Gomez");
        doctor.setEmail("ana.gomez@clinica.local");
        doctor.setTelefono("+56987654321");
        doctor.setNumeroRegistro("REG-MED-1001");
        doctor.setEspecialidad(especialidadActiva());
        doctor.setActivo(true);
        doctor.setFechaContratacion(LocalDate.of(2022, 3, 1));
        return doctor;
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
