package com.ms.ms_licencia.dto;

import lombok.Data;

@Data
public class TipoLicenciaResponseDTO {

    private Long id;
    private String codigo;
    private String nombre;
    private String descripcion;
    private Integer diasMaximos;

}
