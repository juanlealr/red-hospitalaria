package co.edu.uptc.red_hospitalaria.laboratorio;

import java.util.Objects;

public class CierreOrdenLaboratorioService {

    public void cerrar(OrdenLaboratorio orden) {

        Objects.requireNonNull(
                orden,
                "La orden de laboratorio es obligatoria."
        );

        if (!orden.tieneResultado()) {
            throw new IllegalStateException(
                    "No se puede cerrar una orden sin resultado registrado."
            );
        }

        orden.cerrar();
    }
}
