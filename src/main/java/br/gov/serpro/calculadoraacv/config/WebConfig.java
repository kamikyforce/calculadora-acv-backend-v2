package br.gov.serpro.calculadoraacv.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.servlet.MultipartConfigElement;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.oauth2.enabled:true}")
    private boolean oauth2Enabled;

    @Autowired
    private SessionValidationInterceptor sessionValidationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Só adicionar interceptor se OAuth2 estiver habilitado
        if (oauth2Enabled) {
            System.out.println("Adicionando SessionValidationInterceptor (OAuth2 habilitado)");
            registry.addInterceptor(sessionValidationInterceptor)
                    .addPathPatterns("/**")
                    .excludePathPatterns(
                            "/calculadoraacv/backend/public/**",
                            "/auth/**",
                            "/oauth2/**",
                            "/login/**",
                            "/v3/api-docs/**",
                            "/swagger-ui/**",
                            "/actuator/**",
                            "/insumos-rebanho/**",
                            "/insumos-producao-agricola/**",
                            "/energia-combustiveis/**",
                            "/combustiveis/**",
                            "/fatores-energia/**"
                    );
        } else {
            System.out.println("SessionValidationInterceptor desabilitado (modo desenvolvimento)");
        }
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.ofMegabytes(10));
        factory.setMaxRequestSize(DataSize.ofMegabytes(10));
        return factory.createMultipartConfig();
    }
}