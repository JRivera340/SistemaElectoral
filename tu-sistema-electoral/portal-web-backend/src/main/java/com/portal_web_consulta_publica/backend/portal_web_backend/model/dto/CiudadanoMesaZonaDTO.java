package com.portal_web_consulta_publica.backend.portal_web_backend.model.dto;

import lombok.Data;

@Data
public class CiudadanoMesaZonaDTO {
    private String cedula;
    private String nombreCompleto;
    private String zonaVotacion;
    private String direccionZona;
    private String mesaVotacion;
    // Otros datos de la asignación
}