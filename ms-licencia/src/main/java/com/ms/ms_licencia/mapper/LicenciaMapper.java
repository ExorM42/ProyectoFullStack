package com.ms.ms_licencia.mapper;

import com.ms.ms_licencia.dto.DoctorDTO;
import com.ms.ms_licencia.dto.LicenciaRequestDTO;
import com.ms.ms_licencia.dto.LicenciaResponseDTO;
import com.ms.ms_licencia.dto.PacienteDTO;
import com.ms.ms_licencia.dto.TipoLicenciaResponseDTO;
import com.ms.ms_licencia.model.LicenciaMedica;
import com.ms.ms_licencia.model.TipoLicencia;

public class LicenciaMapper {


    public static LicenciaResponseDTO toDTO(LicenciaMedica licencia){
        if (licencia == null){
            return null;
        }

        LicenciaResponseDTO dto = new LicenciaResponseDTO();
        dto.setId(licencia.getId());
        dto.setFolio(licencia.getFolio());
        dto.setPacienteId(licencia.getPacienteId());
        dto.setDoctorId(licencia.getDoctorId());
        dto.setCitaId(licencia.getCitaId());
        dto.setDiagnostico(licencia.getDiagnostico());
        dto.setFechaInicio(licencia.getFechaInicio());
        dto.setFechaFin(licencia.getFechaFin());
        dto.setDiasReposo(licencia.getDiasReposo());
        dto.setEstado(licencia.getEstado());
        dto.setObservaciones(licencia.getObservaciones());
        dto.setFechaEmision(licencia.getFechaEmision());

        if (licencia.getTipoLicencia() != null){
            dto.setTipoLicenciaId(licencia.getTipoLicencia().getId());
            dto.setTipoLicenciaCodigo(licencia.getTipoLicencia().getCodigo());
            dto.setTipoLicenciaNombre(licencia.getTipoLicencia().getNombre());
        }

        return dto;
    }

    public static TipoLicenciaResponseDTO toDTO(TipoLicencia tipoLicencia){
        if (tipoLicencia == null){
            return null;
        }

        TipoLicenciaResponseDTO dto = new TipoLicenciaResponseDTO();
        dto.setId(tipoLicencia.getId());
        dto.setCodigo(tipoLicencia.getCodigo());
        dto.setNombre(tipoLicencia.getNombre());
        dto.setDescripcion(tipoLicencia.getDescripcion());
        dto.setDiasMaximos(tipoLicencia.getDiasMaximos());

        return dto;
    }

    public static LicenciaMedica toModel(LicenciaRequestDTO dto, TipoLicencia tipoLicencia,String folio, int diasReposo){
            if (dto == null){
                return null;
            }

            LicenciaMedica licencia = new LicenciaMedica();
            licencia.setFolio(folio);
            licencia.setPacienteId(dto.getPacienteId());
            licencia.setDoctorId(dto.getDoctorId());
            licencia.setCitaId(dto.getCitaId());
            licencia.setTipoLicencia(tipoLicencia);
            licencia.setDiagnostico(dto.getDiagnostico());
            licencia.setFechaInicio(dto.getFechaInicio());
            licencia.setFechaFin(dto.getFechaFin());
            licencia.setDiasReposo(diasReposo);
            licencia.setObservaciones(dto.getObservaciones());
            licencia.setEstado("EMITIDA");

            return licencia;


        }

        public static void agregarDatosPaciente(LicenciaResponseDTO dto, PacienteDTO paciente){
            if (dto == null || paciente == null){
                return;
            }

            dto.setPacienteNombre(paciente.getNombre());
            dto.setPacienteApellido(paciente.getApellido());
            dto.setPacienteRut(paciente.getRut());
        }

        public static void agregarDatosDoctor(LicenciaResponseDTO dto, DoctorDTO doctor){
            if(dto == null || doctor == null){
                return;
            }

            dto.setDoctorNombre(doctor.getNombre());
            dto.setDoctorApellido(doctor.getApellido());
            dto.setDoctorEspecialidad(doctor.getEspecialidadNombre());
        }


    

}
