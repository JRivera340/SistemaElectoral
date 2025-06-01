package com.portal_web_consulta_publica.backend.portal_web_backend.client;

import com.portal_web_consulta_publica.backend.portal_web_backend.model.dto.CandidatoDTO;
import com.portal_web_consulta_publica.backend.portal_web_backend.model.dto.ConteoVotosDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class AccesoDatosClient {

    private final WebClient webClient;

    // URL base del servicio AccesoDatos, se inyecta desde application.properties
    public AccesoDatosClient(@Value("${client.acceso-datos.url}") String baseUrl, WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    // Corresponde a "ConsultarCandidatosRegistrados"
    public Mono<List<CandidatoDTO>> consultarCandidatosRegistrados() {
        return webClient.get()
                .uri("/candidatos/registrados")
                .retrieve()
                .bodyToFlux(CandidatoDTO.class)
                .collectList();
    }

    // Corresponde a "GetZonasVotacion" (asumiendo un DTO para Zona)
    public Mono<List<String>> getZonasVotacion() {
        // Asumiendo que el servicio AccesoDatos devuelve una lista de nombres de zonas o DTOs de zonas
        return webClient.get()
                .uri("/zonas-votacion")
                .retrieve()
                .bodyToFlux(String.class) // O un DTO de ZonaVotacion
                .collectList();
    }

    // Corresponde a "GetConteoVotos"
    public Mono<ConteoVotosDTO> getConteoVotos() {
        return webClient.get()
                .uri("/conteo-votos")
                .retrieve()
                .bodyToMono(ConteoVotosDTO.class);
    }
}