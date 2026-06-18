package com.clinica.mspaciente.service;

import com.clinica.mspaciente.dto.PacienteRequestDTO;
import com.clinica.mspaciente.dto.PacienteResponseDTO;
import com.clinica.mspaciente.exception.DuplicateResourceException;
import com.clinica.mspaciente.exception.ResourceNotFoundException;
import com.clinica.mspaciente.model.Paciente;
import com.clinica.mspaciente.model.Prevision;
import com.clinica.mspaciente.repository.PacienteRepository;
import com.clinica.mspaciente.repository.PrevisionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
public class PacienteService {

    private static final Logger log = LoggerFactory.getLogger(PacienteService.class);
    private static final int EDAD_MAXIMA = 120;

    private final PacienteRepository pacienteRepository;
    private final PrevisionRepository previsionRepository;

    public PacienteService(PacienteRepository pacienteRepository,
                           PrevisionRepository previsionRepository) {
        this.pacienteRepository = pacienteRepository;
        this.previsionRepository = previsionRepository;
    }

    public List<PacienteResponseDTO> obtenerTodos() {
        log.info("[PACIENTE] Consultando todos los pacientes activos");
        List<Paciente> pacientes = pacienteRepository.findByActivoTrue();
        log.info("[PACIENTE] Se encontraron {} pacientes activos", pacientes.size());
        return pacientes.stream().map(this::mapToResponseDTO).toList();
    }

    public PacienteResponseDTO obtenerPorId(Long id) {
        log.info("[PACIENTE] Buscando paciente con ID: {}", id);
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente", id));
        log.info("[PACIENTE] Paciente encontrado con ID: {}", paciente.getId());
        return mapToResponseDTO(paciente);
    }

    public PacienteResponseDTO obtenerPorRut(String rut) {
        log.info("[PACIENTE] Buscando paciente con RUT: {}", rut);
        Paciente paciente = pacienteRepository.findByRut(rut)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente con RUT " + rut + " no encontrado"));
        return mapToResponseDTO(paciente);
    }

    public List<PacienteResponseDTO> obtenerPorEdad(Integer edad) {
        validarEdadBusqueda(edad);
        log.info("[PACIENTE] Buscando pacientes activos con edad: {}", edad);

        List<Paciente> pacientes = pacienteRepository.findByEdadAndActivoTrue(edad);
        if (pacientes.isEmpty()) {
            log.warn("[PACIENTE] No se encontraron pacientes activos con edad: {}", edad);
            throw new ResourceNotFoundException("No se encontraron pacientes activos con edad: " + edad);
        }

        return pacientes.stream().map(this::mapToResponseDTO).toList();
    }

    @Transactional
    public PacienteResponseDTO crear(PacienteRequestDTO dto) {
        validarDatosPaciente(dto);
        log.info("[PACIENTE] Creando nuevo paciente con RUT: {}", dto.getRut());

        if (pacienteRepository.existsByRut(dto.getRut())) {
            log.warn("[PACIENTE] RUT duplicado detectado: {}", dto.getRut());
            throw new DuplicateResourceException("Ya existe un paciente con el RUT: " + dto.getRut());
        }

        if (pacienteRepository.existsByEmail(dto.getEmail())) {
            log.warn("[PACIENTE] Email duplicado detectado: {}", dto.getEmail());
            throw new DuplicateResourceException("Ya existe un paciente con el email: " + dto.getEmail());
        }

        Prevision prevision = obtenerPrevisionActiva(dto.getPrevisionId());

        Paciente paciente = new Paciente();
        paciente.setRut(dto.getRut());
        paciente.setNombre(dto.getNombre());
        paciente.setApellido(dto.getApellido());
        paciente.setFechaNacimiento(dto.getFechaNacimiento());
        paciente.setGenero(dto.getGenero());
        paciente.setEmail(dto.getEmail());
        paciente.setTelefono(dto.getTelefono());
        paciente.setDireccion(dto.getDireccion());
        paciente.setPrevision(prevision);
        paciente.setActivo(true);
        paciente.setEdad(dto.getEdad());
        paciente.setAcompanado(dto.getAcompanado());

        Paciente guardado = pacienteRepository.save(paciente);
        log.info("[PACIENTE] Paciente creado exitosamente con ID: {}", guardado.getId());
        return mapToResponseDTO(guardado);
    }

    @Transactional
    public PacienteResponseDTO actualizar(Long id, PacienteRequestDTO dto) {
        validarDatosPaciente(dto);
        log.info("[PACIENTE] Actualizando paciente con ID: {}", id);

        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente", id));

        if (!paciente.getRut().equals(dto.getRut()) && pacienteRepository.existsByRut(dto.getRut())) {
            log.warn("[PACIENTE] RUT duplicado al actualizar. RUT: {}", dto.getRut());
            throw new DuplicateResourceException("Ya existe otro paciente con el RUT: " + dto.getRut());
        }

        if (!paciente.getEmail().equals(dto.getEmail()) && pacienteRepository.existsByEmail(dto.getEmail())) {
            log.warn("[PACIENTE] Email duplicado al actualizar. Email: {}", dto.getEmail());
            throw new DuplicateResourceException("Ya existe otro paciente con el email: " + dto.getEmail());
        }

        Prevision prevision = obtenerPrevisionActiva(dto.getPrevisionId());

        paciente.setRut(dto.getRut());
        paciente.setNombre(dto.getNombre());
        paciente.setApellido(dto.getApellido());
        paciente.setFechaNacimiento(dto.getFechaNacimiento());
        paciente.setGenero(dto.getGenero());
        paciente.setEmail(dto.getEmail());
        paciente.setTelefono(dto.getTelefono());
        paciente.setDireccion(dto.getDireccion());
        paciente.setPrevision(prevision);
        paciente.setEdad(dto.getEdad());
        paciente.setAcompanado(dto.getAcompanado());

        Paciente actualizado = pacienteRepository.save(paciente);
        log.info("[PACIENTE] Paciente actualizado exitosamente con ID: {}", actualizado.getId());
        return mapToResponseDTO(actualizado);
    }

