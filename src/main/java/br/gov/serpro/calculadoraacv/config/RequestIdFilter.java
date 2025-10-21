package br.gov.serpro.calculadoraacv.config;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

@Component
public class RequestIdFilter implements Filter {

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, jakarta.servlet.ServletException {
    try {
      String requestId = ((HttpServletRequest) request).getHeader("X-Request-Id");
      requestId = buscarOuGerarId();

      MDC.put("requestId", requestId);
      chain.doFilter(request, response);
    } finally {
      MDC.remove("requestId");
    }
  }

  private String buscarOuGerarId() {
    String requestId = MDC.get("requestId");
    if (requestId == null || requestId.isEmpty()) {
        requestId = UUID.randomUUID().toString();
    }
    return requestId;
  }
}