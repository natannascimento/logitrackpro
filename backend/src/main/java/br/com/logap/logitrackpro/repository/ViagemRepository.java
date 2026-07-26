package br.com.logap.logitrackpro.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.logap.logitrackpro.dto.RankingUtilizacaoProjection;
import br.com.logap.logitrackpro.dto.VolumePorCategoriaProjection;
import br.com.logap.logitrackpro.entity.Viagem;

public interface ViagemRepository extends JpaRepository<Viagem, Integer> {

    @Query(value = """
            SELECT COALESCE(SUM(km_percorrida), 0)
            FROM viagens
            WHERE (:veiculoId IS NULL OR veiculo_id = :veiculoId)
            """, nativeQuery = true)
    BigDecimal totalKmPercorrido(@Param("veiculoId") Integer veiculoId);

    @Query(value = """
            SELECT v.tipo AS tipo, COUNT(vg.id) AS quantidade
            FROM viagens vg
            JOIN veiculos v ON v.id = vg.veiculo_id
            GROUP BY v.tipo
            """, nativeQuery = true)
    List<VolumePorCategoriaProjection> volumePorCategoria();

    @Query(value = """
            SELECT v.id AS veiculoId, v.placa AS placa, v.modelo AS modelo, v.tipo AS tipo, v.ano AS ano,
                   SUM(vg.km_percorrida) AS totalKm
            FROM viagens vg
            JOIN veiculos v ON v.id = vg.veiculo_id
            GROUP BY v.id, v.placa, v.modelo, v.tipo, v.ano
            ORDER BY totalKm DESC
            LIMIT 1
            """, nativeQuery = true)
    Optional<RankingUtilizacaoProjection> rankingUtilizacao();
}
