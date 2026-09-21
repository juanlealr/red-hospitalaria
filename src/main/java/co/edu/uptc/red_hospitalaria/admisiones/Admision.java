package co.edu.uptc.red_hospitalaria.admisiones;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

/**
 * Raíz del Agregado: Admision.
 *
 * Límite del Agregado (paso 4): todo lo que cambia junto y de forma
 * consistente vive aquí (estado de la admisión, sede actual, historial de
 * traslados). Admision NUNCA referencia entidades completas de otros
 * subdominios: al Paciente lo referencia solo por pacienteId, y para dar de
 * alta (HU-09) no conoce la entidad OrdenLaboratorio del subdominio de
 * Laboratorio — solo recibe un booleano ya resuelto por quien orquesta el
 * caso de uso (igual que Publicacion.investigadorCorreo en el ejemplo de RICA).
 *
 * El constructor es de paquete: solo AdmisionFactory puede crear una
 * Admision válida (paso 5).
 */
public class Admision {

    private final String id;
    private final String pacienteId;
    private String sedeActualId;
    private EstadoAdmision estado;
    private final LocalDateTime fechaIngreso;
    private LocalDateTime fechaAlta;
    private final List<Traslado> historialTraslados = new ArrayList<>();

    Admision(String id, String pacienteId, String sedeInicialId, LocalDateTime fechaIngreso) {
        this.id = id;
        this.pacienteId = pacienteId;
        this.sedeActualId = sedeInicialId;
        this.estado = EstadoAdmision.ACTIVA;
        this.fechaIngreso = fechaIngreso;
    }

    /**
     * Usado por TrasladoPacienteService una vez confirmó cama en destino (HU-03).
     */
    void registrarTraslado(String sedeDestinoId, LocalDateTime fecha) {
        if (estado != EstadoAdmision.ACTIVA) {
            throw new IllegalStateException("Solo se puede trasladar una admisión activa.");
        }
        historialTraslados.add(new Traslado(this.sedeActualId, sedeDestinoId, fecha));
        this.sedeActualId = sedeDestinoId;
    }

    /**
     * Alta médica y cierre de la admisión (HU-09).
     * Criterio de aceptación: se rechaza si el paciente tiene órdenes de
     * laboratorio pendientes sin resultado.
     */
    public void darDeAlta(boolean tieneOrdenesLaboratorioPendientes, LocalDateTime fecha) {
        if (estado == EstadoAdmision.DADA_DE_ALTA) {
            throw new IllegalStateException("Esta admisión ya fue dada de alta.");
        }
        if (tieneOrdenesLaboratorioPendientes) {
            throw new IllegalStateException(
                    "No se puede dar de alta: el paciente tiene órdenes de laboratorio pendientes.");
        }
        this.estado = EstadoAdmision.DADA_DE_ALTA;
        this.fechaAlta = fecha;
    }

    public String id() {
        return id;
    }

    public String pacienteId() {
        return pacienteId;
    }

    public String sedeActualId() {
        return sedeActualId;
    }

    public EstadoAdmision estado() {
        return estado;
    }

    public LocalDateTime fechaIngreso() {
        return fechaIngreso;
    }

    public LocalDateTime fechaAlta() {
        return fechaAlta;
    }

    public List<Traslado> historialTraslados() {
        return Collections.unmodifiableList(historialTraslados);
    }
}