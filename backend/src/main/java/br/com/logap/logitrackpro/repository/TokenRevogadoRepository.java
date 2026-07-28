package br.com.logap.logitrackpro.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.logap.logitrackpro.entity.TokenRevogado;

public interface TokenRevogadoRepository extends JpaRepository<TokenRevogado, String> {

    void deleteByExpiraEmBefore(LocalDateTime momento);
}
