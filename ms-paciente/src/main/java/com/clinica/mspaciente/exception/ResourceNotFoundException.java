package com.clinica.mspaciente.exception;

/**
 * Excepción lanzada cuando no se encuentra un recurso en la base de datos.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resource, Long id) {
        super(resource + " con ID " + id + " no fue encontrado");
    }

    // edad 
    public ResourceNotFoundException(String resource, Integer edad) {
        super(resource + " con edad " + edad + " no fue encontrado");
    }

     // ACOMPAÑADO
     public ResourceNotFoundException(String resource, Boolean acompanado) {
        super(resource + " con acompañado " + acompanado + " no fue encontrado");
    }
}
