package br.com.logap.logitrackpro.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.logap.logitrackpro.entity.Manutencao;

public interface ManutencaoRepository extends JpaRepository<Manutencao, Integer> {

    @Query(value = """
            SELECT * FROM manutencoes
            WHERE status <> 'CONCLUIDA'
            ORDER BY data_inicio ASC
            LIMIT 5
            """, nativeQuery = true)
    List<Manutencao> proximasNaoConcluidas();

    @Query(value = """
            SELECT COALESCE(SUM(custo_estimado), 0)
            FROM manutencoes
            WHERE date_trunc('month', data_inicio) = date_trunc('month', CURRENT_DATE)
            """, nativeQuery = true)
    BigDecimal projecaoFinanceiraMesCorrente();
}
