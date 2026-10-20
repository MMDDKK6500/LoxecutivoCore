package br.dev.mmddkk.loxecutivo.controller;

import br.dev.mmddkk.loxecutivo.model.Evento;
import br.dev.mmddkk.loxecutivo.repository.EventoRepository;
import br.dev.mmddkk.loxecutivo.repository.EnderecoRepository;
import br.dev.mmddkk.loxecutivo.repository.PassageiroRepository;
import br.dev.mmddkk.loxecutivo.repository.ViagensRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;

@RestController
@RequestMapping("/eventos")
public class EventoController {

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private ViagensRepository viagensRepository;

    @Autowired
    private PassageiroRepository passageiroRepository;

    @GetMapping
    public Iterable<Evento> listarTodos() {
        return eventoRepository.findAll();
    }

    @GetMapping("/{id}")
    public Evento buscarPorId(@PathVariable Integer id) {
        return eventoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento não encontrado."));
    }

    @PostMapping
    @Transactional
    public Evento salvar(@Valid @RequestBody Evento evento) {
        return eventoRepository.save(resolverEndereco(evento));
    }

    @PutMapping("/{id}")
    @Transactional
    public Evento atualizar(@PathVariable Integer id, @Valid @RequestBody Evento evento) {
        evento.setId(id);
        return eventoRepository.save(resolverEndereco(evento));
    }

    private Evento resolverEndereco(Evento evento) {
        if (evento.getIdEndereco() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O evento deve referenciar um endereço existente.");
        }
        if (evento.getIdEndereco().getId() != null) {
            evento.setIdEndereco(enderecoRepository.findById(evento.getIdEndereco().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Endereço do evento não encontrado.")));
        }
        return evento;
    }

    @Transactional
    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Integer id) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento não encontrado."));
        viagensRepository.findByIdEvento(evento).forEach(viagem -> {
            passageiroRepository.deleteAllByIdViagem(viagem.getId());
            viagensRepository.deleteById(viagem.getId());
        });
        eventoRepository.delete(evento);
    }
}
