package com.portal_web_consulta_publica.backend.portal_web_backend.controller;

import com.portal_web_consulta_publica.backend.portal_web_backend.model.dto.CandidatoDTO;
import com.portal_web_consulta_publica.backend.portal_web_backend.model.dto.CiudadanoMesaZonaDTO;
import com.portal_web_consulta_publica.backend.portal_web_backend.model.dto.ConteoVotosDTO;
import com.portal_web_consulta_publica.backend.portal_web_backend.model.request.IngresarCedulaRequest;
import com.portal_web_consulta_publica.backend.portal_web_backend.service.ConsultoriaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/portal-web/consulta")
@Slf4j
public class UIPublicaController {

    private final ConsultoriaService consultoriaService;

    @Autowired
    public UIPublicaController(ConsultoriaService consultoriaService) {
        this.consultoriaService = consultoriaService;
    }

    // Corresponde a "ShowUI" y "ListarCandidatos"
    @GetMapping(value = "/candidatos", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<CandidatoDTO>>> listarCandidatos() {
        log.info("Petición para listar candidatos.");
        return consultoriaService.listarCandidatos()
                .map(ResponseEntity::ok);
    }

    // Corresponde a "VerConteoVotos" y "ConsolidadoVotos"
    @GetMapping(value = "/conteo-votos", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ConteoVotosDTO>> verConteoVotos() {
        log.info("Petición para ver conteo de votos.");
        return consultoriaService.obtenerConteoVotos()
                .map(ResponseEntity::ok);
    }

    // Corresponde a "IngresarCedula", "ConsultarZonaVotacion", "ConsultarMesa"
    // y potencialmente "RegistrarCedula" (si es para una consulta inicial del ciudadano)
    @PostMapping(value = "/ciudadano-info", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<CiudadanoMesaZonaDTO>> consultarCiudadanoInfo(@RequestBody IngresarCedulaRequest request) {
        log.info("Petición para consultar información de ciudadano con cédula: {}", request.getCedula());
        return consultoriaService.consultarInformacionVotacion(request.getCedula())
                .map(ResponseEntity::ok);
    }

    // TODO: Implementar un endpoint para "Graficos" (puede ser GET, devolviendo datos para gráficos)
    // @GetMapping("/graficos")
    // public Mono<ResponseEntity<Map<String, Object>>> obtenerDatosGraficos() { ... }
}