package com.ms.ms_licencia.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ms.ms_licencia.dto.CitaDTO;
import com.ms.ms_licencia.dto.RemoteApiWrapper;

@FeignClient(name = "ms-cita", url = "${ms.cita.url}")
public interface CitaClient {

    @GetMapping("/api/citas/{id}")
    RemoteApiWrapper<CitaDTO> obtenerPorId(@PathVariable("id") Long id);

}
