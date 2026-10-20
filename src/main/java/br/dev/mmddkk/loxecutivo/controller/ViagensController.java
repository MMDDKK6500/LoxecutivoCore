package br.dev.mmddkk.loxecutivo.controller;

import br.dev.mmddkk.loxecutivo.model.Evento;
import br.dev.mmddkk.loxecutivo.model.Viagens;
import br.dev.mmddkk.loxecutivo.repository.EventoRepository;
import br.dev.mmddkk.loxecutivo.repository.PassageiroRepository;
import br.dev.mmddkk.loxecutivo.repository.ViagensRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/viagens")
public class ViagensController {

    @Autowired
    private ViagensRepository viagensRepository;

    @Autowired
    private PassageiroRepository passageiroRepository;

    @Autowired
    private EventoRepository eventoRepository;


    @GetMapping
    public Iterable<Viagens> listarTodos() {
        return viagensRepository.findAll();
    }


    @GetMapping("/evento/{eventoId}")
    public List<Viagens> listarPorEvento(@PathVariable Integer eventoId) {
        Evento evento = eventoRepository.findById(eventoId).orElse(null);
        if (evento == null) {
            return List.of();
        }
        return viagensRepository.findByIdEvento(evento);
    }

    @GetMapping("/{id}")
    public Viagens buscarPorId(@PathVariable Integer id) {
        return viagensRepository.findById(id).orElseThrow();
    }

    @PostMapping
    public Viagens salvar(@Valid @RequestBody Viagens viagem) {
        return viagensRepository.save(viagem);
    }

    @PutMapping("/{id}")
    public Viagens atualizar(@PathVariable Integer id, @Valid @RequestBody Viagens viagem) {
        viagem.setId(id);
        return viagensRepository.save(viagem);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Integer id) {
        passageiroRepository.deleteAllByIdViagem(id);
        viagensRepository.deleteById(id);
    }
}