    @Transactional
    public void desactivar(Long id) {
        log.info("[PACIENTE] Desactivando paciente con ID: {}", id);
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente", id));

        if (Boolean.FALSE.equals(paciente.getActivo())) {
            log.warn("[PACIENTE] Paciente con ID {} ya estaba desactivado", id);
        }

        paciente.setActivo(false);
        pacienteRepository.save(paciente);
        log.info("[PACIENTE] Paciente con ID {} desactivado correctamente", id);
    }

    @Transactional
    public void eliminar(Long id) {
        log.info("[PACIENTE] Eliminando fisicamente paciente con ID: {}", id);
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente", id));

        pacienteRepository.delete(paciente);
        log.info("[PACIENTE] Paciente con ID {} eliminado fisicamente", id);
    }

    public List<PacienteResponseDTO> buscarPorApellido(String apellido) {
        log.info("[PACIENTE] Buscando pacientes por apellido: {}", apellido);
        return pacienteRepository.findByApellidoContainingIgnoreCase(apellido)
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    private void validarDatosPaciente(PacienteRequestDTO dto) {
        if (dto == null) {
            log.error("[PACIENTE] DTO de paciente nulo");
            throw new IllegalArgumentException("Los datos del paciente son obligatorios");
        }

        LocalDate hoy = LocalDate.now();
        if (dto.getFechaNacimiento() == null) {
            log.warn("[PACIENTE] Fecha de nacimiento nula");
            throw new IllegalArgumentException("La fecha de nacimiento es obligatoria");
        }
        if (!dto.getFechaNacimiento().isBefore(hoy)) {
            log.warn("[PACIENTE] Fecha de nacimiento invalida: {}", dto.getFechaNacimiento());
            throw new IllegalArgumentException("La fecha de nacimiento debe ser anterior a la fecha actual");
        }
        if (dto.getFechaNacimiento().isBefore(LocalDate.of(1900, 1, 1))) {
            log.warn("[PACIENTE] Fecha de nacimiento muy antigua: {}", dto.getFechaNacimiento());
            throw new IllegalArgumentException("La fecha de nacimiento no puede ser anterior al año 1900");
        }

        int edadCalculada = Period.between(dto.getFechaNacimiento(), hoy).getYears();
        if (edadCalculada < 0 || edadCalculada > EDAD_MAXIMA) {
            log.warn("[PACIENTE] Edad calculada fuera de rango: {}", edadCalculada);
            throw new IllegalArgumentException("La edad debe estar entre 0 y " + EDAD_MAXIMA + " años");
        }
        if (dto.getEdad() == null || dto.getEdad() != edadCalculada) {
            log.warn("[PACIENTE] Edad declarada {} no coincide con edad calculada {}", dto.getEdad(), edadCalculada);
            throw new IllegalArgumentException("La edad debe coincidir con la fecha de nacimiento");
        }
        if ((edadCalculada < 18 || edadCalculada > 80) && Boolean.FALSE.equals(dto.getAcompanado())) {
            log.warn("[PACIENTE] Paciente menor o mayor a 80 sin acompanante. RUT: {}", dto.getRut());
            throw new IllegalArgumentException("Pacientes menores de edad o mayores de 80 años deben venir acompañados");
        }
    }

    private void validarEdadBusqueda(Integer edad) {
        if (edad == null || edad < 0 || edad > EDAD_MAXIMA) {
            log.warn("[PACIENTE] Edad de busqueda invalida: {}", edad);
            throw new IllegalArgumentException("La edad debe estar entre 0 y " + EDAD_MAXIMA);
        }
    }

    private Prevision obtenerPrevisionActiva(Long previsionId) {
        Prevision prevision = previsionRepository.findById(previsionId)
                .orElseThrow(() -> new ResourceNotFoundException("Prevision", previsionId));

        if (Boolean.FALSE.equals(prevision.getActivo())) {
            log.warn("[PACIENTE] Prevision inactiva usada en paciente. ID: {}", previsionId);
            throw new IllegalArgumentException("La prevision seleccionada no esta activa");
        }

        return prevision;
    }

    private PacienteResponseDTO mapToResponseDTO(Paciente paciente) {
        PacienteResponseDTO dto = new PacienteResponseDTO();
        dto.setId(paciente.getId());
        dto.setRut(paciente.getRut());
        dto.setNombre(paciente.getNombre());
        dto.setApellido(paciente.getApellido());
        dto.setFechaNacimiento(paciente.getFechaNacimiento());
        dto.setGenero(paciente.getGenero());
        dto.setEmail(paciente.getEmail());
        dto.setTelefono(paciente.getTelefono());
        dto.setDireccion(paciente.getDireccion());
        dto.setActivo(paciente.getActivo());
        dto.setFechaRegistro(paciente.getFechaRegistro());
        dto.setEdad(paciente.getEdad());
        dto.setAcompanado(paciente.getAcompanado());

        if (paciente.getPrevision() != null) {
            dto.setPrevisionId(paciente.getPrevision().getId());
            dto.setPrevisionNombre(paciente.getPrevision().getNombre());
            dto.setPrevisionTipo(paciente.getPrevision().getTipo());
        }

        return dto;
    }
}
