package br.gov.serpro.calculadoraacv.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOriginPatterns("*") // Wildcard bypass
            .allowedMethods("*") // Todos os métodos HTTP
            .allowedHeaders("*") // Todos os headers
            .allowCredentials(true)
            .exposedHeaders("*") // Expor todos os headers na resposta
            .maxAge(3600); // Cache preflight por 1 hora
    }
}