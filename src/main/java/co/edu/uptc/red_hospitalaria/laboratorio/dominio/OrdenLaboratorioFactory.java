package co.edu.uptc.red_hospitalaria.laboratorio.dominio;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class OrdenLaboratorioFactory {

    public OrdenLaboratorio crear(
            UUID pacienteId,
            String tipoExamen
    ) {

        Objects.requireNonNull(
                pacienteId,
                "El id del paciente es obligatorio."
        );

        if (tipoExamen == null || tipoExamen.isBlank()) {
            throw new IllegalArgumentException(
                    "El tipo de examen es obligatorio."
            );
        }

        return new OrdenLaboratorio(
                UUID.randomUUID(),
                pacienteId,
                tipoExamen,
                LocalDateTime.now()
        );
    }
}
