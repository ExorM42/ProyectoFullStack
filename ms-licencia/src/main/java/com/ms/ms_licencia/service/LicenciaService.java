package com.ms.ms_licencia.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

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
import com.ms.ms_licencia.mapper.LicenciaMapper;
import com.ms.ms_licencia.model.LicenciaMedica;
import com.ms.ms_licencia.model.TipoLicencia;
import com.ms.ms_licencia.repository.LicenciaMedicaRepository;
import com.ms.ms_licencia.repository.TipoLicenciaRepository;

import feign.FeignException;
import jakarta.transaction.Transactional;

@Service
public class LicenciaService {

    private final LicenciaMedicaRepository licenciaRepository;
    private final TipoLicenciaRepository tipoLicenciaRepository;
    private final PacienteClient pacienteClient;
    private final DoctorClient doctorClient;
    private final CitaClient citaClient;

    public LicenciaService(LicenciaMedicaRepository licenciaRepository, TipoLicenciaRepository tipoLicenciaRepository,
        PacienteClient pacienteClient, DoctorClient doctorClient, CitaClient citaClient){
            this.licenciaRepository = licenciaRepository;
            this.tipoLicenciaRepository = tipoLicenciaRepository;
            this.pacienteClient = pacienteClient;
            this.doctorClient = doctorClient;
            this.citaClient = citaClient;
        }


        private PacienteDTO obtenerPaciente(Long pacienteId){
            try {
                RemoteApiWrapper<PacienteDTO> wrapper = pacienteClient.obtenerPorId(pacienteId);

                if (wrapper == null || !wrapper.isSuccess() || wrapper.getData() == null){
                    throw new ResourceNotFoundException("Paciente con ID" + pacienteId + "no fue encontrado");
                }
                if(Boolean.FALSE.equals(wrapper.getData().getActivo())){
                    throw new IllegalStateException("El paciente con ID " + pacienteId + "no se encuentra activo");
                }
                return wrapper.getData();
            } catch (FeignException.NotFound e) {

                throw new ResourceNotFoundException("Paciente con ID " + pacienteId + " no encontrado en microservicio");
            }
        }

        private DoctorDTO obtenerDoctor(Long doctorId){
            try{
                RemoteApiWrapper<DoctorDTO> wrapper = doctorClient.obtenerPorId(doctorId);

                if(wrapper == null || !wrapper.isSuccess() || wrapper.getData() == null){
                    throw new ResourceNotFoundException("Doctor con ID " + doctorId + " no fue encontrado");
                }
                if(Boolean.FALSE.equals(wrapper.getData().getActivo())){
                    throw new IllegalStateException("Doctor con ID " + doctorId + " no se encuentra activo en el sistema");
                }
                return wrapper.getData();
            }catch (FeignException.NotFound e){
                throw new ResourceNotFoundException("Doctor con ID" + doctorId + " no pudo ser encontrado en microservicio");
            }



        }

        private CitaDTO obtenerCita(Long citaId){
            try {
                RemoteApiWrapper<CitaDTO> wrapper = citaClient.obtenerPorId(citaId);

                if (wrapper == null || !wrapper.isSuccess() || wrapper.getData() == null){
                    throw new ResourceNotFoundException("Cita con ID " + citaId + " no pudo ser encontrada");
                }
                return wrapper.getData();
            } catch (FeignException.NotFound e) {
                throw new ResourceNotFoundException("Cita con ID " + citaId + " no encontrada en microservicio");
            }
        }

        private void validarCita(LicenciaRequestDTO requestDTO, Long doctorId){
            if(requestDTO.getCitaId() == null){
                return;
            }
            CitaDTO cita = obtenerCita(requestDTO.getCitaId());

            if (!requestDTO.getPacienteId().equals(cita.getPacienteId())){
                throw new IllegalStateException("La cita no esta asociada a dicho paciente");
            }

            if (!doctorId.equals(cita.getDoctorId())){
                throw new IllegalStateException("La cita no esta asociada a dicho doctor");
            }

            if ("CANCELADA".equals(cita.getEstado()) || "NO ASISTIO".equals(cita.getEstado())){
                throw new IllegalStateException("No se puede eimitir una licencia para una cita con estado: " + cita.getEstado());
            }
        }

        private String generarFolio(){
            String fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            int numero = ThreadLocalRandom.current().nextInt(1000, 9999);
            String folio = "LIC-" + fecha + "-" + numero;

            while (licenciaRepository.existsByFolio(folio)){
                numero = ThreadLocalRandom.current().nextInt(1000, 9999);
                folio = "LIC-" + fecha + "-" + numero;
            }
            return folio;
        }
        

        public List<LicenciaResponseDTO> obtenerTodas(){
            return licenciaRepository.findAll().stream().map(LicenciaMapper::toDTO).collect(Collectors.toList());
        }

        public LicenciaResponseDTO obtenerPorId(Long id){
            LicenciaMedica licencia = licenciaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Licencia", id));
            LicenciaResponseDTO dto = LicenciaMapper.toDTO(licencia);
            datosRemotos(dto, licencia.getPacienteId(), licencia.getDoctorId());
            
            return dto;
            
        }

