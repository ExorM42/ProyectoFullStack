package com.clinica.mspaciente.exception;

/**
 * Excepción lanzada cuando se intenta registrar un recurso duplicado (ej: RUT o email ya existe).
 */
public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String message) {
        super(message);
    }
}
