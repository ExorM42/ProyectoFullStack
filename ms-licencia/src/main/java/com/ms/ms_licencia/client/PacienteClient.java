package com.ms.ms_licencia.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ms.ms_licencia.dto.PacienteDTO;
import com.ms.ms_licencia.dto.RemoteApiWrapper;

@FeignClient(name ="ms-paciente", url="${ms.paciente.url}")
public interface PacienteClient {

    @GetMapping("/api/pacientes/{id}")
    RemoteApiWrapper<PacienteDTO> obtenerPorId(@PathVariable("id") Long id);

}
