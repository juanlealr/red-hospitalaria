package co.edu.uptc.red_hospitalaria.farmacia.aplicacion;

import co.edu.uptc.red_hospitalaria.farmacia.dominio.SolicitudMedicamento;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Caso de uso concreto de Farmacia y cobertura.
 *
 * Orquesta el Agregado, la Factory, el Servicio de Dominio de cobertura y
 * el puerto secundario de persistencia. No conoce HTTP, ni JPA, ni la
 * tecnologia concreta de la aseguradora: todo entra por puertos.
 */
@Service
public class SolicitudMedicamentoService implements SolicitudMedicamentoUseCase {

    private final RepositorioSolicitudesMedicamento repositorio;
    private final SolicitudMedicamentoFactory factory;
    private final VerificacionCoberturaService verificacionCoberturaService;

    public SolicitudMedicamentoService(
            RepositorioSolicitudesMedicamento repositorio,
            SolicitudMedicamentoFactory factory,
            VerificacionCoberturaService verificacionCoberturaService) {
        this.repositorio = repositorio;
        this.factory = factory;
        this.verificacionCoberturaService = verificacionCoberturaService;
    }

    @Override
    public SolicitudMedicamento recibir(String pacienteId, String medicoId, String medicamento) {
        SolicitudMedicamento solicitud = factory.recibirSolicitud(pacienteId, medicoId, medicamento);
        return repositorio.guardar(solicitud);
    }

    @Override
    public SolicitudMedicamento verificarCobertura(String solicitudId) {
        SolicitudMedicamento solicitud = buscarPorId(solicitudId);
        verificacionCoberturaService.verificar(solicitud);
        return repositorio.guardar(solicitud);
    }

    @Override
    public SolicitudMedicamento despachar(String solicitudId) {
        SolicitudMedicamento solicitud = buscarPorId(solicitudId);
        solicitud.despachar(LocalDateTime.now());
        return repositorio.guardar(solicitud);
    }

    @Override
    public SolicitudMedicamento buscarPorId(String solicitudId) {
        if (solicitudId == null || solicitudId.isBlank()) {
            throw new IllegalArgumentException("El id de la solicitud es obligatorio.");
        }
        return repositorio.buscarPorId(solicitudId)
                .orElseThrow(() -> new NoSuchElementException(
                        "No existe una solicitud de medicamento con id " + solicitudId));
    }

    @Override
    public List<SolicitudMedicamento> listarTodas() {
        return repositorio.listarTodas();
    }
}
