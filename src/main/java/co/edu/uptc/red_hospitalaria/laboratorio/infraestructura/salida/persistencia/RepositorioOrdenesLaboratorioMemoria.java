package co.edu.uptc.red_hospitalaria.laboratorio.infraestructura;

import co.edu.uptc.red_hospitalaria.laboratorio.aplicacion.RepositorioOrdenesLaboratorio;
import co.edu.uptc.red_hospitalaria.laboratorio.dominio.OrdenLaboratorio;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class RepositorioOrdenesLaboratorioMemoria
        implements RepositorioOrdenesLaboratorio {

    private final Map<UUID, OrdenLaboratorio> ordenes = new HashMap<>();

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