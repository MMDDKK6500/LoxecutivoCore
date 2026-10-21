package br.dev.mmddkk.loxecutivo;

import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class JwtAuthFilterTest {

    private static final String AUTH_URL = "http://auth.test/validar";

    @Test
    void rejectsRequestsWithoutBearerToken() throws Exception {
        RestTemplate restTemplate = mock(RestTemplate.class);
        JwtAuthFilter filter = new JwtAuthFilter(restTemplate, AUTH_URL, "");
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/eventos");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        filter.doFilter(request, response, chain);

        assertEquals(401, response.getStatus());
        verifyNoInteractions(restTemplate);
        verifyNoInteractions(chain);
    }

    @Test
    void forwardsOnlyAfterAuthServiceAcceptsToken() throws Exception {
        RestTemplate restTemplate = mock(RestTemplate.class);
        when(restTemplate.exchange(eq(AUTH_URL), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenReturn(ResponseEntity.ok("{\"auth\":true}"));
        JwtAuthFilter filter = new JwtAuthFilter(restTemplate, AUTH_URL, "");
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/eventos");
        request.addHeader("Authorization", "Bearer signed-token");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertEquals(200, response.getStatus());
        assertTrue(chain.getRequest() != null);
        verify(restTemplate).exchange(eq(AUTH_URL), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class));
    }

    @Test
    void rejectsNonSuccessfulAuthResponses() throws Exception {
        RestTemplate restTemplate = mock(RestTemplate.class);
        when(restTemplate.exchange(eq(AUTH_URL), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenReturn(ResponseEntity.status(403).build());
        JwtAuthFilter filter = new JwtAuthFilter(restTemplate, AUTH_URL, "");
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/eventos");
        request.addHeader("Authorization", "Bearer signed-token");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        filter.doFilter(request, response, chain);

        assertEquals(401, response.getStatus());
        verifyNoInteractions(chain);
    }

    @Test
    void acceptsConfiguredReportTokenOnlyForLoopbackGetRequests() throws Exception {
        RestTemplate restTemplate = mock(RestTemplate.class);
        JwtAuthFilter filter = new JwtAuthFilter(restTemplate, AUTH_URL, "scheduled-report-secret");
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/eventos");
        request.setRemoteAddr("127.0.0.1");
        request.addHeader("Authorization", "Bearer scheduled-report-secret");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertEquals(200, response.getStatus());
        assertTrue(chain.getRequest() != null);
        verifyNoInteractions(restTemplate);
    }

    @Test
    void doesNotAcceptReportTokenForMutatingRequests() throws Exception {
        RestTemplate restTemplate = mock(RestTemplate.class);
        when(restTemplate.exchange(eq(AUTH_URL), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenReturn(ResponseEntity.status(401).build());
        JwtAuthFilter filter = new JwtAuthFilter(restTemplate, AUTH_URL, "scheduled-report-secret");
        MockHttpServletRequest request = new MockHttpServletRequest("DELETE", "/eventos/1");
        request.setRemoteAddr("127.0.0.1");
        request.addHeader("Authorization", "Bearer scheduled-report-secret");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        filter.doFilter(request, response, chain);

        assertEquals(401, response.getStatus());
        verify(restTemplate).exchange(eq(AUTH_URL), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class));
        verifyNoInteractions(chain);
    }

    @Test
    void doesNotAcceptReportTokenOutsideReportingReadEndpoints() throws Exception {
        RestTemplate restTemplate = mock(RestTemplate.class);
        when(restTemplate.exchange(eq(AUTH_URL), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenReturn(ResponseEntity.status(401).build());
        JwtAuthFilter filter = new JwtAuthFilter(restTemplate, AUTH_URL, "scheduled-report-secret");
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/usuarios");
        request.setRemoteAddr("127.0.0.1");
        request.addHeader("Authorization", "Bearer scheduled-report-secret");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        filter.doFilter(request, response, chain);

        assertEquals(401, response.getStatus());
        verify(restTemplate).exchange(eq(AUTH_URL), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class));
        verifyNoInteractions(chain);
    }
}
