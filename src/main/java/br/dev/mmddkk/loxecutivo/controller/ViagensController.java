package br.dev.mmddkk.loxecutivo.controller;

import br.dev.mmddkk.loxecutivo.model.Veiculo;
import br.dev.mmddkk.loxecutivo.model.Viagens;
import br.dev.mmddkk.loxecutivo.repository.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@RequestMapping("/viagens")
public class ViagensController {

    @Autowired
    private ViagensRepository viagensRepository;
    @Autowired
    private EnderecoRepository enderecoRepository;
    @Autowired
    private MotoristaRepository motoristaRepository;
    @Autowired
    private VeiculoRepository veiculoRepository;
    @Autowired
    private EventoRepository eventoRepository;
    @Autowired
    private PassageiroRepository passageiroRepository;

    @PostMapping
    public ModelAndView salvar(@Valid @ModelAttribute Viagens viagens, BindingResult result) {

        if (result.hasErrors()) {
            System.out.println("Erro de validação: " + result.getAllErrors());

            String redirectUrl = "redirect:/";
            if (viagens.getIdEvento() != null && viagens.getIdEvento().getId() != null) {
                redirectUrl += viagens.getIdEvento().getId();
            }
            return new ModelAndView(redirectUrl);
        }

        viagensRepository.save(viagens);

        if (viagens.getIdEvento() != null && viagens.getIdEvento().getId() != null) {
            return new ModelAndView("redirect:/" + viagens.getIdEvento().getId());
        }

        return new ModelAndView("redirect:/");
    }

    @PostMapping("/atualizar/{id}")
    public ModelAndView atualizar(@PathVariable Integer id,
                                  @Valid @ModelAttribute Viagens viagens,
                                  BindingResult result) {

        if (result.hasErrors()) {
            return new ModelAndView("viagens/form");
        }

        viagens.setId(id);
        viagensRepository.save(viagens);

        String redirectUrl = "redirect:/";
        if (viagens.getIdEvento() != null) {
            redirectUrl += viagens.getIdEvento().getId();
        }
        return new ModelAndView(redirectUrl);
    }

    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable Integer id) {

        Viagens viagem = viagensRepository.findById(id).orElseThrow();

        passageiroRepository.deleteAllByIdViagem(id);

        viagensRepository.deleteById(id);

        String redirectUrl = "redirect:/";
        if (viagem.getIdEvento() != null) {
            redirectUrl += viagem.getIdEvento().getId();
        }
        return redirectUrl;
    }

    @PostMapping("/vincular-veiculo")
    public String vincularVeiculo(@RequestParam Integer idViagem, @RequestParam String idPlaca) {

        Viagens viagem = viagensRepository.findById(idViagem).orElseThrow();
        Veiculo veiculo = veiculoRepository.findById(idPlaca).orElseThrow();


        viagem.setIdVeiculo(veiculo.getIdPlaca());
        viagensRepository.save(viagem);


        String redirectUrl = "redirect:/";
        if (viagem.getIdEvento() != null) {
            redirectUrl += viagem.getIdEvento().getId() + "/" + viagem.getId();
        }
        return redirectUrl;
    }
}