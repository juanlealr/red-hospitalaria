package co.edu.uptc.red_hospitalaria.laboratorio.dominio;

// // import java.util.Objects;

public record ResultadoExamen(String valor, String unidad) {

    public ResultadoExamen {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(
                    "El valor del resultado es obligatorio."
            );
        }

        if (unidad == null || unidad.isBlank()) {
            throw new IllegalArgumentException(
                    "La unidad del resultado es obligatoria."
            );
        }

        valor = valor.trim();
        unidad = unidad.trim();
    }
}
