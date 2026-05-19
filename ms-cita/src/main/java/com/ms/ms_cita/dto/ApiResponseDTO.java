package com.ms.ms_cita.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseDTO<T> {
    private boolean success;
    private String mensaje;
    private T data;
    private LocalDateTime timestamp = LocalDateTime.now();

    public ApiResponseDTO(boolean success, String mensaje, T data){
        this.success = success;
        this.mensaje = mensaje;
        this.data = data;
        this.timestamp = LocalDateTime.now();

    }

    public static <T> ApiResponseDTO<T> ok(String mensaje, T data) {
        return new ApiResponseDTO<T>(true, mensaje, data);

    }

    public static <T> ApiResponseDTO<T> error(String mensaje){
        return new ApiResponseDTO<T>(false, mensaje, null);
    }

}
