package com.ms.ms_cita.mapper;

import com.ms.ms_cita.dto.CitaRequestDTO;
import com.ms.ms_cita.dto.CitaResponseDTO;
import com.ms.ms_cita.dto.DoctorDTO;
import com.ms.ms_cita.dto.PacienteDTO;
import com.ms.ms_cita.model.Cita;

public class CitaMapper {

    public static CitaResponseDTO toDTO(Cita cita){
        if (cita == null){
            return null;
        }
    

        CitaResponseDTO dto = new CitaResponseDTO();
        dto.setId(cita.getId());
        dto.setPacienteId(cita.getPacienteId());
        dto.setDoctorId(cita.getDoctorId());
        dto.setFechaHoraCita(cita.getFechaHoraCita());
        dto.setMotivoConsulta(cita.getMotivoConsulta());
        dto.setEstado(cita.getEstado());
        dto.setObservaciones(cita.getObservaciones());
        dto.setDuracionMinutos(cita.getDuracionMinutos());
        dto.setFechaCreacion(cita.getFechaCreacion());
        dto.setFechaActualizacion(cita.getFechaActualizacion());
        
        return dto;


    }

    public static Cita toModel(CitaRequestDTO dto){
        if (dto == null) {
            return null;
        }

        Cita cita = new Cita();
        cita.setPacienteId(dto.getPacienteId());
        cita.setDoctorId(dto.getDoctorId());
        cita.setFechaHoraCita(dto.getFechaHoraCita());
        cita.setMotivoConsulta(dto.getMotivoConsulta());
        cita.setDuracionMinutos(dto.getDuracionMinutos());
        cita.setEstado("PENDIENTE");
        return cita;
    }

    public static void agregarDatosPaciente(CitaResponseDTO dto, PacienteDTO paciente){
        if (dto == null || paciente == null) {
            return;
        }

        dto.setPacienteNombre(paciente.getNombre());
        dto.setPacienteApellido(paciente.getApellido());
        dto.setPacienteRut(paciente.getRut());
    }

    public static void agregarDatosDoctor(CitaResponseDTO dto, DoctorDTO doctor){
        if (dto == null || doctor == null){
            return;
        }
        dto.setDoctorNombre(doctor.getNombre());
        dto.setDoctorApellido(doctor.getApellido());
        dto.setDoctorEspecialidad(doctor.getEspecialidadNombre());
    }
        

}
