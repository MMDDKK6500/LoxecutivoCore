package br.dev.mmddkk.loxecutivo.controller;

import br.dev.mmddkk.loxecutivo.model.Passageiro;
import br.dev.mmddkk.loxecutivo.repository.PassageiroRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/passageiros")
public class PassageiroController {

    @Autowired
    private PassageiroRepository passageiroRepository;

    @GetMapping
    public Iterable<Passageiro> listarTodos() {
        return passageiroRepository.findAll();
    }

    @PostMapping
    public Passageiro salvar(@Valid @RequestBody Passageiro passageiro) {
        return passageiroRepository.save(passageiro);
    }

    @PutMapping("/{id}")
    public Passageiro atualizar(@PathVariable Integer id, @Valid @RequestBody Passageiro passageiro) {
        passageiro.setId(id);
        return passageiroRepository.save(passageiro);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Integer id) {
        passageiroRepository.deleteById(id);
    }
}