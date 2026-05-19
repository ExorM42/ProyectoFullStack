package com.ms.ms_licencia.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ms.ms_licencia.dto.DoctorDTO;
import com.ms.ms_licencia.dto.RemoteApiWrapper;

@FeignClient(name = "ms-doctor", url="${ms.doctor.url}")
public interface DoctorClient {

    @GetMapping("/api/doctores/{id}")
    RemoteApiWrapper<DoctorDTO> obtenerPorId(@PathVariable("id") Long id);

}
