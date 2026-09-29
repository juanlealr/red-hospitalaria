package co.edu.uptc.red_hospitalaria.farmacia.aplicacion;

import co.edu.uptc.red_hospitalaria.farmacia.dominio.SolicitudMedicamento;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Factory (paso 5): mueve aquí la validación y construcción de la raíz del
 * Agregado, igual que InvestigadorFactory en el taller de RICA.
 *
 * @Component: Spring la gestiona como bean, y nada fuera de esta clase
 * debería construir una SolicitudMedicamento directamente.
 *
 * Cubre HU-06 ("recibir ... solicitudes de medicamentos") y refuerza la
 * regla "un medicamento no puede despacharse sin una solicitud médica
 * asociada": sin medicoId no hay solicitud posible. El id se genera aquí
 * con UUID, igual que AdmisionFactory.
 */
@Component
public class SolicitudMedicamentoFactory {

    public SolicitudMedicamento recibirSolicitud(String pacienteId, String medicoId, String medicamento) {
        if (pacienteId == null || pacienteId.isBlank()) {
            throw new IllegalArgumentException("Se requiere el id del paciente para recibir la solicitud de medicamento.");
        }
        if (medicoId == null || medicoId.isBlank()) {
            throw new IllegalArgumentException(
                    "Se requiere la solicitud médica asociada (id del médico): un medicamento no se despacha sin ella.");
        }
        if (medicamento == null || medicamento.isBlank()) {
            throw new IllegalArgumentException("Se requiere el medicamento solicitado.");
        }

        String id = UUID.randomUUID().toString();
        return new SolicitudMedicamento(id, pacienteId, medicoId, medicamento, LocalDateTime.now());
    }
}
