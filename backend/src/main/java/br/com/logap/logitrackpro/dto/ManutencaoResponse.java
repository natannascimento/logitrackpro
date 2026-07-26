package br.com.logap.logitrackpro.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.logap.logitrackpro.entity.Manutencao;
import br.com.logap.logitrackpro.entity.StatusManutencao;

public record ManutencaoResponse(
        Integer id,
        VeiculoResponse veiculo,
        LocalDate dataInicio,
        LocalDate dataFinalizacao,
        String tipoServico,
        BigDecimal custoEstimado,
        StatusManutencao status) {

    public static ManutencaoResponse from(Manutencao manutencao) {
        return new ManutencaoResponse(
                manutencao.getId(),
                VeiculoResponse.from(manutencao.getVeiculo()),
                manutencao.getDataInicio(),
                manutencao.getDataFinalizacao(),
                manutencao.getTipoServico(),
                manutencao.getCustoEstimado(),
                manutencao.getStatus());
    }
}
