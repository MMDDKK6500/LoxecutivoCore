package br.dev.mmddkk.loxecutivo.controller;

import br.dev.mmddkk.loxecutivo.model.Endereco;
import br.dev.mmddkk.loxecutivo.model.Evento;
import br.dev.mmddkk.loxecutivo.repository.EventoRepository;
import br.dev.mmddkk.loxecutivo.repository.EnderecoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@RequestMapping("/eventos")
public class EventoController {

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @PostMapping
    public ModelAndView salvar(@Valid @ModelAttribute Evento evento, BindingResult result) {

        if (result.hasErrors()) {
            ModelAndView mv = new ModelAndView("evento/form");
            mv.addObject("enderecos", enderecoRepository.findAll());
            return mv;
        }

        eventoRepository.save(evento);

        return new ModelAndView("redirect:/");
    }

    @PostMapping("/atualizar/{id}")
    public ModelAndView atualizar(@PathVariable Integer id, @Valid @ModelAttribute Evento evento, BindingResult result) {

        if (result.hasErrors()) {
            ModelAndView mv = new ModelAndView("evento/form");
            mv.addObject("enderecos", enderecoRepository.findAll());
            return mv;
        }

        evento.setId(id);
        eventoRepository.save(evento);

        return new ModelAndView("redirect:/");
    }

    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable Integer id) {
        eventoRepository.deleteById(id);
        return "redirect:/";
    }
}