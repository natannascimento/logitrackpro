package br.com.logap.logitrackpro.service;

import java.util.List;

import br.com.logap.logitrackpro.dto.ViagemRequest;
import br.com.logap.logitrackpro.entity.Viagem;

public interface ViagemService {

    List<Viagem> listarTodas();

    Viagem buscarPorId(Integer id);

    Viagem criar(ViagemRequest request);

    Viagem atualizar(Integer id, ViagemRequest request);

    void excluir(Integer id);
}
