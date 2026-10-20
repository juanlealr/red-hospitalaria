package co.edu.uptc.red_hospitalaria.farmacia.infraestructura.entrada.rest.dto;

/**
 * DTO de entrada del adaptador primario. Pertenece a infraestructura:
 * el nucleo nunca recibe esta forma de JSON directamente.
 */
public record RecibirSolicitudRequest(
        String pacienteId,
        String medicoId,
        String medicamento) {
}
