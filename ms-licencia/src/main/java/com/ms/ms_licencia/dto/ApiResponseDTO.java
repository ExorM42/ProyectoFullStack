package com.ms.ms_licencia.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    
    public static <T> ApiResponseDTO<T> ok(String mensaje, T data){
        return new ApiResponseDTO<>(true, mensaje, data);
    }

    public static <T> ApiResponseDTO<T> error(String mensaje){
        return new ApiResponseDTO<T>(false, mensaje, null);
    }



}
