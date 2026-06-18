package com.clinica.msdoctor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Respuesta estandar de la API.")
public class ApiResponseDTO<T> {

    @Schema(description = "Indica si la operacion fue exitosa.", example = "true")
    private boolean success;

    @Schema(description = "Mensaje funcional de respuesta.", example = "Operacion realizada exitosamente")
    private String message;

    @Schema(description = "Datos devueltos por el endpoint.")
    private T data;

    @Schema(description = "Codigo de estado HTTP.", example = "200")
    private Integer status;

    @Schema(description = "Fecha y hora de generacion de la respuesta.")
    private LocalDateTime timestamp = LocalDateTime.now();

    public ApiResponseDTO(boolean success, String message, T data, Integer status) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }

    public static <T> ApiResponseDTO<T> ok(String message, T data) {
        return new ApiResponseDTO<>(true, message, data, 200);
    }

    public static <T> ApiResponseDTO<T> error(String message, Integer status) {
        return new ApiResponseDTO<>(false, message, null, status);
    }
}
