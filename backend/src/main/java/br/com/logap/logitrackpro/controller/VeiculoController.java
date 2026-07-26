package br.com.logap.logitrackpro.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.logap.logitrackpro.dto.VeiculoResponse;
import br.com.logap.logitrackpro.service.VeiculoService;

@RestController
@RequestMapping("/api/veiculos")
public class VeiculoController {

    private final VeiculoService veiculoService;

    public VeiculoController(VeiculoService veiculoService) {
        this.veiculoService = veiculoService;
    }

    @GetMapping
    public List<VeiculoResponse> listar() {
        return veiculoService.listarTodos().stream()
                .map(VeiculoResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    public VeiculoResponse buscarPorId(@PathVariable Integer id) {
        return VeiculoResponse.from(veiculoService.buscarPorId(id));
    }
}
