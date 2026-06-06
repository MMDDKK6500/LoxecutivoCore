package br.dev.mmddkk.loxecutivo.controller;

import br.dev.mmddkk.loxecutivo.model.Veiculo;
import br.dev.mmddkk.loxecutivo.repository.VeiculoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@RequestMapping("/veiculos")
public class VeiculoController {

    @Autowired
    private VeiculoRepository veiculoRepository;

    @PostMapping("/{eventoId}")
    public ModelAndView salvar(@Valid @ModelAttribute Veiculo veiculo,
                               @PathVariable Integer eventoId,
                               BindingResult result) {

        if (result.hasErrors()) {
            return new ModelAndView("veiculo/form");
        }

        veiculoRepository.save(veiculo);
        return new ModelAndView("redirect:/" + eventoId + "?modal=modalViagem");
    }


    @PostMapping("/atualizar/{id}")
    public ModelAndView atualizar(@PathVariable String id,
                                  @Valid @ModelAttribute Veiculo veiculo,
                                  BindingResult result) {

        if (result.hasErrors()) {
            return new ModelAndView("veiculo/form");
        }

        veiculo.setIdPlaca(id);
        veiculoRepository.save(veiculo);

        return new ModelAndView("redirect:/");
    }


    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable String id) {
        veiculoRepository.deleteById(id);
        return "redirect:/";
    }
}