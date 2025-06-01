package com.portal_web_consulta_publica.backend.portal_web_backend.ui;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.portal_web_consulta_publica.backend.portal_web_backend.model.dto.CandidatoDTO; // Ajustado el import
import com.portal_web_consulta_publica.backend.portal_web_backend.model.dto.CiudadanoMesaZonaDTO; // Ajustado el import
import com.portal_web_consulta_publica.backend.portal_web_backend.model.dto.ConteoVotosDTO; // Ajustado el import
import com.portal_web_consulta_publica.backend.portal_web_backend.model.dto.ErrorResponseDTO; // Ajustado el import
import com.portal_web_consulta_publica.backend.portal_web_backend.model.request.IngresarCedulaRequest; // Ajustado el import

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class ApiClient {

    // La URL base de tu propio backend, que estará corriendo en el mismo proceso
    private static final String BASE_URL = "http://localhost:8080/portal-web/consulta";
    private final Gson gson = new Gson();

    public List<CandidatoDTO> listarCandidatos() throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(BASE_URL + "/candidatos");
            return httpClient.execute(request, response -> {
                if (response.getCode() == 200) {
                    Type listType = new TypeToken<List<CandidatoDTO>>(){}.getType();
                    return gson.fromJson(new InputStreamReader(response.getEntity().getContent()), listType);
                } else {
                    handleErrorResponse(new InputStreamReader(response.getEntity().getContent()), response.getCode());
                    return Collections.emptyList(); // Esto nunca se alcanzará si handleErrorResponse lanza excepción
                }
            });
        }
    }

    public ConteoVotosDTO obtenerConteoVotos() throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(BASE_URL + "/conteo-votos");
            return httpClient.execute(request, response -> {
                if (response.getCode() == 200) {
                    return gson.fromJson(new InputStreamReader(response.getEntity().getContent()), ConteoVotosDTO.class);
                } else {
                    handleErrorResponse(new InputStreamReader(response.getEntity().getContent()), response.getCode());
                    return null; // O lanzar excepción si prefieres
                }
            });
        }
    }

    public CiudadanoMesaZonaDTO consultarCiudadanoInfo(String cedula) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(BASE_URL + "/ciudadano-info");
            IngresarCedulaRequest reqBody = new IngresarCedulaRequest();
            reqBody.setCedula(cedula);
            request.setEntity(new StringEntity(gson.toJson(reqBody), ContentType.APPLICATION_JSON));

            return httpClient.execute(request, response -> {
                if (response.getCode() == 200) {
                    return gson.fromJson(new InputStreamReader(response.getEntity().getContent()), CiudadanoMesaZonaDTO.class);
                } else {
                    handleErrorResponse(new InputStreamReader(response.getEntity().getContent()), response.getCode());
                    return null; // O lanzar excepción si prefieres
                }
            });
        }
    }

    private void handleErrorResponse(InputStreamReader reader, int statusCode) throws IOException {
        ErrorResponseDTO error = gson.fromJson(reader, ErrorResponseDTO.class);
        String errorMessage = "Error del Servicio (" + statusCode + "): " + error.getMessage();
        throw new IOException(errorMessage); // Lanza una excepción para que la UI la capture
    }
}