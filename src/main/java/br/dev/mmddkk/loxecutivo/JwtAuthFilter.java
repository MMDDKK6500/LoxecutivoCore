package br.dev.mmddkk.loxecutivo;

import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // IMPORTANTE: Libera requisições OPTIONS (CORS Preflight) sem exigir token
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            filterChain.doFilter(request, response);
            return;
        }

        // Pega o token do cabeçalho da requisição
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Acesso negado: Token ausente.");
            return;
        }

        try {
            // Prepara a requisição para perguntar ao Node.js se o token é válido
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", authHeader);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            // Chama a API do Node.js na porta 8082
            String authServiceUrl = "http://localhost:8082/validar";
            ResponseEntity<String> nodeResponse = restTemplate.exchange(
                    authServiceUrl,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            // Se o Node.js responder 200 OK, a requisição segue para o Controller do Spring
            if (nodeResponse.getStatusCode() == HttpStatus.OK) {
                filterChain.doFilter(request, response);
            }

        } catch (HttpClientErrorException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Acesso negado: Token invalido ou expirado.");
        } catch (ResourceAccessException e) {
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            response.getWriter().write("Servico de autenticacao indisponivel.");
        }
    }
}