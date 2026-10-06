package co.edu.uptc.red_hospitalaria.laboratorio.aplicacion;

import co.edu.uptc.red_hospitalaria.laboratorio.dominio.OrdenLaboratorio;

import java.util.Optional;
import java.util.UUID;

public interface RepositorioOrdenesLaboratorio {

    OrdenLaboratorio guardar(OrdenLaboratorio orden);

    Optional<OrdenLaboratorio> buscarPorId(UUID id);
}