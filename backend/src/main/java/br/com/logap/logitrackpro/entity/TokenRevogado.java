package br.com.logap.logitrackpro.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tokens_revogados")
public class TokenRevogado {

    @Id
    private String jti;

    @Column(name = "expira_em", nullable = false)
    private LocalDateTime expiraEm;

    protected TokenRevogado() {
    }

    public TokenRevogado(String jti, LocalDateTime expiraEm) {
        this.jti = jti;
        this.expiraEm = expiraEm;
    }

    public String getJti() {
        return jti;
    }

    public LocalDateTime getExpiraEm() {
        return expiraEm;
    }
}
