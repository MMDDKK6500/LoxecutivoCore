package br.dev.mmddkk.loxecutivo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "passageiros")
public class Passageiro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_passageiro", nullable = false)
    private Integer id;

    @Size(max = 30)
    @NotNull
    @Column(name = "nome", nullable = false, length = 30)
    private String nome;

    @Size(max = 60)
    @NotNull
    @Column(name = "sobrenome", nullable = false, length = 60)
    private String sobrenome;

    @Size(max = 14)
    @NotNull
    @Column(name = "numero", nullable = false, length = 14)
    private String numero;

    @Size(max = 60)
    @NotNull
    @Column(name = "empresa", nullable = false, length = 60)
    private String empresa;

    @Size(max = 9)
    @NotNull
    @Column(name = "rg", nullable = false, length = 9)
    private String rg;

    @Size(max = 11, min=11)
    @NotNull
    @Column(name = "cpf", nullable = false, length = 11)
    private String cpf;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_viagem")
    private Viagens idViagem;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Viagens getIdViagem() {
        return idViagem;
    }

    public void setIdViagem(Viagens idViagem) {
        this.idViagem = idViagem;
    }


    public String getCpfFormatado() {

        if (this.cpf != null && this.cpf.matches("\\d{11}")) {
            return this.cpf.replaceFirst("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
        }
        return this.cpf;
    }


    public String getRgFormatado() {

        if (this.rg != null && this.rg.matches("[0-9]{8}[0-9Xx]")) {
            return this.rg.replaceFirst("(\\d{2})(\\d{3})(\\d{3})([0-9Xx])", "$1.$2.$3-$4").toUpperCase();
        }
        return this.rg;
    }

}