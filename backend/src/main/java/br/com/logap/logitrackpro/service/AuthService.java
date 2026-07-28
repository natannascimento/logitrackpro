package br.com.logap.logitrackpro.service;

import br.com.logap.logitrackpro.dto.LoginRequest;
import br.com.logap.logitrackpro.dto.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);

    void logout(String token);
}
