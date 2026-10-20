package br.dev.mmddkk.loxecutivo.controller;

import br.dev.mmddkk.loxecutivo.model.Veiculo;
import br.dev.mmddkk.loxecutivo.repository.VeiculoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/veiculos")
public class VeiculoController {

    @Autowired
    private VeiculoRepository veiculoRepository;

    @GetMapping
    public Iterable<Veiculo> listarTodos() {
        return veiculoRepository.findAll();
    }

    @PostMapping
    public Veiculo salvar(@Valid @RequestBody Veiculo veiculo) {
        return veiculoRepository.save(veiculo);
    }

    @PutMapping("/{id}")
    public Veiculo atualizar(@PathVariable String id, @Valid @RequestBody Veiculo veiculo) {
        veiculo.setIdPlaca(id);
        return veiculoRepository.save(veiculo);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable String id) {
        veiculoRepository.deleteById(id);
    }
}