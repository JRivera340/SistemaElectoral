package com.portal_web_consulta_publica.backend.portal_web_backend.ui;

import com.portal_web_consulta_publica.backend.portal_web_backend.model.dto.ConteoVotosDTO; // Ajustado el import

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Map;

public class VotosPanel extends JPanel {
    private JLabel totalVotosLabel;
    private JTextArea votosDetalleArea;
    private ApiClient apiClient;

    public VotosPanel() {
        apiClient = new ApiClient();
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Conteo de Votos", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        JPanel infoPanel = new JPanel(new GridLayout(2, 1, 0, 10)); // Espacio entre componentes
        totalVotosLabel = new JLabel("Total Votos Emitidos: Cargando...");
        totalVotosLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        infoPanel.add(totalVotosLabel);

        votosDetalleArea = new JTextArea(10, 30);
        votosDetalleArea.setEditable(false);
        votosDetalleArea.setLineWrap(true); // Envuelve el texto
        votosDetalleArea.setWrapStyleWord(true); // Envuelve por palabras
        JScrollPane scrollPane = new JScrollPane(votosDetalleArea);
        infoPanel.add(scrollPane);

        add(infoPanel, BorderLayout.CENTER);

        JButton refreshButton = new JButton("Actualizar Conteo");
        refreshButton.addActionListener(e -> loadConteoVotos());
        add(refreshButton, BorderLayout.SOUTH);

        loadConteoVotos(); // Cargar al iniciar el panel
    }

    private void loadConteoVotos() {
        totalVotosLabel.setText("Total Votos Emitidos: Cargando...");
        votosDetalleArea.setText("Detalle de votos: Cargando...");
        try {
            ConteoVotosDTO conteo = apiClient.obtenerConteoVotos();
            if (conteo != null) {
                totalVotosLabel.setText("Total Votos Emitidos: " + conteo.getTotalVotosEmitidos());
                StringBuilder detalle = new StringBuilder();
                detalle.append("Resultados de Elección: ").append(conteo.getNombreEleccion() != null ? conteo.getNombreEleccion() : "N/A").append("\n\n");
                detalle.append("Votos por Candidato:\n");
                if (conteo.getVotosPorCandidato() != null && !conteo.getVotosPorCandidato().isEmpty()) {
                    for (Map.Entry<String, Long> entry : conteo.getVotosPorCandidato().entrySet()) {
                        detalle.append("  - ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
                    }
                } else {
                    detalle.append("  (No hay datos de votos por candidato)\n");
                }
                
                detalle.append("\nVotos por Partido:\n");
                if (conteo.getVotosPorPartido() != null && !conteo.getVotosPorPartido().isEmpty()) {
                    for (Map.Entry<String, Long> entry : conteo.getVotosPorPartido().entrySet()) {
                        detalle.append("  - ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
                    }
                } else {
                    detalle.append("  (No hay datos de votos por partido)\n");
                }
                votosDetalleArea.setText(detalle.toString());
            } else {
                votosDetalleArea.setText("No se pudo obtener el conteo de votos.");
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar conteo de votos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            votosDetalleArea.setText("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}