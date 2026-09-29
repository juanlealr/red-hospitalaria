package co.edu.uptc.red_hospitalaria.farmacia.aplicacion;

import co.edu.uptc.red_hospitalaria.farmacia.dominio.Cobertura;

/**
 * Puerto secundario hacia el sistema externo de la aseguradora (HU-07).
 *
 * Existe para cumplir dos requisitos no funcionales del proyecto: "la
 * lógica clínica del hospital no debe depender de la tecnología específica
 * de cada integración externa" y "la integración con servicios externos no
 * debe bloquear la operación clínica interna si esos servicios fallan". El
 * adaptador real (cliente HTTP/REST contra la aseguradora) se implementará
 * en la fase de integración; mientras tanto el contexto usa
 * CoberturaAseguradoraStubAdapter.
 */
public interface ConsultaCoberturaExterna {

    Cobertura consultar(String pacienteId);
}
