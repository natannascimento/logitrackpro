package br.com.logap.logitrackpro.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.logap.logitrackpro.dto.DashboardResponse;
import br.com.logap.logitrackpro.dto.ManutencaoResponse;
import br.com.logap.logitrackpro.dto.RankingUtilizacaoResponse;
import br.com.logap.logitrackpro.dto.VolumePorCategoriaResponse;
import br.com.logap.logitrackpro.repository.ManutencaoRepository;
import br.com.logap.logitrackpro.repository.ViagemRepository;
import br.com.logap.logitrackpro.service.DashboardService;

@Service
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private final ViagemRepository viagemRepository;
    private final ManutencaoRepository manutencaoRepository;

    public DashboardServiceImpl(ViagemRepository viagemRepository, ManutencaoRepository manutencaoRepository) {
        this.viagemRepository = viagemRepository;
        this.manutencaoRepository = manutencaoRepository;
    }

    @Override
    public DashboardResponse obterMetricas(Integer veiculoId) {
        var totalKmPercorrido = viagemRepository.totalKmPercorrido(veiculoId);

        var volumePorCategoria = viagemRepository.volumePorCategoria().stream()
                .map(VolumePorCategoriaResponse::from)
                .toList();

        var cronogramaManutencoes = manutencaoRepository.proximasNaoConcluidas().stream()
                .map(ManutencaoResponse::from)
                .toList();

        var rankingUtilizacao = viagemRepository.rankingUtilizacao()
                .map(RankingUtilizacaoResponse::from)
                .orElse(null);

        var projecaoFinanceiraMesCorrente = manutencaoRepository.projecaoFinanceiraMesCorrente();

        return new DashboardResponse(
                totalKmPercorrido,
                volumePorCategoria,
                cronogramaManutencoes,
                rankingUtilizacao,
                projecaoFinanceiraMesCorrente);
    }
}
