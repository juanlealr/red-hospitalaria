package co.edu.uptc.red_hospitalaria.laboratorio.aplicacion;

import co.edu.uptc.red_hospitalaria.laboratorio.dominio.OrdenLaboratorio;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;

@Service
public class CierreOrdenLaboratorioService
        implements CerrarOrdenLaboratorioUseCase {

    private final RepositorioOrdenesLaboratorio repositorioOrdenesLaboratorio;

    public CierreOrdenLaboratorioService(
            RepositorioOrdenesLaboratorio repositorioOrdenesLaboratorio) {

        this.repositorioOrdenesLaboratorio =
                repositorioOrdenesLaboratorio;
    }

    @Override
    public void cerrar(UUID ordenId) {

        Objects.requireNonNull(
                ordenId,
                "El id de la orden es obligatorio."
        );

        OrdenLaboratorio orden =
                repositorioOrdenesLaboratorio.buscarPorId(ordenId)
                        .orElseThrow(() ->
                                new NoSuchElementException(
                                        "No existe una orden de laboratorio con id "
                                                + ordenId
                                )
                        );

        orden.cerrar();

        repositorioOrdenesLaboratorio.guardar(orden);
    }
}