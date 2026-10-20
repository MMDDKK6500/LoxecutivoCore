package br.dev.mmddkk.loxecutivo.controller;

import br.dev.mmddkk.loxecutivo.model.Motorista;
import br.dev.mmddkk.loxecutivo.repository.MotoristaRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/motoristas")
public class MotoristaController {

    @Autowired
    private MotoristaRepository motoristaRepository;

    @GetMapping
    public Iterable<Motorista> listarTodos() {
        return motoristaRepository.findAll();
    }

    @PostMapping
    public Motorista salvar(@Valid @RequestBody Motorista motorista) {
        return motoristaRepository.save(motorista);
    }

    @PutMapping("/{id}")
    public Motorista atualizar(@PathVariable Integer id, @Valid @RequestBody Motorista motorista) {
        motorista.setId(id);
        return motoristaRepository.save(motorista);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Integer id) {
        motoristaRepository.deleteById(id);
    }
}