package br.dev.mmddkk.loxecutivo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "veiculos")
public class Veiculo {
    @Id
    @Size(max = 7)
    @NotBlank
    @Pattern(regexp = "[A-Z0-9-]{1,7}")
    @Column(name = "id_placa", nullable = false, length = 7)
    private String idPlaca;

    @Size(max = 20)
    @NotBlank
    @Column(name = "modelo", nullable = false, length = 20)
    private String modelo;

    @NotNull
    @Min(1950)
    @Max(2100)
    @Column(name = "ano", nullable = false)
    private Integer ano;

    @Size(max = 20)
    @NotBlank
    @Column(name = "marca", nullable = false, length = 20)
    private String marca;

    @Size(max = 20)
    @NotBlank
    @Column(name = "cor", nullable = false, length = 20)
    private String cor;

    public String getIdPlaca() {
        return idPlaca;
    }

    public void setIdPlaca(String idPlaca) {
        this.idPlaca = idPlaca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

}
