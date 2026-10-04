package co.edu.uptc.red_hospitalaria.farmacia.infraestructura.salida.aseguradora;

import co.edu.uptc.red_hospitalaria.farmacia.aplicacion.ConsultaCoberturaExterna;
import co.edu.uptc.red_hospitalaria.farmacia.dominio.Cobertura;
import org.springframework.stereotype.Component;

/**
 * Adaptador provisional (stub) del puerto ConsultaCoberturaExterna.
 *
 * Devuelve una cobertura fija del 80% para cualquier paciente. Existe para
 * que el contexto de Spring arranque y el flujo de Farmacia se pueda
 * probar de punta a punta mientras se implementa el cliente real de la
 * aseguradora (fase de integración con sistemas externos). Cuando ese
 * cliente exista, este archivo se elimina y el puerto queda implementado
 * por el adaptador real — el dominio no cambia.
 */
@Component
public class CoberturaAseguradoraStubAdapter implements ConsultaCoberturaExterna {

    @Override
    public Cobertura consultar(String pacienteId) {
        return new Cobertura(80);
    }
}
