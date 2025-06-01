package com.portal_web_consulta_publica.backend.portal_web_backend;

import com.portal_web_consulta_publica.backend.portal_web_backend.ui.MainApp; // Importa tu MainApp de UI
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean; // Necesitas este import para el @Bean

import javax.swing.SwingUtilities;

@SpringBootApplication
public class PortalWebBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(PortalWebBackendApplication.class, args);
    }

    // Este @Bean se ejecutará después de que el contexto de Spring Boot haya cargado
    @Bean
    public CommandLineRunner runSwingApp() {
        return args -> {
            // Asegúrate de que la UI se ejecute en el Event Dispatch Thread (EDT)
            SwingUtilities.invokeLater(() -> {
                MainApp app = new MainApp();
                app.setVisible(true);
            });
        };
    }
}