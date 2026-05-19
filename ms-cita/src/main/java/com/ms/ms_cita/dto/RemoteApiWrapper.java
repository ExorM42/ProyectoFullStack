package com.ms.ms_cita.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown= true)
public class RemoteApiWrapper<T> {

    private boolean success;
    private String message;
    private T data;

}
