package br.com.logap.logitrackpro.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.logap.logitrackpro.entity.Veiculo;
import br.com.logap.logitrackpro.exception.ResourceNotFoundException;
import br.com.logap.logitrackpro.repository.VeiculoRepository;
import br.com.logap.logitrackpro.service.VeiculoService;

@Service
@Transactional(readOnly = true)
public class VeiculoServiceImpl implements VeiculoService {

    private final VeiculoRepository veiculoRepository;

    public VeiculoServiceImpl(VeiculoRepository veiculoRepository) {
        this.veiculoRepository = veiculoRepository;
    }

    @Override
    public List<Veiculo> listarTodos() {
        return veiculoRepository.findAll();
    }

    @Override
    public Veiculo buscarPorId(Integer id) {
        return veiculoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Veículo não encontrado: " + id));
    }
}
