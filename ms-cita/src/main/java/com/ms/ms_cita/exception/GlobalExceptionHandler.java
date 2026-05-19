package com.ms.ms_cita.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import feign.FeignException;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {


    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> manejarValidacion(MethodArgumentNotValidException ex){
        Map<String, String> error = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err ->
            error.put(err.getField(), err.getDefaultMessage())
        );
        
    
        log.warn("EXCEPTION || Error de validación: {}", error);
        Map<String, Object> respuesta = crearError(HttpStatus.BAD_REQUEST, "Error de validación");
        respuesta.put("Errores", error);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> manejarConstraintViolation(ConstraintViolationException ex){
        log.warn("EXCEPTION || Restriccion violada: {}", ex.getMessage());
        return crearRespuestaError(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> manejarNoEncontrado(ResourceNotFoundException ex){
        log.warn("EXCEPTION || Recurso no encontrado!! {}", ex.getMessage());
        return crearRespuestaError(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(HorarioOcupadoException.class)
    public ResponseEntity<Map<String, Object>> manejarHorarioOcupado(HorarioOcupadoException ex){
        log.warn("EXCEPTION || Horario ocupado {}", ex.getMessage());
        return crearRespuestaError(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<Map<String, Object>> manejarFeign(FeignException ex){
        log.error("EXCEPTION || Error de comunicacion en Feign. Estado: {} | Mensaje: {}", ex.status(), ex.getMessage());
        return crearRespuestaError(HttpStatus.SERVICE_UNAVAILABLE, "Error de comunicacion en feign");
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>> manejarIllegalState(IllegalStateException ex){
        log.warn("EXCEPTION || Estado invalido: {}", ex.getMessage());
        return crearRespuestaError(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> manejarIllegalArgument(IllegalArgumentException ex){
        log.warn("EXCEPTION || Argumento invalido: {}", ex.getMessage());
        return crearRespuestaError(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> manejarErrorGeneral(Exception ex){
        log.error("EXCEPTION || Error", ex);
        return crearRespuestaError(HttpStatus.BAD_REQUEST, ex.getMessage());

    }



    private ResponseEntity<Map<String, Object>> crearRespuestaError(HttpStatus estado, String mensaje){
        return ResponseEntity.status(estado).body(crearError(estado, mensaje));
    }
    private Map<String, Object> crearError(HttpStatus estado, String mensaje){
        Map<String, Object> err = new HashMap<>();
        err.put("fecha", LocalDateTime.now());
        err.put("estado", estado.value());
        err.put("error", estado.getReasonPhrase());
        err.put("mensaje", mensaje);
        return err;

    }
}
