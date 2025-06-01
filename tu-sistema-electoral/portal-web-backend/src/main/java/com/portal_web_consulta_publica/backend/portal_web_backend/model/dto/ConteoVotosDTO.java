package com.portal_web_consulta_publica.backend.portal_web_backend.model.dto;

import lombok.Data;
import java.util.Map;

@Data
public class ConteoVotosDTO {
    private String eleccionId;
    private String nombreEleccion;
    private Long totalVotosEmitidos;
    private Map<String, Long> votosPorCandidato; // Map<NombreCandidato, Conteo>
    private Map<String, Long> votosPorPartido;   // Map<NombrePartido, Conteo>
    // Otros datos como votos nulos, blancos, etc.
}