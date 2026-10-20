package br.dev.mmddkk.loxecutivo.controller;

import br.dev.mmddkk.loxecutivo.model.Passageiro;
import br.dev.mmddkk.loxecutivo.repository.PassageiroRepository;
import br.dev.mmddkk.loxecutivo.repository.ViagensRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;

@RestController
@RequestMapping("/passageiros")
public class PassageiroController {

    @Autowired
    private PassageiroRepository passageiroRepository;

    @Autowired
    private ViagensRepository viagensRepository;

    @GetMapping
    public Iterable<Passageiro> listarTodos() {
        return passageiroRepository.findAll();
    }

    @GetMapping("/viagem/{viagemId}")
    public Iterable<Passageiro> listarPorViagem(@PathVariable Integer viagemId) {
        return passageiroRepository.findAllByViagemId(viagemId);
    }

    @PostMapping
    @Transactional
    public Passageiro salvar(@Valid @RequestBody Passageiro passageiro) {
        return passageiroRepository.save(resolverViagem(passageiro));
    }

    @PutMapping("/{id}")
    @Transactional
    public Passageiro atualizar(@PathVariable Integer id, @Valid @RequestBody Passageiro passageiro) {
        passageiro.setId(id);
        return passageiroRepository.save(resolverViagem(passageiro));
    }

    private Passageiro resolverViagem(Passageiro passageiro) {
        if (passageiro.getIdViagem() == null || passageiro.getIdViagem().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O passageiro deve estar associado a uma viagem.");
        }
        passageiro.setIdViagem(viagensRepository.findById(passageiro.getIdViagem().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Viagem não encontrada.")));
        return passageiro;
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Integer id) {
        passageiroRepository.deleteById(id);
    }
}
