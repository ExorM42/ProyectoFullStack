package com.ms.ms_licencia.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import feign.FeignException;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> manejarValidacion(MethodArgumentNotValidException ex){
        Map<String, String> errores = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errores.put(error.getField(), error.getDefaultMessage()));

        Map<String, Object> respuesta = crearError(HttpStatus.BAD_REQUEST, "Error de validación");
        respuesta.put("errores", errores);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> manejarConstraintViolation(ConstraintViolationException ex){
        return crearRespuestaError(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> manejarNoEncontrado(ResourceNotFoundException ex){
        return crearRespuestaError(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(LicenciaConflictoException.class)
    public ResponseEntity<Map<String, Object>> manejarConflictoLicencia(LicenciaConflictoException ex){
        return crearRespuestaError(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<Map<String, Object>> manejarFeign(FeignException ex){
        return crearRespuestaError(HttpStatus.SERVICE_UNAVAILABLE, "Eror interno del servidor");


    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>> manejarIllegalState(IllegalStateException ex){
        return crearRespuestaError(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> manejarIllegalArgument(IllegalArgumentException ex){
        return crearRespuestaError(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> manejarException(Exception ex){
        return crearRespuestaError(HttpStatus.INTERNAL_SERVER_ERROR, "Error inesperado");
    }
    



    

    private ResponseEntity<Map<String, Object>> crearRespuestaError(HttpStatus estado, String mensaje ){

        return ResponseEntity.status(estado).body(crearError(estado, mensaje));
    }

    private Map<String, Object> crearError(HttpStatus estado, String mensaje){
        Map<String, Object> error = new HashMap<>();
        error.put("fecha", LocalDateTime.now());
        error.put("estado", estado.value());
        error.put("error", estado.getReasonPhrase());
        error.put("mensaje", mensaje);
        return error;
    }

}
