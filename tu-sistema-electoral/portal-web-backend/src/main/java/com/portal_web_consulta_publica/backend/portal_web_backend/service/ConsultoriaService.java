package com.portal_web_consulta_publica.backend.portal_web_backend.service;

import com.portal_web_consulta_publica.backend.portal_web_backend.client.AccesoDatosClient;
import com.portal_web_consulta_publica.backend.portal_web_backend.client.SeguridadClient;
import com.portal_web_consulta_publica.backend.portal_web_backend.exception.ServiceException;
import com.portal_web_consulta_publica.backend.portal_web_backend.model.dto.CandidatoDTO;
import com.portal_web_consulta_publica.backend.portal_web_backend.model.dto.CiudadanoMesaZonaDTO;
import com.portal_web_consulta_publica.backend.portal_web_backend.model.dto.ConteoVotosDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Slf4j // Para logging
public class ConsultoriaService {

    private final AccesoDatosClient accesoDatosClient;
    private final SeguridadClient seguridadClient;

    @Autowired
    public ConsultoriaService(AccesoDatosClient accesoDatosClient, SeguridadClient seguridadClient) {
        this.accesoDatosClient = accesoDatosClient;
        this.seguridadClient = seguridadClient;
    }

    // Implementa "ListarCandidatos" / "ConsultarCandidatos"
    public Mono<List<CandidatoDTO>> listarCandidatos() {
        log.info("Consultando candidatos registrados...");
        return accesoDatosClient.consultarCandidatosRegistrados()
                .onErrorResume(e -> {
                    log.error("Error al consultar candidatos registrados: {}", e.getMessage());
                    return Mono.error(new ServiceException("No se pudo obtener la lista de candidatos.", HttpStatus.INTERNAL_SERVER_ERROR));
                });
    }

    // Implementa "VerConteoVotos" / "GetConteoVotos"
    public Mono<ConteoVotosDTO> obtenerConteoVotos() {
        log.info("Obteniendo conteo de votos...");
        return accesoDatosClient.getConteoVotos()
                .onErrorResume(e -> {
                    log.error("Error al obtener conteo de votos: {}", e.getMessage());
                    return Mono.error(new ServiceException("No se pudo obtener el conteo de votos.", HttpStatus.INTERNAL_SERVER_ERROR));
                });
    }

    // Implementa "ConsultarZonaVotacion" y "ConsultarMesa"
    public Mono<CiudadanoMesaZonaDTO> consultarInformacionVotacion(String cedula) {
        log.info("Consultando información de votación para cédula: {}", cedula);
        return seguridadClient.validarFormato(cedula)
                .flatMap(isValid -> {
                    if (Boolean.FALSE.equals(isValid)) {
                        return Mono.error(new ServiceException("Formato de cédula inválido.", HttpStatus.BAD_REQUEST));
                    }
                    return seguridadClient.validarUsuario(cedula); // Validar si la cédula es de un usuario existente
                })
                .flatMap(isUserValid -> {
                    if (Boolean.FALSE.equals(isUserValid)) {
                        return Mono.error(new ServiceException("Cédula no encontrada o usuario no válido.", HttpStatus.NOT_FOUND));
                    }
                    return seguridadClient.getCiudadanoMesaZonaAsignada(cedula);
                })
                .onErrorResume(e -> {
                    log.error("Error al consultar información de votación: {}", e.getMessage());
                    if (e instanceof ServiceException) {
                        return Mono.error(e);
                    }
                    return Mono.error(new ServiceException("Error interno al consultar información de votación.", HttpStatus.INTERNAL_SERVER_ERROR));
                });
    }
    // Nota: Las interfaces "IngresarCedula" y "RegistrarCedula" del diagrama
    // podrían ser parte de la validación interna o una petición de registro
    // que este servicio orquestaría. Aquí se combinan para obtener la info.
}