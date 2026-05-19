package com.ms.ms_cita.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ms.ms_cita.client.DoctorClient;
import com.ms.ms_cita.client.PacienteClient;
import com.ms.ms_cita.dto.ActualizarEstadoCitaDTO;
import com.ms.ms_cita.dto.CitaRequestDTO;
import com.ms.ms_cita.dto.CitaResponseDTO;
import com.ms.ms_cita.dto.DoctorDTO;
import com.ms.ms_cita.dto.PacienteDTO;
import com.ms.ms_cita.dto.RemoteApiWrapper;
import com.ms.ms_cita.exception.HorarioOcupadoException;
import com.ms.ms_cita.exception.ResourceNotFoundException;
import com.ms.ms_cita.mapper.CitaMapper;
import com.ms.ms_cita.model.Cita;
import com.ms.ms_cita.repository.CitaRepository;

import feign.FeignException;
import jakarta.transaction.Transactional;

@Service
public class CitaService {


    private final CitaRepository citaRepository;
    private final PacienteClient pacienteClient;
    private final DoctorClient doctorClient;

    public CitaService(CitaRepository citaRepository,
                        PacienteClient pacienteClient,
                        DoctorClient doctorClient){
        this.citaRepository = citaRepository;
        this.pacienteClient = pacienteClient;
        this.doctorClient = doctorClient;
     }

     private PacienteDTO obtenerPaciente(Long pacienteId){
        try {
            RemoteApiWrapper<PacienteDTO> wrapper = pacienteClient.obtenerPorId(pacienteId);
            if  (wrapper == null || !wrapper.isSuccess() || wrapper.getData() == null) {
                throw new ResourceNotFoundException("Paciente con ID " +  pacienteId + "no existe");
            }

            if  (Boolean.FALSE.equals(wrapper.getData().getActivo())){
                throw new IllegalStateException("Paciente con ID" + pacienteId + "está inactivo, por lo que no puede agendar citas");

            }
            return wrapper.getData();
        } catch (FeignException.NotFound e){
            throw new ResourceNotFoundException("Paciente con ID " + pacienteId + "no encontrado");
            
        }
     }

     private DoctorDTO obtenerDoctor(Long doctorId){
        try {
            RemoteApiWrapper<DoctorDTO> wrapper = doctorClient.obtenerPorId(doctorId);
            if  (wrapper == null || !wrapper.isSuccess() || wrapper.getData() == null){
                throw new ResourceNotFoundException("Doctor con ID " + doctorId + "no existe");
            }
            if (Boolean.FALSE.equals(wrapper.getData().getActivo())){
                throw new IllegalStateException("Doctor con ID " + doctorId + "no se encuentra activo, por lo que no puede recibir citas");

            }
            return wrapper.getData();
            
        } catch (FeignException e) {
            throw new ResourceNotFoundException("Doctor con ID" + doctorId + "no existe");
        }
     }

     public List<CitaResponseDTO> obtenerTodas(){
        return citaRepository.findAll().stream().map(CitaMapper::toDTO).collect(Collectors.toList());
        
     }

     public CitaResponseDTO obtenerPorId(Long id){
        Optional<Cita> citaOptional = citaRepository.findById(id);
        
        if  (citaOptional.isEmpty()){
            throw new ResourceNotFoundException("Cita", id);
        }

        Cita cita = citaOptional.get();

        CitaResponseDTO dto = CitaMapper.toDTO(cita);
        ObtenerDatosPyD(dto, cita.getPacienteId(), cita.getDoctorId());

        return dto;

    }

    public List<CitaResponseDTO> obtenerPorPaciente(Long pacienteId){
        return citaRepository.findByPacienteId(pacienteId).stream().map(CitaMapper::toDTO).collect(Collectors.toList());

    }

    public List<CitaResponseDTO> obtenerPorDoctor(Long doctorId){
        return citaRepository.findByDoctorId(doctorId).stream().map(CitaMapper::toDTO).collect(Collectors.toList());
    }

    public List<CitaResponseDTO> obtenerCitasHoy(){
        LocalDateTime inicioDia = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime finDia = inicioDia.plusDays(1);
        return citaRepository.findCitasHoy(inicioDia, finDia).stream().map(CitaMapper::toDTO).collect(Collectors.toList());

    }

    @Transactional
    public CitaResponseDTO crearCita(CitaRequestDTO requestDTO){
        PacienteDTO paciente = obtenerPaciente(requestDTO.getPacienteId());
        DoctorDTO doctor = obtenerDoctor(requestDTO.getDoctorId());
        LocalDateTime inicio = requestDTO.getFechaHoraCita();
        LocalDateTime fin = inicio.plusMinutes(requestDTO.getDuracionMinutos());

        if  (citaRepository.existeCitaEnHorario(requestDTO.getDoctorId(), inicio, fin)){
            throw new HorarioOcupadoException("El Doctor " + doctor.getNombre() + " " + doctor.getApellido() + "ya tiene una cita en ese horario");
        }

        if  (inicio.isAfter(LocalDateTime.now().plusDays(60))){
            throw new IllegalStateException("No se puede agendar cita con mas de 60 días de anticipación");
        }

        Cita cita = CitaMapper.toModel(requestDTO);

        Cita guardar = citaRepository.save(cita);
        CitaResponseDTO responseDTO = CitaMapper.toDTO(guardar);
        CitaMapper.agregarDatosPaciente(responseDTO, paciente);
        CitaMapper.agregarDatosDoctor(responseDTO, doctor);
        return responseDTO;
    }

    @Transactional
    public CitaResponseDTO actualizarEstado(Long id, ActualizarEstadoCitaDTO dto){
        Cita cita = citaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cita", id));
        if ("COMPLETADA".equals(cita.getEstado()) || "CANCELADA".equals(cita.getEstado())){
            throw new IllegalStateException("No se puede cambiar el estado de una cita en estado : " + cita.getEstado());
        }
        cita.setEstado(dto.getEstado());
        if (dto.getObservaciones() != null && !dto.getObservaciones().isBlank()){
            cita.setObservaciones(dto.getObservaciones());
        }
        Cita actualizada = citaRepository.save(cita);
        return CitaMapper.toDTO(actualizada);
    }

    @Transactional
    public void cancelar(Long id){
        
        Cita cita = citaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("cita", id));
        if ("COMPLETADA".equals(cita.getEstado())){
            throw new IllegalStateException("No se puede cancelar una cita ya completada");
        }
        cita.setEstado("CANCELADA");
        citaRepository.save(cita);
    }

    @Transactional
    public void eliminar(Long id){
        if (!citaRepository.existsById(id)){
            throw new ResourceNotFoundException("Cita", id);
        }
        citaRepository.deleteById(id);
    }




    private void ObtenerDatosPyD(CitaResponseDTO dto, Long pacienteId, Long doctorId){
        try {
            PacienteDTO paciente = obtenerPaciente(pacienteId);
            CitaMapper.agregarDatosPaciente(dto, paciente);           
        } catch (Exception e) {
        }
        try {
            DoctorDTO doctor = obtenerDoctor(doctorId);
            CitaMapper.agregarDatosDoctor(dto, doctor);
        } catch (Exception e) {
        }
    }

}
