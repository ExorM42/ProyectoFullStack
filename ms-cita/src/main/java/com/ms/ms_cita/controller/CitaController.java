package com.ms.ms_cita.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ms.ms_cita.dto.ActualizarEstadoCitaDTO;
import com.ms.ms_cita.dto.ApiResponseDTO;
import com.ms.ms_cita.dto.CitaRequestDTO;
import com.ms.ms_cita.dto.CitaResponseDTO;
import com.ms.ms_cita.service.CitaService;

import jakarta.validation.Valid;



@RestController
@RequestMapping("/api/citas")
public class CitaController {

    private final CitaService citaService;

    public CitaController(CitaService citaService){
        this.citaService = citaService;
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<CitaResponseDTO>>> obtenerTodasLasCitas(){
        return ResponseEntity.ok(ApiResponseDTO.ok("Citas obtenidas: ", citaService.obtenerTodas()));
        
    }

    @GetMapping("/hoy")
    public ResponseEntity<ApiResponseDTO<List<CitaResponseDTO>>> obtenerCitasHoy() {
        return ResponseEntity.ok(ApiResponseDTO.ok("Citas de hoy :", citaService.obtenerCitasHoy()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<CitaResponseDTO>> obtenerCitaPorId(@PathVariable Long id){
        return ResponseEntity.ok(ApiResponseDTO.ok("Cita encontrada: ", citaService.obtenerPorId(id)));
    }

    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<ApiResponseDTO<List<CitaResponseDTO>>> obtenerPorPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(ApiResponseDTO.ok("Citas de Paciente: ", citaService.obtenerPorPaciente(pacienteId)));
        
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<ApiResponseDTO<List<CitaResponseDTO>>> obtenerPorDoctor(@PathVariable Long doctorId) {

        return ResponseEntity.ok(ApiResponseDTO.ok("Citas del doctor : ", citaService.obtenerPorDoctor(doctorId)));
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<CitaResponseDTO>> crearCita(@Valid @RequestBody CitaRequestDTO dto) {
        CitaResponseDTO citaCreada = citaService.crearCita(dto);
       return ResponseEntity.ok(ApiResponseDTO.ok("Cita agendad de manera exitosa", citaCreada ));
    }

    @PatchMapping("{id}/estado")
    public ResponseEntity<ApiResponseDTO<CitaResponseDTO>> actualizarEstado(@PathVariable Long id, @Valid @RequestBody ActualizarEstadoCitaDTO dto){
        return ResponseEntity.ok(ApiResponseDTO.ok("Estado de cita actualizado de amanera exitosa", citaService.actualizarEstado(id, dto)));
    }


    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<ApiResponseDTO<Void>> cancelarCita(@PathVariable Long id){
        citaService.cancelar(id);
        return ResponseEntity.ok(ApiResponseDTO.ok("Cita cancelada de manera exitosa", null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<CitaResponseDTO>> eliminarCita(@PathVariable Long id){
        citaService.eliminar(id);
        return ResponseEntity.ok(ApiResponseDTO.ok("Cita eliminada de manera exitosa", null));
    }

    
    
    
    
    
    

}
