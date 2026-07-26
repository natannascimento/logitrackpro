package br.com.logap.logitrackpro.dto;

import java.math.BigDecimal;

public interface RankingUtilizacaoProjection {

    Integer getVeiculoId();

    String getPlaca();

    String getModelo();

    String getTipo();

    Integer getAno();

    BigDecimal getTotalKm();
}
