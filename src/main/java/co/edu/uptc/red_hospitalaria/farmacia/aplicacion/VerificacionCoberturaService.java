package co.edu.uptc.red_hospitalaria.farmacia.aplicacion;

import co.edu.uptc.red_hospitalaria.farmacia.dominio.Cobertura;
import co.edu.uptc.red_hospitalaria.farmacia.dominio.SolicitudMedicamento;
import org.springframework.stereotype.Service;

/**
 * Servicio de Dominio (paso 3): VerificacionCoberturaService.
 *
 * Cubre HU-07: "verificar la cobertura del paciente ante una aseguradora
 * externa, para saber qué está cubierto". La operación no pertenece
 * naturalmente a SolicitudMedicamento (que no debería saber cómo se
 * consulta la aseguradora) ni a Cobertura (que no conoce la solicitud):
 * necesita a ambos, por eso es un Servicio de Dominio.
 *
 * @Service, igual que LimitePublicacionesAnualesService en el taller de
 * RICA: Spring lo gestiona como bean, y la aseguradora entra por el puerto
 * ConsultaCoberturaExterna — nunca como una dependencia concreta.
 */
@Service
public class VerificacionCoberturaService {

    private final ConsultaCoberturaExterna consultaCoberturaExterna;

    public VerificacionCoberturaService(ConsultaCoberturaExterna consultaCoberturaExterna) {
        this.consultaCoberturaExterna = consultaCoberturaExterna;
    }

    /**
     * Consulta la cobertura del paciente en la aseguradora externa y la
     * registra en la solicitud: a partir de este punto la solicitud queda
     * apta para despacho (evento CoberturaVerificada del Event Storming).
     */
    public Cobertura verificar(SolicitudMedicamento solicitud) {
        if (solicitud == null) {
            throw new IllegalArgumentException("La solicitud de medicamento es obligatoria.");
        }
        Cobertura cobertura = consultaCoberturaExterna.consultar(solicitud.pacienteId());
        solicitud.registrarCobertura(cobertura);
        return cobertura;
    }
}
