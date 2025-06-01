package com.portal_web_consulta_publica.backend.portal_web_backend.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainApp extends JFrame {

    public MainApp() {
        setTitle("Sistema Electoral - Consulta Pública Local");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // No cerrar por defecto, lo manejaremos
        setSize(800, 600); // Tamaño inicial de la ventana
        setMinimumSize(new Dimension(600, 400)); // Tamaño mínimo
        setLocationRelativeTo(null); // Centrar la ventana en la pantalla

        // Manejar el cierre de la ventana para terminar toda la aplicación
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                // Confirmar si el usuario realmente quiere salir
                int confirm = JOptionPane.showConfirmDialog(
                        MainApp.this,
                        "¿Estás seguro de que quieres salir?\nEsto detendrá el servidor del portal web.",
                        "Confirmar Salida",
                        JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    System.exit(0); // Cierra toda la JVM, incluyendo el servidor Spring Boot
                }
            }
        });

        // Crear el panel de pestañas
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("SansSerif", Font.BOLD, 14)); // Fuente para las pestañas

        // Crear instancias de tus paneles de contenido
        CandidatosPanel candidatosPanel = new CandidatosPanel();
        VotosPanel votosPanel = new VotosPanel();
        CiudadanoPanel ciudadanoPanel = new CiudadanoPanel();

        // Añadir paneles al JTabbedPane
        tabbedPane.addTab("Candidatos", null, candidatosPanel, "Consulta el listado de candidatos");
        tabbedPane.addTab("Conteo Votos", null, votosPanel, "Consulta el conteo de votos");
        tabbedPane.addTab("Mi Votación", null, ciudadanoPanel, "Consulta tu mesa y zona de votación");

        // Añadir el panel de pestañas a la ventana principal
        add(tabbedPane);
    }

    // Este main NO se ejecutará directamente, lo llamará el CommandLineRunner de Spring Boot
    public static void main(String[] args) {
        // Asegúrate de que la UI se ejecute en el Event Dispatch Thread (EDT)
        // Esto es crucial para la estabilidad de Swing
        SwingUtilities.invokeLater(() -> {
            MainApp app = new MainApp();
            app.setVisible(true);
        });
    }
}