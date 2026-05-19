package com.ms.ms_licencia.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
public class RemoteApiWrapper<T> {

    private boolean success;
    private String mensaje;
    private T data;
}
