package br.dev.mmddkk.loxecutivo.controller;

import br.dev.mmddkk.loxecutivo.model.Endereco;
import br.dev.mmddkk.loxecutivo.repository.EnderecoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/enderecos")
public class EnderecoController {

    @Autowired
    private EnderecoRepository enderecoRepository;

    @GetMapping
    public Iterable<Endereco> listarTodos() {
        return enderecoRepository.findAll();
    }

    @PostMapping
    public Endereco salvar(@Valid @RequestBody Endereco endereco) {
        return enderecoRepository.save(endereco);
    }

    @PutMapping("/{id}")
    public Endereco atualizar(@PathVariable Integer id, @Valid @RequestBody Endereco endereco) {
        endereco.setId(id);
        return enderecoRepository.save(endereco);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Integer id) {
        enderecoRepository.deleteById(id);
    }
}