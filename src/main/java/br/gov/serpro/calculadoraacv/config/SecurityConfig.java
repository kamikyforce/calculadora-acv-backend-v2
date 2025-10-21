package br.gov.serpro.calculadoraacv.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import br.gov.serpro.calculadoraacv.utils.Constantes;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${app.oauth2.enabled:true}")
    private boolean oauth2Enabled;

    @Autowired
    private CustomOAuth2SuccessHandler successHandler;

    @Autowired
    private CustomOAuth2FailureHandler failureHandler;

    @Autowired
    private CustomLogoutSuccessHandler logoutSuccessHandler;

    private static final String[] PUBLIC_RESOURCES = {
            Constantes.PUBLIC_PATH + "/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/actuator/health",
            "/test-auth/**",
            "/auth/**",
            "/public/**",
            "/certificadoras/**",
            "/administradores/**",
            "/industrias/**",
            "/usuarios/**",
            "/rebanho/**",
            // Adicionar endpoints de energia e combustíveis
            "/energia-combustiveis/**",
            "/combustiveis/**",
            "/fatores-energia/**",
            "/insumos-rebanho/**",
            "/insumos-producao-agricola/**",
            // Endpoints MUT - acesso público para consulta
            "/mut",
            "/mut/estatisticas",
            "/mut/verificar-existencia"
    };

    @Autowired
    private OAuth2AuthorizationRequestResolver authorizationRequestResolver;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        HttpSecurity httpSecurity = http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(AbstractHttpConfigurer::disable);

        if (!oauth2Enabled) {
            // Modo desenvolvimento: permitir todas as requisições
            System.out.println("🔓 MODO DESENVOLVIMENTO: OAuth2 desabilitado - todas as requisições permitidas");
            return httpSecurity
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                    .maximumSessions(1)
                    .maxSessionsPreventsLogin(false)
                )
                .build();
        } else {
            // Modo produção: usar OAuth2
            System.out.println("MODO PRODUÇÃO: OAuth2 habilitado");
            return httpSecurity
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers(publicResources()).permitAll()
                    .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                    .authorizationEndpoint(authorization -> authorization
                        .authorizationRequestResolver(authorizationRequestResolver)
                    )
                    .successHandler(successHandler)
                    .failureHandler(failureHandler)
                )
                .logout(logout -> logout
                    .logoutUrl("/auth/logout")
                    .logoutSuccessHandler(logoutSuccessHandler)
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                )
                .sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                    .maximumSessions(1)
                    .maxSessionsPreventsLogin(false)
                )
                .build();
        }
    }

    // Método que estava faltando
    private String[] publicResources() {
        return PUBLIC_RESOURCES;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*")); // Wildcard bypass
        configuration.setAllowedMethods(Arrays.asList("*")); // Todos os métodos
        configuration.setAllowedHeaders(Arrays.asList("*")); // Todos os headers
        configuration.setAllowCredentials(true); // Permitir credenciais
        configuration.setExposedHeaders(Arrays.asList("*")); // Expor headers
        configuration.setMaxAge(3600L); // Cache preflight por 1 hora
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}