package br.com.logap.logitrackpro.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record ViagemRequest(
        @NotNull(message = "veiculoId é obrigatório") Integer veiculoId,
        @NotNull(message = "dataSaida é obrigatória") LocalDateTime dataSaida,
        LocalDateTime dataChegada,
        String origem,
        String destino,
        @NotNull(message = "kmPercorrida é obrigatória")
        @DecimalMin(value = "0.0", inclusive = false, message = "kmPercorrida deve ser maior que zero")
        BigDecimal kmPercorrida) {
}
