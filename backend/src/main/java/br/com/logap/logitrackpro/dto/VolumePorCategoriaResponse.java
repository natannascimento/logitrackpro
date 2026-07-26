package br.com.logap.logitrackpro.dto;

public record VolumePorCategoriaResponse(String categoria, Long quantidade) {

    public static VolumePorCategoriaResponse from(VolumePorCategoriaProjection projection) {
        return new VolumePorCategoriaResponse(projection.getTipo(), projection.getQuantidade());
    }
}
