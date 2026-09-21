package co.edu.uptc.red_hospitalaria.admisiones;

import java.time.LocalDateTime;

/**
 * Value Object: Traslado.
 *
 * Registra un movimiento de un paciente entre sedes, para cumplir el
 * criterio de aceptación de HU-03: "el historial del paciente conserva el
 * registro de todos sus traslados". Forma parte del Agregado Admision
 * (no tiene identidad ni se consulta por fuera de él).
 */
public record Traslado(
        String sedeOrigenId,
        String sedeDestinoId,
        LocalDateTime fecha) {

    public Traslado {
        if (sedeOrigenId == null || sedeOrigenId.isBlank()) {
            throw new IllegalArgumentException("La sede de origen es obligatoria en un traslado.");
        }
        if (sedeDestinoId == null || sedeDestinoId.isBlank()) {
            throw new IllegalArgumentException("La sede de destino es obligatoria en un traslado.");
        }
        if (sedeOrigenId.equals(sedeDestinoId)) {
            throw new IllegalArgumentException("No tiene sentido trasladar al paciente a la misma sede.");
        }
        if (fecha == null) {
            throw new IllegalArgumentException("El traslado debe tener fecha.");
        }
    }
}
