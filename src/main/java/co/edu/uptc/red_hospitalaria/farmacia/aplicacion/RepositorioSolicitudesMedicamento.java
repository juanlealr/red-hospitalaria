package co.edu.uptc.red_hospitalaria.farmacia.aplicacion;

import co.edu.uptc.red_hospitalaria.farmacia.dominio.SolicitudMedicamento;

import java.util.List;
import java.util.Optional;

/**
 * Puerto secundario (driven) del subdominio Farmacia y cobertura.
 *
 * Solo declara lo que el caso de uso necesita: guardar, buscar por id y
 * listar. No copia los metodos genericos de JpaRepository; el nucleo
 * define el contrato y la infraestructura se adapta a el.
 */
public interface RepositorioSolicitudesMedicamento {

    SolicitudMedicamento guardar(SolicitudMedicamento solicitud);

    Optional<SolicitudMedicamento> buscarPorId(String id);

    List<SolicitudMedicamento> listarTodas();
}
