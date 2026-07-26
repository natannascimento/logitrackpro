package br.com.logap.logitrackpro.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import br.com.logap.logitrackpro.entity.Viagem;

public record ViagemResponse(
        Integer id,
        VeiculoResponse veiculo,
        LocalDateTime dataSaida,
        LocalDateTime dataChegada,
        String origem,
        String destino,
        BigDecimal kmPercorrida,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {

    public static ViagemResponse from(Viagem viagem) {
        return new ViagemResponse(
                viagem.getId(),
                VeiculoResponse.from(viagem.getVeiculo()),
                viagem.getDataSaida(),
                viagem.getDataChegada(),
                viagem.getOrigem(),
                viagem.getDestino(),
                viagem.getKmPercorrida(),
                viagem.getCreatedAt(),
                viagem.getUpdatedAt());
    }
}
