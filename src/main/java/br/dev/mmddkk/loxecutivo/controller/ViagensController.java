package br.dev.mmddkk.loxecutivo.controller;

import br.dev.mmddkk.loxecutivo.model.Endereco;
import br.dev.mmddkk.loxecutivo.model.Evento;
import br.dev.mmddkk.loxecutivo.model.Motorista;
import br.dev.mmddkk.loxecutivo.model.Veiculo;
import br.dev.mmddkk.loxecutivo.model.Viagens;
import br.dev.mmddkk.loxecutivo.repository.EnderecoRepository;
import br.dev.mmddkk.loxecutivo.repository.EventoRepository;
import br.dev.mmddkk.loxecutivo.repository.MotoristaRepository;
import br.dev.mmddkk.loxecutivo.repository.VeiculoRepository;
import br.dev.mmddkk.loxecutivo.repository.PassageiroRepository;
import br.dev.mmddkk.loxecutivo.repository.ViagensRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
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

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private MotoristaRepository motoristaRepository;

    @Autowired
    private VeiculoRepository veiculoRepository;


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

    @Transactional
    @PostMapping
    public Viagens salvar(@Valid @RequestBody Viagens viagem) {
        return viagensRepository.save(resolverAssociacoes(viagem));
    }

    @Transactional
    @PutMapping("/{id}")
    public Viagens atualizar(@PathVariable Integer id, @Valid @RequestBody Viagens viagem) {
        viagem.setId(id);
        return viagensRepository.save(resolverAssociacoes(viagem));
    }

    private Viagens resolverAssociacoes(Viagens viagem) {
        Endereco origem = viagem.getLocalDeOrigem();
        if (origem == null || origem.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A origem deve referenciar um endereço existente.");
        }
        viagem.setLocalDeOrigem(enderecoRepository.findById(origem.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Endereço de origem não encontrado.")));

        Endereco destino = viagem.getLocalDeDestino();
        if (destino == null || destino.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O destino deve referenciar um endereço existente.");
        }
        viagem.setLocalDeDestino(enderecoRepository.findById(destino.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Endereço de destino não encontrado.")));

        Motorista motorista = viagem.getIdMotorista();
        if (motorista == null || motorista.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A viagem deve referenciar um motorista existente.");
        }
        viagem.setIdMotorista(motoristaRepository.findById(motorista.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Motorista não encontrado.")));

        if (viagem.getIdVeiculo() == null || !veiculoRepository.existsById(viagem.getIdVeiculo())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Veículo não encontrado.");
        }

        Evento evento = viagem.getIdEvento();
        if (evento != null) {
            if (evento.getId() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A viagem deve referenciar um evento existente.");
            }
            viagem.setIdEvento(eventoRepository.findById(evento.getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento não encontrado.")));
        }

        return viagem;
    }

    @Transactional
    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Integer id) {
        passageiroRepository.deleteAllByIdViagem(id);
        viagensRepository.deleteById(id);
    }
}
