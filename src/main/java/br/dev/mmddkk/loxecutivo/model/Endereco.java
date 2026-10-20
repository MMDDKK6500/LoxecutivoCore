package br.dev.mmddkk.loxecutivo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

@Entity
@Table(name = "enderecos")
public class Endereco implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_endereco", nullable = false)
    private Integer id;

    @Size(max = 50)
    @NotBlank
    @Column(name = "rua", nullable = false, length = 50)
    private String rua;

    @NotNull
    @Min(1)
    @Column(name = "numero", nullable = false)
    private Integer numero;

    @Size(max = 20)
    @NotBlank
    @Column(name = "bairro", nullable = false, length = 20)
    private String bairro;

    @Size(max = 20)
    @NotBlank
    @Column(name = "cidade", nullable = false, length = 20)
    private String cidade;

    @NotBlank
    @Pattern(regexp = "[A-Za-zÀ-ÿ]{2}")
    @Lob
    @Column(name = "uf", nullable = false)
    private String uf;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

}
