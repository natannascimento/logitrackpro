package br.com.logap.logitrackpro.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.logap.logitrackpro.dto.LoginRequest;
import br.com.logap.logitrackpro.dto.LoginResponse;
import br.com.logap.logitrackpro.entity.TokenRevogado;
import br.com.logap.logitrackpro.entity.Usuario;
import br.com.logap.logitrackpro.exception.CredenciaisInvalidasException;
import br.com.logap.logitrackpro.repository.TokenRevogadoRepository;
import br.com.logap.logitrackpro.repository.UsuarioRepository;
import br.com.logap.logitrackpro.security.JwtService;
import br.com.logap.logitrackpro.service.AuthService;

@Service
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenRevogadoRepository tokenRevogadoRepository;

    public AuthServiceImpl(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder,
                            JwtService jwtService, TokenRevogadoRepository tokenRevogadoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.tokenRevogadoRepository = tokenRevogadoRepository;
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

    @Override
    @Transactional
    public void logout(String token) {
        String jti = jwtService.extrairJti(token);
        LocalDateTime expiraEm = jwtService.extrairExpiracao(token)
                .toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        tokenRevogadoRepository.deleteByExpiraEmBefore(LocalDateTime.now());
        tokenRevogadoRepository.save(new TokenRevogado(jti, expiraEm));
    }
}
