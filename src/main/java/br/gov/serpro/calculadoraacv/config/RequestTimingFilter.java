package br.gov.serpro.calculadoraacv.config;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@Component
public class RequestTimingFilter implements Filter {

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
          throws IOException, jakarta.servlet.ServletException {
    long startTime = System.currentTimeMillis();
    try {
      chain.doFilter(request, response);
    } finally {
      long duracaoEmMilissegundos = System.currentTimeMillis() - startTime;
      MDC.put("duracaoEmMilissegundos", String.valueOf(duracaoEmMilissegundos));

      if (request instanceof HttpServletRequest) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();
        log.info(requestURI);
      }

      MDC.remove("duracaoEmMilissegundos");
    }
  }
}