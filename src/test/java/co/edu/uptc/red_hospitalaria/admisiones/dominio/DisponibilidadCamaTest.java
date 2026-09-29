package co.edu.uptc.red_hospitalaria.admisiones.dominio;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DisponibilidadCamaTest {

    @Test
    void calculaCamasDisponiblesCorrectamente() {
        var disponibilidad = new DisponibilidadCama("sede-tunja", "urgencias", 10, 7);

        assertEquals(3, disponibilidad.camasDisponibles());
        assertTrue(disponibilidad.hayCamaConfirmada());
    }

    @Test
    void noHayCamaConfirmadaCuandoEstaLlena() {
        var disponibilidad = new DisponibilidadCama("sede-tunja", "uci", 5, 5);

        assertFalse(disponibilidad.hayCamaConfirmada());
    }

    @Test
    void rechazaCamasOcupadasMayoresAlTotal() {
        assertThrows(IllegalArgumentException.class,
                () -> new DisponibilidadCama("sede-tunja", "uci", 5, 6));
    }

    @Test
    void rechazaValoresNegativos() {
        assertThrows(IllegalArgumentException.class,
                () -> new DisponibilidadCama("sede-tunja", "uci", -1, 0));
    }

    @Test
    void rechazaSedeVacia() {
        assertThrows(IllegalArgumentException.class,
                () -> new DisponibilidadCama(" ", "uci", 5, 0));
    }
}
