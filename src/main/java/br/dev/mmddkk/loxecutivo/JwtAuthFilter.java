package br.dev.mmddkk.loxecutivo;

import org.springframework.http.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final RestTemplate restTemplate;
    private final String authServiceUrl;
    private final String reportServiceToken;

    public JwtAuthFilter(RestTemplate restTemplate,
                         @Value("${auth.service.url:http://127.0.0.1:8082/validar}") String authServiceUrl,
                         @Value("${REPORT_SERVICE_TOKEN:}") String reportServiceToken) {
        this.restTemplate = restTemplate;
        this.authServiceUrl = authServiceUrl;
        this.reportServiceToken = reportServiceToken;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return "GET".equalsIgnoreCase(request.getMethod()) && "/health".equals(request.getRequestURI());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // IMPORTANTE: Libera requisições OPTIONS (CORS Preflight) sem exigir token
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            filterChain.doFilter(request, response);
            return;
        }

        // The reporting service may read data on a schedule without a user's session.
        // Keep this credential read-only and accept it only over the local interface.
        if (isTrustedReportRead(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Pega o token do cabeçalho da requisição
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.matches("(?i)^Bearer\\s+\\S+$")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Acesso negado: Token ausente.");
            return;
        }

        try {
            // Prepara a requisição para perguntar ao Node.js se o token é válido
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", authHeader);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> nodeResponse = restTemplate.exchange(
                    authServiceUrl,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            // Only a successful validation response may reach a controller.
            if (nodeResponse.getStatusCode().is2xxSuccessful()) {
                filterChain.doFilter(request, response);
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Acesso negado: Token inválido ou expirado.");
            }

        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode().value() == HttpServletResponse.SC_UNAUTHORIZED) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Acesso negado: Token inválido ou expirado.");
            } else {
                response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
                response.getWriter().write("Serviço de autenticação indisponível.");
            }
        } catch (ResourceAccessException e) {
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            response.getWriter().write("Servico de autenticacao indisponivel.");
        }
    }

    private boolean isTrustedReportRead(HttpServletRequest request) {
        if (reportServiceToken == null || reportServiceToken.isBlank()
                || !"GET".equalsIgnoreCase(request.getMethod())) return false;
        String requestPath = request.getRequestURI();
        if (!("/eventos".equals(requestPath) || "/viagens".equals(requestPath)
                || "/motoristas".equals(requestPath) || "/veiculos".equals(requestPath)
                || "/enderecos".equals(requestPath) || "/passageiros".equals(requestPath))) return false;
        String remoteAddress = request.getRemoteAddr();
        if (!("127.0.0.1".equals(remoteAddress) || "::1".equals(remoteAddress)
                || "0:0:0:0:0:0:0:1".equals(remoteAddress) || "::ffff:127.0.0.1".equals(remoteAddress))) return false;
        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.matches("(?i)^Bearer\\s+\\S+$")) return false;
        byte[] expected = reportServiceToken.getBytes(StandardCharsets.UTF_8);
        byte[] provided = authorization.replaceFirst("(?i)^Bearer\\s+", "").getBytes(StandardCharsets.UTF_8);
        return MessageDigest.isEqual(expected, provided);
    }
}
