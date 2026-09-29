package co.edu.uptc.red_hospitalaria.farmacia.dominio;

import java.time.LocalDateTime;

/**
 * Raíz del Agregado: SolicitudMedicamento.
 *
 * Es el único punto de entrada del subdominio "Farmacia y cobertura" y la
 * única clase del paquete dominio que llegará a ser @Entity cuando el
 * proyecto avance a persistencia (igual que Admision en admisiones y
 * Investigador en RICA). Límite del Agregado (mini-ADR en
 * NOTAS-equipo.md, sección 5.1): todo lo que cambia junto y de forma
 * consistente vive aquí — el estado de la solicitud (RECIBIDA →
 * COBERTURA_VERIFICADA → DESPACHADA) y la cobertura verificada.
 *
 * Referencias a otros subdominios: SIEMPRE por id, nunca por objeto
 * completo. Al paciente se lo referencia con pacienteId (su admisión es
 * responsabilidad de Admisiones) y al médico con medicoId (la solicitud
 * médica asociada). La regla "un medicamento no puede despacharse sin una
 * solicitud médica asociada" se cumple porque no existe forma de despachar
 * un medicamento por fuera de este Agregado, y la Factory exige el
 * medicoId para construirlo.
 *
 * Nota sobre la construcción: este constructor no valida — igual que en
 * Admision, su única llamada legítima es SolicitudMedicamentoFactory, que
 * es quien garantiza que nunca se cree una solicitud a medio armar.
 */
public class SolicitudMedicamento {

    private final String id;

    private final String pacienteId;

    private final String medicoId;

    private final String medicamento;

    private final LocalDateTime fechaSolicitud;

    private EstadoSolicitudMedicamento estado;

    private Cobertura cobertura;

    private LocalDateTime fechaDespacho;

    /**
     * Solo debe llamarse desde SolicitudMedicamentoFactory.
     */
    public SolicitudMedicamento(
            String id,
            String pacienteId,
            String medicoId,
            String medicamento,
            LocalDateTime fechaSolicitud) {
        this.id = id;
        this.pacienteId = pacienteId;
        this.medicoId = medicoId;
        this.medicamento = medicamento;
        this.fechaSolicitud = fechaSolicitud;
        this.estado = EstadoSolicitudMedicamento.RECIBIDA;
    }

    /**
     * Registra la cobertura que confirmó la aseguradora externa (HU-07).
     * Solo tiene sentido sobre una solicitud recién recibida: una vez
     * verificada, la cobertura no se reemplaza.
     */
    public void registrarCobertura(Cobertura cobertura) {
        if (cobertura == null) {
            throw new IllegalArgumentException("La cobertura verificada es obligatoria.");
        }
        if (estado != EstadoSolicitudMedicamento.RECIBIDA) {
            throw new IllegalStateException(
                    "Solo se registra la cobertura de una solicitud recibida (estado actual: " + estado + ").");
        }
        this.cobertura = cobertura;
        this.estado = EstadoSolicitudMedicamento.COBERTURA_VERIFICADA;
    }

    /**
     * Despacho del medicamento al paciente (HU-06). Exige que la cobertura
     * ya haya sido verificada — el orden que aplica VerificacionCoberturaService.
     */
    public void despachar(LocalDateTime fecha) {
        if (fecha == null) {
            throw new IllegalArgumentException("El despacho debe tener fecha.");
        }
        if (estado == EstadoSolicitudMedicamento.DESPACHADA) {
            throw new IllegalStateException("La solicitud ya fue despachada.");
        }
        if (estado != EstadoSolicitudMedicamento.COBERTURA_VERIFICADA) {
            throw new IllegalStateException(
                    "No se puede despachar sin cobertura verificada (estado actual: " + estado + ").");
        }
        this.estado = EstadoSolicitudMedicamento.DESPACHADA;
        this.fechaDespacho = fecha;
    }

    public String id() {
        return id;
    }

    public String pacienteId() {
        return pacienteId;
    }

    public String medicoId() {
        return medicoId;
    }

    public String medicamento() {
        return medicamento;
    }

    public LocalDateTime fechaSolicitud() {
        return fechaSolicitud;
    }

    public EstadoSolicitudMedicamento estado() {
        return estado;
    }

    public Cobertura cobertura() {
        return cobertura;
    }

    public LocalDateTime fechaDespacho() {
        return fechaDespacho;
    }
}
