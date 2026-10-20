package br.dev.mmddkk.loxecutivo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "motoristas")
public class Motorista {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_motorista", nullable = false)
    private Integer id;

    @Size(max = 30)
    @NotBlank
    @Pattern(regexp = "[\\p{L}][\\p{L} '-]*")
    @Column(name = "nome", nullable = false, length = 30)
    private String nome;

    @Size(max = 60)
    @NotBlank
    @Pattern(regexp = "[\\p{L}][\\p{L} '-]*")
    @Column(name = "sobrenome", nullable = false, length = 60)
    private String sobrenome;

    @Size(max = 9)
    @NotBlank
    @Pattern(regexp = "\\d{9}")
    @Column(name = "rg", nullable = false, length = 9)
    private String rg;

    @Size(max = 11)
    @NotBlank
    @Pattern(regexp = "\\d{11}")
    @Column(name = "cpf", nullable = false, length = 11)
    private String cpf;

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
