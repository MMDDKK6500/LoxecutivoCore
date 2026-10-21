package br.dev.mmddkk.loxecutivo.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ViagemStatusTest {
    @Test
    void scheduledTripsCanBeCompletedOrCancelled() {
        assertTrue(ViagemStatus.AGENDADA.permiteTransicaoPara(ViagemStatus.REALIZADA));
        assertTrue(ViagemStatus.AGENDADA.permiteTransicaoPara(ViagemStatus.CANCELADA));
    }

    @Test
    void cancelledTripsMustBeRescheduledBeforeCompletion() {
        assertTrue(ViagemStatus.CANCELADA.permiteTransicaoPara(ViagemStatus.AGENDADA));
        assertFalse(ViagemStatus.CANCELADA.permiteTransicaoPara(ViagemStatus.REALIZADA));
    }

    @Test
    void completedTripsCanOnlyBeReopenedForCorrection() {
        assertTrue(ViagemStatus.REALIZADA.permiteTransicaoPara(ViagemStatus.AGENDADA));
        assertFalse(ViagemStatus.REALIZADA.permiteTransicaoPara(ViagemStatus.CANCELADA));
    }
}
