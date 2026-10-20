package br.dev.mmddkk.loxecutivo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "viagens")
public class Viagens {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_viagem", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "local_de_origem", nullable = false)
    private Endereco localDeOrigem;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "local_de_destino", nullable = false)
    private Endereco localDeDestino;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_motorista", nullable = false)
    private Motorista idMotorista;

    @Size(max = 7)
    @NotNull
    @Column(name = "id_veiculo", nullable = false, length = 7)
    private String idVeiculo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_evento")
    private Evento idEvento;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Endereco getLocalDeOrigem() {
        return localDeOrigem;
    }

    public void setLocalDeOrigem(Endereco localDeOrigem) {
        this.localDeOrigem = localDeOrigem;
    }

    public Endereco getLocalDeDestino() {
        return localDeDestino;
    }

    public void setLocalDeDestino(Endereco localDeDestino) {
        this.localDeDestino = localDeDestino;
    }

    public Motorista getIdMotorista() {
        return idMotorista;
    }

    public void setIdMotorista(Motorista idMotorista) {
        this.idMotorista = idMotorista;
    }

    public String getIdVeiculo() {
        return idVeiculo;
    }

    public void setIdVeiculo(String idVeiculo) {
        this.idVeiculo = idVeiculo;
    }

    public Evento getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(Evento idEvento) {
        this.idEvento = idEvento;
    }

}