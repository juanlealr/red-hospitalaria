package co.edu.uptc.red_hospitalaria.laboratorio.dominio;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class OrdenLaboratorio {

    private final UUID id;
    private final UUID pacienteId;
    private final String tipoExamen;
    private final LocalDateTime fechaSolicitud;

    private EstadoOrdenLaboratorio estado;
    private ResultadoExamen resultado;

    OrdenLaboratorio(
            UUID id,
            UUID pacienteId,
            String tipoExamen,
            LocalDateTime fechaSolicitud
    ) {

        this.id = Objects.requireNonNull(
                id,
                "El id de la orden es obligatorio."
        );

        this.pacienteId = Objects.requireNonNull(
                pacienteId,
                "El id del paciente es obligatorio."
        );

        if (tipoExamen == null || tipoExamen.isBlank()) {
            throw new IllegalArgumentException(
                    "El tipo de examen es obligatorio."
            );
        }

        this.tipoExamen = tipoExamen.trim();

        this.fechaSolicitud = Objects.requireNonNull(
                fechaSolicitud,
                "La fecha de solicitud es obligatoria."
        );

        this.estado = EstadoOrdenLaboratorio.SOLICITADA;
        this.resultado = null;
    }

    public UUID getId() {
        return id;
    }

    public UUID getPacienteId() {
        return pacienteId;
    }

    public String getTipoExamen() {
        return tipoExamen;
    }

    public LocalDateTime getFechaSolicitud() {
        return fechaSolicitud;
    }

    public EstadoOrdenLaboratorio getEstado() {
        return estado;
    }

    public ResultadoExamen getResultado() {
        return resultado;
    }

    public boolean tieneResultado() {
        return resultado != null;
    }

    public void registrarResultado(ResultadoExamen resultado) {

        Objects.requireNonNull(
                resultado,
                "El resultado del examen es obligatorio."
        );

        if (estado == EstadoOrdenLaboratorio.CERRADA) {
            throw new IllegalStateException(
                    "No se puede registrar un resultado en una orden cerrada."
            );
        }

        this.resultado = resultado;
        this.estado = EstadoOrdenLaboratorio.CON_RESULTADO;
    }

    public void cerrar() {

        if (!tieneResultado()) {
            throw new IllegalStateException(
                    "No se puede cerrar una orden sin resultado registrado."
            );
        }

        if (estado == EstadoOrdenLaboratorio.CERRADA) {
            throw new IllegalStateException(
                    "La orden de laboratorio ya está cerrada."
            );
        }

        this.estado = EstadoOrdenLaboratorio.CERRADA;
    }
}
