package co.edu.uptc.red_hospitalaria.farmacia.aplicacion;

import co.edu.uptc.red_hospitalaria.farmacia.dominio.SolicitudMedicamento;

import java.util.List;

/**
 * Puerto primario (driving) del subdominio Farmacia y cobertura.
 *
 * Es el contrato que ofrece el nucleo; cualquier adaptador primario
 * (un controlador REST, una tarea programada o una prueba) programa
 * contra esta interfaz, nunca contra SolicitudMedicamentoService.
 */
public interface SolicitudMedicamentoUseCase {

    SolicitudMedicamento recibir(String pacienteId, String medicoId, String medicamento);

    SolicitudMedicamento verificarCobertura(String solicitudId);

    SolicitudMedicamento despachar(String solicitudId);

    SolicitudMedicamento buscarPorId(String solicitudId);

    List<SolicitudMedicamento> listarTodas();
}
