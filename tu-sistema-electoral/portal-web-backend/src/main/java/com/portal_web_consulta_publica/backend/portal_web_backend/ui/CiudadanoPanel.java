package com.portal_web_consulta_publica.backend.portal_web_backend.ui;

import com.portal_web_consulta_publica.backend.portal_web_backend.model.dto.CiudadanoMesaZonaDTO; // Ajustado el import

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class CiudadanoPanel extends JPanel {
    private JTextField cedulaField;
    private JTextArea resultadoArea;
    private ApiClient apiClient;

    public CiudadanoPanel() {
        apiClient = new ApiClient();
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Consulta de Ciudadano", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Centrar y añadir espacio
        inputPanel.add(new JLabel("Cédula:"));
        cedulaField = new JTextField(15);
        inputPanel.add(cedulaField);

        JButton searchButton = new JButton("Consultar");
        searchButton.addActionListener(e -> consultarCiudadano());
        inputPanel.add(searchButton);
        add(inputPanel, BorderLayout.NORTH);

        resultadoArea = new JTextArea(10, 40);
        resultadoArea.setEditable(false);
        resultadoArea.setFont(new Font("Monospaced", Font.PLAIN, 14)); // Fuente monoespaciada para mejor formato
        resultadoArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Margen interno
        JScrollPane scrollPane = new JScrollPane(resultadoArea);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void consultarCiudadano() {
        String cedula = cedulaField.getText().trim();
        if (cedula.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese una cédula.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        resultadoArea.setText("Consultando información para cédula: " + cedula + "...");
        // Ejecutar la consulta en un hilo separado para no bloquear la UI
        new SwingWorker<CiudadanoMesaZonaDTO, Void>() {
            @Override
            protected CiudadanoMesaZonaDTO doInBackground() throws Exception {
                return apiClient.consultarCiudadanoInfo(cedula);
            }

            @Override
            protected void done() {
                try {
                    CiudadanoMesaZonaDTO info = get(); // Obtiene el resultado de doInBackground
                    if (info != null) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("Cédula:             ").append(info.getCedula()).append("\n");
                        sb.append("Nombre Completo:    ").append(info.getNombreCompleto()).append("\n");
                        sb.append("Zona de Votación:   ").append(info.getZonaVotacion()).append("\n");
                        sb.append("Dirección Zona:     ").append(info.getDireccionZona()).append("\n");
                        sb.append("Mesa de Votación:   ").append(info.getMesaVotacion()).append("\n");
                        resultadoArea.setText(sb.toString());
                    } else {
                        resultadoArea.setText("No se encontró información para la cédula ingresada o el servicio devolvió nulo.");
                    }
                } catch (Exception e) {
                    Throwable cause = e.getCause() != null ? e.getCause() : e;
                    String errorMessage = "Error al consultar ciudadano: " + cause.getMessage();
                    JOptionPane.showMessageDialog(CiudadanoPanel.this, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
                    resultadoArea.setText("Error: " + cause.getMessage());
                    cause.printStackTrace();
                }
            }
        }.execute(); // Inicia el SwingWorker
    }
}