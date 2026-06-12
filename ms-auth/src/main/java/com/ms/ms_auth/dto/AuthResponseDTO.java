package com.ms.ms_auth.dto;

import com.ms.ms_auth.model.Rol;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponseDTO {

    private String token;
    private Rol rol;
    private Long pacienteId;
    private Long doctorId;

}
