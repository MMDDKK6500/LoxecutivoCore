package br.dev.mmddkk.loxecutivo.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MotoristaTest {
    @Test
    void formatsCpfAndRgForApiResponses() {
        Motorista motorista = new Motorista();
        motorista.setCpf("39091495880");
        motorista.setRg("552265688");

        assertEquals("390.914.958-80", motorista.getCpfFormatado());
        assertEquals("55.226.568-8", motorista.getRgFormatado());
    }

    @Test
    void preservesInvalidDocumentValuesInsteadOfInventingFormatting() {
        Motorista motorista = new Motorista();
        motorista.setCpf("123");
        motorista.setRg("abc");

        assertEquals("123", motorista.getCpfFormatado());
        assertEquals("abc", motorista.getRgFormatado());
    }
}
