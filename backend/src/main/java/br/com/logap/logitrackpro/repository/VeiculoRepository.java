package br.com.logap.logitrackpro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.logap.logitrackpro.entity.Veiculo;

public interface VeiculoRepository extends JpaRepository<Veiculo, Integer> {
}
