package br.dev.mmddkk.loxecutivo.controller;

import br.dev.mmddkk.loxecutivo.model.Endereco;
import br.dev.mmddkk.loxecutivo.repository.EnderecoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@RequestMapping("/enderecos")
public class EnderecoController {

    @Autowired
    private EnderecoRepository enderecoRepository;

    @PostMapping
    public ModelAndView salvar(@Valid @ModelAttribute Endereco endereco, BindingResult result) {
        if (result.hasErrors()) {
            return new ModelAndView("redirect:/");
        }
        enderecoRepository.save(endereco);
        return new ModelAndView("redirect:/");
    }

    @PostMapping("/atualizar/{id}")
    public ModelAndView atualizar(@PathVariable Integer id,
                                  @Valid @ModelAttribute Endereco endereco,
                                  BindingResult result) {

        if (result.hasErrors()) {
            return new ModelAndView("endereco/form");
        }

        endereco.setId(id);
        enderecoRepository.save(endereco);

        return new ModelAndView("redirect:/enderecos");
    }


    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable Integer id) {
        enderecoRepository.deleteById(id);
        return "redirect:/enderecos";
    }
}