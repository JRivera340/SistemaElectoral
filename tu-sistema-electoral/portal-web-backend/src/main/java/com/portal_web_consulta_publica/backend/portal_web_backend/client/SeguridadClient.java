package com.portal_web_consulta_publica.backend.portal_web_backend.client;

import com.portal_web_consulta_publica.backend.portal_web_backend.model.dto.CiudadanoMesaZonaDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class SeguridadClient {

    private final WebClient webClient;

    // URL base del servicio Seguridad, se inyecta desde application.properties
    public SeguridadClient(@Value("${client.seguridad.url}") String baseUrl, WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    // Corresponde a "ValidarFormato"
    public Mono<Boolean> validarFormato(String input) {
        // Asumiendo que Seguridad tiene un endpoint para validar formatos generales
        return webClient.post()
                .uri("/validar/formato")
                .bodyValue(input)
                .retrieve()
                .bodyToMono(Boolean.class);
    }

    // Corresponde a "ValidarUsuario" (ej. para validar si la cédula corresponde a un usuario registrado)
    public Mono<Boolean> validarUsuario(String cedula) {
        return webClient.get()
                .uri("/usuarios/validar/{cedula}", cedula)
                .retrieve()
                .bodyToMono(Boolean.class);
    }

    // Corresponde a "CiudadanoMesaZonaAsig"
    public Mono<CiudadanoMesaZonaDTO> getCiudadanoMesaZonaAsignada(String cedula) {
        return webClient.get()
                .uri("/ciudadanos/{cedula}/mesa-zona", cedula)
                .retrieve()
                .bodyToMono(CiudadanoMesaZonaDTO.class);
    }
}