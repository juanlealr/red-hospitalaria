package co.edu.uptc.red_hospitalaria.farmacia.dominio;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SolicitudMedicamentoTest {

    private SolicitudMedicamento solicitudRecibida() {
        return new SolicitudMedicamento(
                "sol-1", "pac-1", "med-1", "Ibuprofeno 400 mg", LocalDateTime.of(2026, 9, 29, 8, 0));
    }

    @Test
    void iniciaRecibidaYSinCobertura() {
        var solicitud = solicitudRecibida();

        assertEquals(EstadoSolicitudMedicamento.RECIBIDA, solicitud.estado());
        assertNull(solicitud.cobertura());
        assertNull(solicitud.fechaDespacho());
    }

    @Test
    void noSePuedeDespacharSinCoberturaVerificada() {
        var solicitud = solicitudRecibida();

        assertThrows(IllegalStateException.class, () -> solicitud.despachar(LocalDateTime.now()));
    }

    @Test
    void registraCoberturaYPasaACoberturaVerificada() {
        var solicitud = solicitudRecibida();

        solicitud.registrarCobertura(new Cobertura(80));

        assertEquals(EstadoSolicitudMedicamento.COBERTURA_VERIFICADA, solicitud.estado());
        assertEquals(new Cobertura(80), solicitud.cobertura());
    }

    @Test
    void despachaDespuesDeVerificarLaCobertura() {
        var solicitud = solicitudRecibida();
        solicitud.registrarCobertura(new Cobertura(80));
        var fechaDespacho = LocalDateTime.of(2026, 9, 29, 9, 30);

        solicitud.despachar(fechaDespacho);

        assertEquals(EstadoSolicitudMedicamento.DESPACHADA, solicitud.estado());
        assertEquals(fechaDespacho, solicitud.fechaDespacho());
    }

    @Test
    void noReemplazaLaCoberturaYaVerificada() {
        var solicitud = solicitudRecibida();
        solicitud.registrarCobertura(new Cobertura(80));

        assertThrows(IllegalStateException.class, () -> solicitud.registrarCobertura(new Cobertura(90)));
    }

    @Test
    void noDespachaDosVeces() {
        var solicitud = solicitudRecibida();
        solicitud.registrarCobertura(new Cobertura(80));
        solicitud.despachar(LocalDateTime.now());

        assertThrows(IllegalStateException.class, () -> solicitud.despachar(LocalDateTime.now()));
    }
}
