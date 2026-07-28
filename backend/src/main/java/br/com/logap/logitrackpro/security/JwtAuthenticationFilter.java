package br.com.logap.logitrackpro.security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import br.com.logap.logitrackpro.repository.TokenRevogadoRepository;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String PREFIXO_BEARER = "Bearer ";

    private final JwtService jwtService;
    private final TokenRevogadoRepository tokenRevogadoRepository;

    public JwtAuthenticationFilter(JwtService jwtService, TokenRevogadoRepository tokenRevogadoRepository) {
        this.jwtService = jwtService;
        this.tokenRevogadoRepository = tokenRevogadoRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                     HttpServletResponse response,
                                     FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith(PREFIXO_BEARER)) {
            String token = header.substring(PREFIXO_BEARER.length());

            if (jwtService.tokenValido(token) && !tokenRevogadoRepository.existsById(jwtService.extrairJti(token))) {
                String email = jwtService.extrairEmail(token);
                var authentication = new UsernamePasswordAuthenticationToken(email, null, List.of());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }
}
