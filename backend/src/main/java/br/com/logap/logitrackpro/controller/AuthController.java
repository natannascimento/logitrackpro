package br.com.logap.logitrackpro.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import br.com.logap.logitrackpro.dto.LoginRequest;
import br.com.logap.logitrackpro.dto.LoginResponse;
import br.com.logap.logitrackpro.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final String PREFIXO_BEARER = "Bearer ";

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/logout")
    public void logout(@RequestHeader("Authorization") String authorization) {
        String token = authorization.substring(PREFIXO_BEARER.length());
        authService.logout(token);
    }
}
