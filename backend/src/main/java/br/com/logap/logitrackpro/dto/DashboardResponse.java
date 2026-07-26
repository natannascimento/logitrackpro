package br.com.logap.logitrackpro.dto;

import java.math.BigDecimal;
import java.util.List;

public record DashboardResponse(
        BigDecimal totalKmPercorrido,
        List<VolumePorCategoriaResponse> volumePorCategoria,
        List<ManutencaoResponse> cronogramaManutencoes,
        RankingUtilizacaoResponse rankingUtilizacao,
        BigDecimal projecaoFinanceiraMesCorrente) {
}
