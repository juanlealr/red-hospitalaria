package co.edu.uptc.red_hospitalaria.farmacia.infraestructura.salida.persistencia;

import co.edu.uptc.red_hospitalaria.farmacia.aplicacion.RepositorioSolicitudesMedicamento;
import co.edu.uptc.red_hospitalaria.farmacia.dominio.SolicitudMedicamento;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Adaptador secundario en memoria del puerto RepositorioSolicitudesMedicamento.
 *
 * Mantiene el dominio libre de jakarta.persistence y permite sustituirlo
 * por un adaptador JPA o por un cliente de otro almacen sin tocar el nucleo.
 */
@Repository
public class RepositorioSolicitudesMedicamentoMemoria implements RepositorioSolicitudesMedicamento {

    private final Map<String, SolicitudMedicamento> solicitudes = new HashMap<>();

    @Override
    public SolicitudMedicamento guardar(SolicitudMedicamento solicitud) {
        solicitudes.put(solicitud.id(), solicitud);
        return solicitud;
    }

    @Override
    public Optional<SolicitudMedicamento> buscarPorId(String id) {
        return Optional.ofNullable(solicitudes.get(id));
    }

    @Override
    public List<SolicitudMedicamento> listarTodas() {
        return new ArrayList<>(solicitudes.values());
    }
}
