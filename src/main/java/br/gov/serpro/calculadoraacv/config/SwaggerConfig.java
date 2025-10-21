package br.gov.serpro.calculadoraacv.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@Configuration
@OpenAPIDefinition(info = @Info(title = "CalculadoraAcv", version = "1.0.0"))
public class SwaggerConfig {
  
}
