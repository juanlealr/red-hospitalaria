package co.edu.uptc.red_hospitalaria.laboratorio.dominio;

import co.edu.uptc.red_hospitalaria.laboratorio.aplicacion.CierreOrdenLaboratorioService;
import co.edu.uptc.red_hospitalaria.laboratorio.aplicacion.RepositorioOrdenesLaboratorio;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CierreOrdenLaboratorioServiceTest {

    @Test
    void debeCerrarOrdenConResultadoUsandoFake() {

        // Fake del repositorio secundario
        FakeRepositorioOrdenesLaboratorio fake =
                new FakeRepositorioOrdenesLaboratorio();

        // Se crea el caso de uso usando únicamente el puerto
        CierreOrdenLaboratorioService servicio =
                new CierreOrdenLaboratorioService(fake);

        UUID ordenId = UUID.randomUUID();
        UUID pacienteId = UUID.randomUUID();

        // Se crea una orden
        OrdenLaboratorio orden = new OrdenLaboratorio(
                ordenId,
                pacienteId,
                "Hemograma",
                LocalDateTime.now()
        );

        // La orden necesita un resultado antes de poder cerrarse
        ResultadoExamen resultado =
                new ResultadoExamen("120", "g/L");

        orden.registrarResultado(resultado);

        // Se almacena en el Fake
        fake.guardar(orden);

        // Se ejecuta el caso de uso principal
        servicio.cerrar(ordenId);

        // Se consulta nuevamente la orden
        OrdenLaboratorio ordenGuardada =
                fake.buscarPorId(ordenId).orElseThrow();

        // Verificamos que realmente quedó cerrada
        assertEquals(
                EstadoOrdenLaboratorio.CERRADA,
                ordenGuardada.getEstado()
        );
    }

    /**
     * Fake hecho manualmente para la prueba.
     * No utiliza Spring, JPA ni Mockito.
     */
    private static class FakeRepositorioOrdenesLaboratorio
            implements RepositorioOrdenesLaboratorio {

        private final Map<UUID, OrdenLaboratorio> ordenes =
                new HashMap<>();

        @Override
        public OrdenLaboratorio guardar(OrdenLaboratorio orden) {
            ordenes.put(orden.getId(), orden);
            return orden;
        }

        @Override
        public Optional<OrdenLaboratorio> buscarPorId(UUID id) {
            return Optional.ofNullable(ordenes.get(id));
        }
    }
}