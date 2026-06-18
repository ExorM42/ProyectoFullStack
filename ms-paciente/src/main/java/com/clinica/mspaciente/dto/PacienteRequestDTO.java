package com.clinica.mspaciente.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "Datos necesarios para crear o actualizar un paciente.")
public class PacienteRequestDTO {

    @NotBlank(message = "El RUT es obligatorio")
    @Pattern(regexp = "^[0-9]{7,8}-[0-9Kk]$", message = "El RUT debe tener formato 12345678-9")
    @Schema(description = "RUT chileno del paciente.", example = "12345678-9", requiredMode = Schema.RequiredMode.REQUIRED)
    private String rut;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Schema(description = "Nombre del paciente.", example = "Juan", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, max = 100, message = "El apellido debe tener entre 2 y 100 caracteres")
    @Schema(description = "Apellido del paciente.", example = "Perez", requiredMode = Schema.RequiredMode.REQUIRED)
    private String apellido;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser en el pasado")
    @Schema(description = "Fecha de nacimiento. No puede ser futura.", example = "1990-04-10", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate fechaNacimiento;

    @NotBlank(message = "El genero es obligatorio")
    @Pattern(regexp = "MASCULINO|FEMENINO|OTRO", message = "El genero debe ser MASCULINO, FEMENINO u OTRO")
    @Schema(description = "Genero declarado del paciente.", example = "MASCULINO", allowableValues = {"MASCULINO", "FEMENINO", "OTRO"}, requiredMode = Schema.RequiredMode.REQUIRED)
    private String genero;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato valido")
    @Schema(description = "Email unico del paciente.", example = "juan.perez@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @NotBlank(message = "El telefono es obligatorio")
    @Pattern(regexp = "^[+]?[0-9]{9,15}$", message = "El telefono debe contener entre 9 y 15 digitos")
    @Schema(description = "Telefono de contacto entre 9 y 15 digitos.", example = "+56912345678", requiredMode = Schema.RequiredMode.REQUIRED)
    private String telefono;

    @Size(max = 255, message = "La direccion no puede superar los 255 caracteres")
    @Schema(description = "Direccion particular del paciente.", example = "Av. Siempre Viva 123")
    private String direccion;

    @NotNull(message = "El ID de prevision es obligatorio")
    @Positive(message = "El ID de prevision debe ser positivo")
    @Schema(description = "Identificador de la prevision asociada.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long previsionId;

    @NotNull(message = "La edad es obligatoria")
    @Min(value = 0, message = "La edad no puede ser negativa")
    @Max(value = 120, message = "La edad no puede ser mayor a 120")
    @Schema(description = "Edad declarada. Debe coincidir con la fecha de nacimiento.", example = "36", minimum = "0", maximum = "120", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer edad;

    @NotNull(message = "Debe indicar si el paciente viene acompanado")
    @Schema(description = "Indica si el paciente viene acompanado. Obligatorio para menores de edad.", example = "false", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean acompanado;
}
