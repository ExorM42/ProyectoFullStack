package com.clinica.msdoctor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "Datos necesarios para crear o actualizar un doctor.")
public class DoctorRequestDTO {

    @NotBlank(message = "El RUT es obligatorio")
    @Pattern(regexp = "^[0-9]{7,8}-[0-9Kk]$", message = "El RUT debe tener formato 12345678-9")
    @Schema(description = "RUT chileno del doctor.", example = "11222333-4", requiredMode = Schema.RequiredMode.REQUIRED)
    private String rut;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Schema(description = "Nombre del doctor.", example = "Ana", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, max = 100, message = "El apellido debe tener entre 2 y 100 caracteres")
    @Schema(description = "Apellido del doctor.", example = "Gomez", requiredMode = Schema.RequiredMode.REQUIRED)
    private String apellido;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener formato valido")
    @Schema(description = "Email unico del doctor.", example = "ana.gomez@clinica.local", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @NotBlank(message = "El telefono es obligatorio")
    @Pattern(regexp = "^[+]?[0-9]{9,15}$", message = "El telefono debe contener entre 9 y 15 digitos")
    @Schema(description = "Telefono de contacto entre 9 y 15 digitos.", example = "+56987654321", requiredMode = Schema.RequiredMode.REQUIRED)
    private String telefono;

    @NotBlank(message = "El numero de registro medico es obligatorio")
    @Size(min = 3, max = 50, message = "El numero de registro debe tener entre 3 y 50 caracteres")
    @Schema(description = "Numero unico de registro medico.", example = "REG-MED-1001", requiredMode = Schema.RequiredMode.REQUIRED)
    private String numeroRegistro;

    @NotNull(message = "El ID de especialidad es obligatorio")
    @Positive(message = "El ID de especialidad debe ser positivo")
    @Schema(description = "Identificador de la especialidad asociada.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long especialidadId;

    @NotNull(message = "La fecha de contratacion es obligatoria")
    @PastOrPresent(message = "La fecha de contratacion no puede ser futura")
    @Schema(description = "Fecha de contratacion. No puede ser futura.", example = "2022-03-01", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate fechaContratacion;
}
