package co.edu.uptc.red_hospitalaria.farmacia.dominio;

/**
 * Estados posibles de una SolicitudMedicamento, dentro del subdominio
 * "Farmacia y cobertura".
 *
 * Lenguaje Ubicuo (paso 1): estos son exactamente los nombres con los que el
 * farmacéutico y el personal de farmacia describen el ciclo de una solicitud,
 * y coinciden uno a uno con los eventos de dominio que el equipo identificó
 * en el Event Storming (NOTAS-equipo.md, sección 1):
 * MedicamentoSolicitado → CoberturaVerificada → MedicamentoDespachado,
 * que cubren HU-06 y HU-07.
 */
public enum EstadoSolicitudMedicamento {

    /** La solicitud fue recibida por farmacia (HU-06, evento MedicamentoSolicitado). */
    RECIBIDA,

    /** La aseguradora externa ya confirmó qué porcentaje cubre (HU-07, evento CoberturaVerificada). */
    COBERTURA_VERIFICADA,

    /** El medicamento fue despachado al paciente (HU-06, evento MedicamentoDespachado). */
    DESPACHADA
}
