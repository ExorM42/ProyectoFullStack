package com.ms.ms_cita.exception;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String mensaje) {super(mensaje);}
    public ResourceNotFoundException(String res, Long id){
        super(res + " con ID" + id + "no pudo ser encontrado");
    }

}
