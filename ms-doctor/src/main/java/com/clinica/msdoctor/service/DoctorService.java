package com.clinica.msdoctor.service;

import com.clinica.msdoctor.dto.DoctorRequestDTO;
import com.clinica.msdoctor.dto.DoctorResponseDTO;
import com.clinica.msdoctor.exception.DuplicateResourceException;
import com.clinica.msdoctor.exception.ResourceNotFoundException;
import com.clinica.msdoctor.model.Doctor;
import com.clinica.msdoctor.model.Especialidad;
import com.clinica.msdoctor.repository.DoctorRepository;
import com.clinica.msdoctor.repository.EspecialidadRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class DoctorService {

    private static final Logger log = LoggerFactory.getLogger(DoctorService.class);
    private static final LocalDate FECHA_MINIMA_CONTRATACION = LocalDate.of(2000, 1, 1);

    private final DoctorRepository doctorRepository;
    private final EspecialidadRepository especialidadRepository;

    public DoctorService(DoctorRepository doctorRepository,
                         EspecialidadRepository especialidadRepository) {
        this.doctorRepository = doctorRepository;
        this.especialidadRepository = especialidadRepository;
    }

    public List<DoctorResponseDTO> obtenerTodos() {
        log.info("[DOCTOR] Consultando todos los doctores activos");
        List<Doctor> doctores = doctorRepository.findByActivoTrue();
        log.info("[DOCTOR] Se encontraron {} doctores activos", doctores.size());
        return doctores.stream().map(this::mapToDTO).toList();
    }

    public DoctorResponseDTO obtenerPorId(Long id) {
        log.info("[DOCTOR] Buscando doctor con ID: {}", id);
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", id));
        return mapToDTO(doctor);
    }

    public DoctorResponseDTO obtenerPorRut(String rut) {
        log.info("[DOCTOR] Buscando doctor con RUT: {}", rut);
        Doctor doctor = doctorRepository.findByRut(rut)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor con RUT " + rut + " no encontrado"));
        return mapToDTO(doctor);
    }

    public List<DoctorResponseDTO> obtenerPorEspecialidad(Long especialidadId) {
        log.info("[DOCTOR] Buscando doctores con especialidadId: {}", especialidadId);
        Especialidad especialidad = obtenerEspecialidadActiva(especialidadId);
        log.info("[DOCTOR] Especialidad encontrada: {}", especialidad.getNombre());
        return doctorRepository.findByEspecialidadId(especialidadId)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Transactional
    public DoctorResponseDTO crear(DoctorRequestDTO dto) {
        validarDatosDoctor(dto);
        log.info("[DOCTOR] Creando doctor con RUT: {}", dto.getRut());

        if (doctorRepository.existsByRut(dto.getRut())) {
            log.warn("[DOCTOR] RUT duplicado: {}", dto.getRut());
            throw new DuplicateResourceException("Ya existe un doctor con el RUT: " + dto.getRut());
        }
        if (doctorRepository.existsByEmail(dto.getEmail())) {
            log.warn("[DOCTOR] Email duplicado: {}", dto.getEmail());
            throw new DuplicateResourceException("Ya existe un doctor con el email: " + dto.getEmail());
        }
        if (doctorRepository.existsByNumeroRegistro(dto.getNumeroRegistro())) {
            log.warn("[DOCTOR] Numero de registro duplicado: {}", dto.getNumeroRegistro());
            throw new DuplicateResourceException("Ya existe un doctor con el numero de registro: " + dto.getNumeroRegistro());
        }

        Especialidad especialidad = obtenerEspecialidadActiva(dto.getEspecialidadId());

        Doctor doctor = new Doctor();
        doctor.setRut(dto.getRut());
        doctor.setNombre(dto.getNombre());
        doctor.setApellido(dto.getApellido());
        doctor.setEmail(dto.getEmail());
        doctor.setTelefono(dto.getTelefono());
        doctor.setNumeroRegistro(dto.getNumeroRegistro());
        doctor.setEspecialidad(especialidad);
        doctor.setFechaContratacion(dto.getFechaContratacion());
        doctor.setActivo(true);

        Doctor guardado = doctorRepository.save(doctor);
        log.info("[DOCTOR] Doctor creado exitosamente con ID: {}", guardado.getId());
        return mapToDTO(guardado);
    }

    @Transactional
    public DoctorResponseDTO actualizar(Long id, DoctorRequestDTO dto) {
        validarDatosDoctor(dto);
        log.info("[DOCTOR] Actualizando doctor con ID: {}", id);

        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", id));

        if (Boolean.FALSE.equals(doctor.getActivo())) {
            log.warn("[DOCTOR] Intento de actualizar doctor inactivo. ID: {}", id);
            throw new IllegalArgumentException("No se puede operar con un doctor inactivo");
        }

        if (!doctor.getRut().equals(dto.getRut()) && doctorRepository.existsByRut(dto.getRut())) {
            log.warn("[DOCTOR] RUT duplicado al actualizar: {}", dto.getRut());
            throw new DuplicateResourceException("Ya existe otro doctor con el RUT: " + dto.getRut());
        }
        if (!doctor.getEmail().equals(dto.getEmail()) && doctorRepository.existsByEmail(dto.getEmail())) {
            log.warn("[DOCTOR] Email duplicado al actualizar: {}", dto.getEmail());
            throw new DuplicateResourceException("Ya existe otro doctor con el email: " + dto.getEmail());
        }
        if (!doctor.getNumeroRegistro().equals(dto.getNumeroRegistro())
                && doctorRepository.existsByNumeroRegistro(dto.getNumeroRegistro())) {
            log.warn("[DOCTOR] Numero de registro duplicado al actualizar: {}", dto.getNumeroRegistro());
            throw new DuplicateResourceException("Ya existe otro doctor con el numero de registro: " + dto.getNumeroRegistro());
        }

        Especialidad especialidad = obtenerEspecialidadActiva(dto.getEspecialidadId());

        doctor.setRut(dto.getRut());
        doctor.setNombre(dto.getNombre());
        doctor.setApellido(dto.getApellido());
        doctor.setEmail(dto.getEmail());
        doctor.setTelefono(dto.getTelefono());
        doctor.setNumeroRegistro(dto.getNumeroRegistro());
        doctor.setEspecialidad(especialidad);
        doctor.setFechaContratacion(dto.getFechaContratacion());

        Doctor actualizado = doctorRepository.save(doctor);
        log.info("[DOCTOR] Doctor actualizado con ID: {}", actualizado.getId());
        return mapToDTO(actualizado);
    }

    @Transactional
    public void desactivar(Long id) {
        log.info("[DOCTOR] Desactivando doctor con ID: {}", id);
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", id));

        if (Boolean.FALSE.equals(doctor.getActivo())) {
            log.warn("[DOCTOR] Doctor con ID {} ya estaba desactivado", id);
        }

        doctor.setActivo(false);
        doctorRepository.save(doctor);
        log.info("[DOCTOR] Doctor con ID {} desactivado", id);
    }

    @Transactional
    public void eliminar(Long id) {
        log.info("[DOCTOR] Eliminando fisicamente doctor con ID: {}", id);
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", id));

        doctorRepository.delete(doctor);
        log.info("[DOCTOR] Doctor con ID {} eliminado fisicamente", id);
    }

    private void validarDatosDoctor(DoctorRequestDTO dto) {
        if (dto == null) {
            log.error("[DOCTOR] DTO de doctor nulo");
            throw new IllegalArgumentException("Los datos del doctor son obligatorios");
        }
        if (dto.getFechaContratacion() == null) {
            log.warn("[DOCTOR] Fecha de contratacion nula");
            throw new IllegalArgumentException("La fecha de contratacion es obligatoria");
        }
        if (dto.getFechaContratacion().isAfter(LocalDate.now())) {
            log.warn("[DOCTOR] Fecha de contratacion futura: {}", dto.getFechaContratacion());
            throw new IllegalArgumentException("La fecha de contratacion no puede ser futura");
        }
        if (dto.getFechaContratacion().isBefore(FECHA_MINIMA_CONTRATACION)) {
            log.warn("[DOCTOR] Fecha de contratacion anterior al minimo permitido: {}", dto.getFechaContratacion());
            throw new IllegalArgumentException("La fecha de contratacion no puede ser anterior a " + FECHA_MINIMA_CONTRATACION);
        }
    }

    private Especialidad obtenerEspecialidadActiva(Long especialidadId) {
        Especialidad especialidad = especialidadRepository.findById(especialidadId)
                .orElseThrow(() -> new ResourceNotFoundException("Especialidad", especialidadId));

        if (Boolean.FALSE.equals(especialidad.getActivo())) {
            log.warn("[DOCTOR] Especialidad inactiva usada en doctor. ID: {}", especialidadId);
            throw new IllegalArgumentException("La especialidad seleccionada no esta activa");
        }

        return especialidad;
    }

    private DoctorResponseDTO mapToDTO(Doctor doctor) {
        DoctorResponseDTO dto = new DoctorResponseDTO();
        dto.setId(doctor.getId());
        dto.setRut(doctor.getRut());
        dto.setNombre(doctor.getNombre());
        dto.setApellido(doctor.getApellido());
        dto.setEmail(doctor.getEmail());
        dto.setTelefono(doctor.getTelefono());
        dto.setNumeroRegistro(doctor.getNumeroRegistro());
        dto.setActivo(doctor.getActivo());
        dto.setFechaContratacion(doctor.getFechaContratacion());
        if (doctor.getEspecialidad() != null) {
            dto.setEspecialidadId(doctor.getEspecialidad().getId());
            dto.setEspecialidadNombre(doctor.getEspecialidad().getNombre());
        }
        return dto;
    }
}
