package br.dev.mmddkk.loxecutivo.controller;

import br.dev.mmddkk.loxecutivo.model.*;
import br.dev.mmddkk.loxecutivo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
public class IndexController {

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private EventoRepository eventoRepository;
    @Autowired
    private ViagensRepository viagensRepository;
    @Autowired
    private VeiculoRepository veiculoRepository;
    @Autowired
    private MotoristaRepository motoristaRepository;
    @Autowired
    private PassageiroRepository passageiroRepository;

    @RequestMapping(value = {"/", "/{idEvento:\\d+}", "/{idEvento:\\d+}/{idViagem:\\d+}", "/{idEvento:\\d+}/{idViagem:\\d+}/{idPassageiro:\\d+}"})
    public ModelAndView index(
            @PathVariable("idEvento") Optional<Integer> idEvento,
            @PathVariable("idViagem") Optional<Integer> idViagem,
            @PathVariable("idPassageiro") Optional<Integer> idPassageiro) {

        ModelAndView mv = new ModelAndView("index");

        Evento eventoAtivo = idEvento.flatMap(eventoRepository::findById).orElse(new Evento());
        if (eventoAtivo.getIdEndereco() == null) eventoAtivo.setIdEndereco(new Endereco());

        if (eventoAtivo.getId() != null) {
            List<Viagens> viagensDoEvento = viagensRepository.findByIdEvento(eventoAtivo);
            mv.addObject("viagensDoEvento", viagensDoEvento);
        }

        if (idViagem.isPresent()) {
                viagensRepository.findById(idViagem.get()).ifPresent(v -> {
                    mv.addObject("viagemSelecionada", v);

                    mv.addObject("passageirosDaViagem", passageiroRepository.findByIdViagem(v));

                    if (v.getIdVeiculo() != null && !v.getIdVeiculo().isEmpty()) {
                        veiculoRepository.findById(v.getIdVeiculo()).ifPresent(veiculo ->
                                mv.addObject("veiculoSelecionado", veiculo)
                        );
                    }
                });
        }

        if (idPassageiro.isPresent()) {
            Optional<Passageiro> editarPassageiro = passageiroRepository.findById(idPassageiro.get());

            if (editarPassageiro.isPresent()) {
                mv.addObject("editarPassageiro", editarPassageiro.get());
            }
        }

        mv.addObject("novoVeiculo", new Veiculo());
        mv.addObject("novaViagem", new Viagens());

        Passageiro pass = new Passageiro();
        pass.setEmpresa("");
        mv.addObject("novoPassageiro", pass);

        mv.addObject("novoMotorista", new Motorista());
        mv.addObject("novoEndereco", new Endereco());


        mv.addObject("listaVeiculos", veiculoRepository.findAll());
        mv.addObject("listaMotoristas", motoristaRepository.findAll());
        mv.addObject("listaEnderecos", enderecoRepository.findAll());

        mv.addObject("evento", eventoAtivo);
        mv.addObject("listaEventos", eventoRepository.findAll());

        return mv;
    }

}
