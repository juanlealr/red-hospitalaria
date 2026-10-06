package co.edu.uptc.red_hospitalaria.laboratorio.aplicacion;

import java.util.UUID;

public interface CerrarOrdenLaboratorioUseCase {

    void cerrar(UUID ordenId);
}