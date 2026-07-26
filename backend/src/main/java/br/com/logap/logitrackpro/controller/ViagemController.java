package br.com.logap.logitrackpro.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import br.com.logap.logitrackpro.dto.ViagemRequest;
import br.com.logap.logitrackpro.dto.ViagemResponse;
import br.com.logap.logitrackpro.service.ViagemService;

@RestController
@RequestMapping("/api/viagens")
public class ViagemController {

    private final ViagemService viagemService;

    public ViagemController(ViagemService viagemService) {
        this.viagemService = viagemService;
    }

    @GetMapping
    public List<ViagemResponse> listar() {
        return viagemService.listarTodas().stream()
                .map(ViagemResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    public ViagemResponse buscarPorId(@PathVariable Integer id) {
        return ViagemResponse.from(viagemService.buscarPorId(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ViagemResponse criar(@Valid @RequestBody ViagemRequest request) {
        return ViagemResponse.from(viagemService.criar(request));
    }

    @PutMapping("/{id}")
    public ViagemResponse atualizar(@PathVariable Integer id, @Valid @RequestBody ViagemRequest request) {
        return ViagemResponse.from(viagemService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        viagemService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
