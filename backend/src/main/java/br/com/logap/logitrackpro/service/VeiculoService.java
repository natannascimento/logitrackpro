package br.com.logap.logitrackpro.service;

import java.util.List;

import br.com.logap.logitrackpro.entity.Veiculo;

public interface VeiculoService {

    List<Veiculo> listarTodos();

    Veiculo buscarPorId(Integer id);
}
