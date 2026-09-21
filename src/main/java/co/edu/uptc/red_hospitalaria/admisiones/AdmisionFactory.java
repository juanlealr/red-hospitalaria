package co.edu.uptc.red_hospitalaria.admisiones;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Factory (paso 5): mueve aquí la validación y construcción de la raíz del
 * Agregado, igual que InvestigadorFactory en el taller de RICA.
 *
 * @Component, igual que InvestigadorFactory: Spring la gestiona como bean
 * (nada fuera de esta clase debería construir un Admision directamente —
 * el mismo principio que el taller aplica sobre InvestigadorFactory).
 *
 * Cubre HU-01: "registrar el ingreso de un paciente en una sede, para
 * iniciar su atención" — y de paso aplica la regla de negocio de que no se
 * puede admitir a alguien sin cama disponible confirmada en esa sede.
 *
 * Nota: en RICA, la Factory recibe el Repository para chequear duplicados
 * porque ya existe persistencia (Tutorial 4). Nuestro proyecto todavía está
 * en el Tutorial 1 (solo Spring Web, sin JPA), así que por ahora el id se
 * genera aquí con UUID y la disponibilidad de cama llega ya resuelta por
 * parámetro. Cuando el equipo llegue a la arquitectura hexagonal, este
 * método pasará a recibir un Repository inyectado, igual que
 * InvestigadorFactory.
 */
@Component
public class AdmisionFactory {

    public Admision registrarIngreso(String pacienteId, String sedeId, DisponibilidadCama disponibilidadSede) {
        if (pacienteId == null || pacienteId.isBlank()) {
            throw new IllegalArgumentException("Se requiere el id del paciente para registrar el ingreso.");
        }
        if (sedeId == null || sedeId.isBlank()) {
            throw new IllegalArgumentException("Se requiere la sede de ingreso.");
        }
        if (!disponibilidadSede.sedeId().equals(sedeId)) {
            throw new IllegalArgumentException(
                    "La disponibilidad de camas consultada no corresponde a la sede de ingreso.");
        }
        if (!disponibilidadSede.hayCamaConfirmada()) {
            throw new IllegalStateException("No se puede registrar el ingreso: no hay cama disponible en la sede.");
        }

        String id = UUID.randomUUID().toString();
        return new Admision(id, pacienteId, sedeId, LocalDateTime.now());
    }
}
