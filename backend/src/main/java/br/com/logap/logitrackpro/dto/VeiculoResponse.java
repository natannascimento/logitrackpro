package br.com.logap.logitrackpro.dto;

import br.com.logap.logitrackpro.entity.CategoriaVeiculo;
import br.com.logap.logitrackpro.entity.Veiculo;

public record VeiculoResponse(Integer id, String placa, String modelo, CategoriaVeiculo tipo, Integer ano) {

    public static VeiculoResponse from(Veiculo veiculo) {
        return new VeiculoResponse(veiculo.getId(), veiculo.getPlaca(), veiculo.getModelo(),
                veiculo.getTipo(), veiculo.getAno());
    }
}
