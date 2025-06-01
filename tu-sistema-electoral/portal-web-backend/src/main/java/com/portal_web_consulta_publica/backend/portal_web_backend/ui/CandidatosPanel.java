package com.portal_web_consulta_publica.backend.portal_web_backend.ui;

import com.portal_web_consulta_publica.backend.portal_web_backend.model.dto.CandidatoDTO; // Ajustado el import

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class CandidatosPanel extends JPanel {
    private JTable candidatosTable;
    private DefaultTableModel tableModel;
    private ApiClient apiClient;

    public CandidatosPanel() {
        apiClient = new ApiClient();
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Margen alrededor del panel

        JLabel titleLabel = new JLabel("Listado de Candidatos", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        String[] columnNames = {"ID", "Nombre", "Partido", "Cargo"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer que las celdas no sean editables
            }
        };
        candidatosTable = new JTable(tableModel);
        candidatosTable.setFillsViewportHeight(true); // La tabla usa toda la altura disponible
        JScrollPane scrollPane = new JScrollPane(candidatosTable);
        add(scrollPane, BorderLayout.CENTER);

        JButton refreshButton = new JButton("Actualizar Candidatos");
        refreshButton.addActionListener(e -> loadCandidatos());
        add(refreshButton, BorderLayout.SOUTH);

        loadCandidatos(); // Cargar datos al iniciar el panel
    }

    private void loadCandidatos() {
        // Limpiar tabla antes de cargar nuevos datos
        tableModel.setRowCount(0);
        try {
            List<CandidatoDTO> candidatos = apiClient.listarCandidatos();
            if (candidatos.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No se encontraron candidatos.", "Información", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            for (CandidatoDTO candidato : candidatos) {
                tableModel.addRow(new Object[]{candidato.getId(), candidato.getNombre(), candidato.getPartido()});
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar candidatos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}