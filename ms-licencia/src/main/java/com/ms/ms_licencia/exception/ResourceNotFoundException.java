package com.ms.ms_licencia.exception;

public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(String mensaje) {super (mensaje);}
    public ResourceNotFoundException(String resource, Long id){
        super(resource + " con ID" + id + "no pudo ser encontrado");
    }

}
