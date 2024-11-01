package com.aluracursos.screenmatch.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Clase para configurar CORS en la aplicación.
 * Esta configuración permite peticiones desde "http://127.0.0.1:5500"
 * con métodos como GET, POST, PUT, DELETE, OPTIONS, HEAD, TRACE y CONNECT.
 */
@Configuration
public class CorsConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Añade la configuración de CORS para todas las rutas
        registry.addMapping("/**")
                // Permite las peticiones desde el origen especificado
                .allowedOrigins("http://127.0.0.1:5500")
                // Permite los métodos HTTP especificados
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "TRACE", "CONNECT");
    }
}
