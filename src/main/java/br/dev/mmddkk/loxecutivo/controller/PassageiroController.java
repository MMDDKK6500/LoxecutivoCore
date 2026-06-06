package br.dev.mmddkk.loxecutivo.controller;

import br.dev.mmddkk.loxecutivo.model.Passageiro;
import br.dev.mmddkk.loxecutivo.model.Viagens;
import br.dev.mmddkk.loxecutivo.repository.PassageiroRepository;
import br.dev.mmddkk.loxecutivo.repository.ViagensRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@RequestMapping("/passageiros")
public class PassageiroController {

    @Autowired
    private PassageiroRepository passageiroRepository;
    @Autowired
    private ViagensRepository viagensRepository;

    @PostMapping
    public ModelAndView salvar(@Valid @ModelAttribute Passageiro passageiro, BindingResult result) {

        Viagens viagemCompleta = viagensRepository.findById(passageiro.getIdViagem().getId())
                .orElseThrow(() -> new RuntimeException("Viagem não encontrada"));

        int eventoId = viagemCompleta.getIdEvento().getId();
        int viagemId = viagemCompleta.getId();

        if (result.hasErrors()) {

            return new ModelAndView("redirect:/" + eventoId + "/" + viagemId + "?modal=modalPassageiro");

        }

        passageiroRepository.save(passageiro);

        return new ModelAndView("redirect:/" + eventoId + "/" + viagemId);
    }


    @PostMapping("/atualizar/{id}")
    public ModelAndView atualizar(@PathVariable Integer id,
                                  @Valid @ModelAttribute Passageiro passageiro,
                                  BindingResult result) {


        Viagens viagemCompleta = viagensRepository.findById(passageiro.getIdViagem().getId())
                .orElseThrow(() -> new RuntimeException("Viagem não encontrada"));

        int eventoId = viagemCompleta.getIdEvento().getId();
        int viagemId = viagemCompleta.getId();

        passageiro.setId(id);
        passageiroRepository.save(passageiro);

        if (result.hasErrors()) {
            return new ModelAndView("redirect:/" + eventoId + "/" + viagemId + "?modal=modalEditarPassageiro");
        }

        return new ModelAndView("redirect:/" + eventoId + "/" + viagemId);
    }


    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable Integer id) {

        Passageiro pass = passageiroRepository.findById(id).get();

        Viagens viagemCompleta = viagensRepository.findById(pass.getIdViagem().getId())
                .orElseThrow(() -> new RuntimeException("Viagem não encontrada"));

        int eventoId = viagemCompleta.getIdEvento().getId();
        int viagemId = viagemCompleta.getId();

        passageiroRepository.deleteById(id);

        return "redirect:/" + eventoId + "/" + viagemId;
    }
}