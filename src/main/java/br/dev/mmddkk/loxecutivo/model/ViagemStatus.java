package br.dev.mmddkk.loxecutivo.model;

public enum ViagemStatus {
    AGENDADA,
    REALIZADA,
    CANCELADA;

    public boolean permiteTransicaoPara(ViagemStatus destino) {
        return switch (this) {
            case AGENDADA -> destino == REALIZADA || destino == CANCELADA;
            case CANCELADA, REALIZADA -> destino == AGENDADA;
        };
    }
}
