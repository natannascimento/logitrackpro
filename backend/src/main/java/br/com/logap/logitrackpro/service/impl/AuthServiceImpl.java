package br.com.logap.logitrackpro.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.logap.logitrackpro.dto.LoginRequest;
import br.com.logap.logitrackpro.dto.LoginResponse;
import br.com.logap.logitrackpro.entity.Usuario;
import br.com.logap.logitrackpro.exception.CredenciaisInvalidasException;
import br.com.logap.logitrackpro.repository.UsuarioRepository;
import br.com.logap.logitrackpro.security.JwtService;
import br.com.logap.logitrackpro.service.AuthService;

@Service
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthServiceImpl(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder,
                            JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow(() -> new CredenciaisInvalidasException("Email ou senha inválidos"));

        if (!passwordEncoder.matches(request.senha(), usuario.getSenha())) {
            throw new CredenciaisInvalidasException("Email ou senha inválidos");
        }

        String accessToken = jwtService.gerarToken(usuario.getEmail());
        return new LoginResponse(accessToken);
    }
}