        public LicenciaResponseDTO obtenerPorFolio(String folio){
            LicenciaMedica licencia = licenciaRepository.findByFolio(folio).orElseThrow(() -> new ResourceNotFoundException("Licencia con folio " + folio + "no encontrada"));
            LicenciaResponseDTO dto = LicenciaMapper.toDTO(licencia);
            datosRemotos(dto, licencia.getPacienteId(), licencia.getDoctorId());

            return dto;
        }

        public List<LicenciaResponseDTO> obtenerPorDoctor(Long doctorId){
            return licenciaRepository.findByDoctorId(doctorId).stream().map(LicenciaMapper::toDTO).collect(Collectors.toList());
        }

        public List<LicenciaResponseDTO> obtenerPorPaciente(Long pacienteId){
            return licenciaRepository.findByPacienteId(pacienteId).stream().map(LicenciaMapper::toDTO).collect(Collectors.toList());
        }

        @Transactional
        public LicenciaResponseDTO emitir(LicenciaRequestDTO requestDTO, Long doctorId){

            if(!requestDTO.getFechaFin().isAfter(requestDTO.getFechaInicio())){
                throw new IllegalStateException("La fecha de fin debe ser posterior a la fecha de inicio");
            }

            long diasReposo = ChronoUnit.DAYS.between(requestDTO.getFechaInicio(), requestDTO.getFechaFin()) + 1;
            TipoLicencia tipoLicencia = tipoLicenciaRepository.findById(requestDTO.getTipoLicenciaId()).orElseThrow(() -> new ResourceNotFoundException("TipoLicencia", requestDTO.getTipoLicenciaId()));

            if (diasReposo > tipoLicencia.getDiasMaximos()){
                throw new LicenciaConflictoException("Los dias de reposo solicitados superan el maximo permitido");
            }

            if(licenciaRepository.existeLicenciaVigenteEnPeriodo(requestDTO.getPacienteId(), requestDTO.getFechaInicio(), requestDTO.getFechaFin())){
                throw new LicenciaConflictoException("El paciente se encuentra con licencia vigente por lo que entra en conflicto con la licencia a emitir");
            }

            PacienteDTO paciente = obtenerPaciente(requestDTO.getPacienteId());
            DoctorDTO doctor = obtenerDoctor(doctorId);

            validarCita(requestDTO, doctorId);

            int diasAcumulados = licenciaRepository.totalDiasReposoAnio(requestDTO.getPacienteId(), LocalDate.now().getYear());
            if (diasAcumulados + diasReposo > 60){
                throw new LicenciaConflictoException("El paciente supera el maximo de dias de licencia");
            }

            String folio = generarFolio();

            LicenciaMedica licencia = LicenciaMapper.toModel(requestDTO, tipoLicencia, folio, (int)diasReposo, doctorId);

            LicenciaMedica guardada = licenciaRepository.save(licencia);

            LicenciaResponseDTO  responseDTO = LicenciaMapper.toDTO(guardada);
            LicenciaMapper.agregarDatosPaciente(responseDTO, paciente);
            LicenciaMapper.agregarDatosDoctor(responseDTO, doctor);
            return responseDTO;
        }
        @Transactional
        public LicenciaResponseDTO actualizarEstado(Long id, ActualizarEstadoLicenciaDTO dto, Long doctorId) {
            LicenciaMedica licencia = licenciaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Licencia", id));

            if (!licencia.getDoctorId().equals(doctorId)) {
                throw new IllegalStateException("No puedes modificar una licencia emitida por otro doctor");
            }

            if ("ANULADA".equals(licencia.getEstado())) {
                throw new IllegalStateException("No se puede modificar una licencia en estado ANULADA");
            }

            licencia.setEstado(dto.getEstado());
            if (dto.getObservaciones() != null && !dto.getObservaciones().isBlank()) {
                licencia.setObservaciones(dto.getObservaciones());
            }

            LicenciaMedica actualizada = licenciaRepository.save(licencia);
            LicenciaResponseDTO responseDTO = LicenciaMapper.toDTO(actualizada);
            datosRemotos(responseDTO, actualizada.getPacienteId(), actualizada.getDoctorId());
            return responseDTO;
        }

        @Transactional
        public void anular(Long id, Long doctorId) {
            LicenciaMedica licencia = licenciaRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Licencia", id));

            if (!licencia.getDoctorId().equals(doctorId)) {
                throw new IllegalStateException("No puedes anular una licencia emitida por otro doctor");
            }
            if ("ANULADA".equals(licencia.getEstado())) {
                throw new IllegalStateException("La licencia ya se encuentra anulada");
            }
            licencia.setEstado("ANULADA");
            licenciaRepository.save(licencia);
        }

        @Transactional
        public void eliminar(Long id) {
            if (!licenciaRepository.existsById(id)) {
                throw new ResourceNotFoundException("Licencia", id);
            }
            licenciaRepository.deleteById(id);
        }

        private void datosRemotos(LicenciaResponseDTO dto, Long pacienteId, Long doctorId){
            try{
                PacienteDTO paciente = obtenerPaciente(pacienteId);
                LicenciaMapper.agregarDatosPaciente(dto, paciente);
            }catch(Exception e){

            }

            try {
                DoctorDTO doctor = obtenerDoctor(doctorId);
                LicenciaMapper.agregarDatosDoctor(dto, doctor);
            } catch (Exception e) {
            }
        }
    

}
