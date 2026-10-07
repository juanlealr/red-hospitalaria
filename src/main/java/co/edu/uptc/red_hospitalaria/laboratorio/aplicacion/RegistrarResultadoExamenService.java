package co.edu.uptc.red_hospitalaria.laboratorio.aplicacion;

import co.edu.uptc.red_hospitalaria.laboratorio.dominio.OrdenLaboratorio;
import co.edu.uptc.red_hospitalaria.laboratorio.dominio.ResultadoExamen;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;

@Service
public class RegistrarResultadoExamenService {

    private final RepositorioOrdenesLaboratorio repositorioOrdenesLaboratorio;

    public RegistrarResultadoExamenService(
            RepositorioOrdenesLaboratorio repositorioOrdenesLaboratorio) {

        this.repositorioOrdenesLaboratorio =
                repositorioOrdenesLaboratorio;
    }

    public void registrar(UUID ordenId, ResultadoExamen resultado) {

        Objects.requireNonNull(
                ordenId,
                "El id de la orden es obligatorio."
        );

        Objects.requireNonNull(
                resultado,
                "El resultado del examen es obligatorio."
        );

        OrdenLaboratorio orden =
                repositorioOrdenesLaboratorio.buscarPorId(ordenId)
                        .orElseThrow(() ->
                                new NoSuchElementException(
                                        "No existe una orden de laboratorio con id "
                                                + ordenId
                                )
                        );

        orden.registrarResultado(resultado);

        repositorioOrdenesLaboratorio.guardar(orden);
    }
}