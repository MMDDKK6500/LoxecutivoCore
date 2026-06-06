package br.dev.mmddkk.loxecutivo.controller;

import br.dev.mmddkk.loxecutivo.model.Motorista;
import br.dev.mmddkk.loxecutivo.repository.MotoristaRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/motoristas")
public class MotoristaController {

    @Autowired
    private MotoristaRepository motoristaRepository;


    @PostMapping("/{eventoId}")
    public String salvar(@Valid @ModelAttribute Motorista motorista,
                               @PathVariable Integer eventoId,
                               BindingResult result,
                               RedirectAttributes attributes) {

        if (result.hasErrors()) {
            return "redirect:/" + eventoId;
        }

        motoristaRepository.save(motorista);

        return "redirect:/" + eventoId + "?modal=modalViagem";
    }


    @PostMapping("/atualizar/{id}")
    public ModelAndView atualizar(@PathVariable Integer id,
                                  @Valid @ModelAttribute Motorista motorista,
                                  BindingResult result) {

        if (result.hasErrors()) {
            return new ModelAndView("motorista/form");
        }

        motorista.setId(id);
        motoristaRepository.save(motorista);

        return new ModelAndView("redirect:/motoristas");
    }


    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable Integer id) {
        motoristaRepository.deleteById(id);
        return "redirect:/motoristas";
    }
}