package br.com.logap.logitrackpro.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.logap.logitrackpro.dto.ViagemRequest;
import br.com.logap.logitrackpro.entity.Veiculo;
import br.com.logap.logitrackpro.entity.Viagem;
import br.com.logap.logitrackpro.exception.BusinessRuleException;
import br.com.logap.logitrackpro.exception.ResourceNotFoundException;
import br.com.logap.logitrackpro.repository.VeiculoRepository;
import br.com.logap.logitrackpro.repository.ViagemRepository;
import br.com.logap.logitrackpro.service.ViagemService;

@Service
@Transactional(readOnly = true)
public class ViagemServiceImpl implements ViagemService {

    private final ViagemRepository viagemRepository;
    private final VeiculoRepository veiculoRepository;

    public ViagemServiceImpl(ViagemRepository viagemRepository, VeiculoRepository veiculoRepository) {
        this.viagemRepository = viagemRepository;
        this.veiculoRepository = veiculoRepository;
    }

    @Override
    public List<Viagem> listarTodas() {
        return viagemRepository.findAll();
    }

    @Override
    public Viagem buscarPorId(Integer id) {
        return viagemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Viagem não encontrada: " + id));
    }

    @Override
    @Transactional
    public Viagem criar(ViagemRequest request) {
        validarRegrasDeNegocio(request);
        Veiculo veiculo = buscarVeiculo(request.veiculoId());
        Viagem viagem = new Viagem(veiculo, request.dataSaida(), request.dataChegada(),
                request.origem(), request.destino(), request.kmPercorrida());
        return viagemRepository.save(viagem);
    }

    @Override
    @Transactional
    public Viagem atualizar(Integer id, ViagemRequest request) {
        Viagem viagem = buscarPorId(id);
        validarRegrasDeNegocio(request);
        Veiculo veiculo = buscarVeiculo(request.veiculoId());
        viagem.atualizar(veiculo, request.dataSaida(), request.dataChegada(),
                request.origem(), request.destino(), request.kmPercorrida());
        return viagem;
    }

    @Override
    @Transactional
    public void excluir(Integer id) {
        Viagem viagem = buscarPorId(id);
        viagemRepository.delete(viagem);
    }

    private Veiculo buscarVeiculo(Integer veiculoId) {
        return veiculoRepository.findById(veiculoId)
                .orElseThrow(() -> new BusinessRuleException("Veículo não encontrado: " + veiculoId));
    }

    private void validarRegrasDeNegocio(ViagemRequest request) {
        if (request.dataChegada() != null && request.dataChegada().isBefore(request.dataSaida())) {
            throw new BusinessRuleException("dataChegada não pode ser anterior a dataSaida");
        }
        if (request.kmPercorrida().signum() <= 0) {
            throw new BusinessRuleException("kmPercorrida deve ser maior que zero");
        }
    }
}
