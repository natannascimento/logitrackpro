package br.com.logap.logitrackpro.dto;

import java.math.BigDecimal;

public record RankingUtilizacaoResponse(
        Integer veiculoId, String placa, String modelo, String tipo, Integer ano, BigDecimal totalKm) {

    public static RankingUtilizacaoResponse from(RankingUtilizacaoProjection projection) {
        return new RankingUtilizacaoResponse(
                projection.getVeiculoId(),
                projection.getPlaca(),
                projection.getModelo(),
                projection.getTipo(),
                projection.getAno(),
                projection.getTotalKm());
    }
}
