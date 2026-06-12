package com.ms.ms_auth.dto;

import com.ms.ms_auth.model.Rol;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegisterRequestDTO {

    @NotBlank(message="El email es obligatorio")
    @Email(message="El email debe tener formato valido")
    private String email;

    @NotBlank(message="La contraseña es obligatoria")
    private String password;

    @NotNull(message="El rol es obligatorio")
    private Rol rol;

    private Long pacienteId;
    
    private Long doctorId;

}
