package co.edu.uptc.red_hospitalaria.admisiones.infraestructura.salida.persistencia;

import co.edu.uptc.red_hospitalaria.admisiones.dominio.Admision;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Detalle técnico (paso 3 del taller de Hexagonal): igual que
 * InvestigadorRepository en RICA, esta interfaz sigue existiendo para que
 * Spring Data JPA genere la implementación por reflexión. Nadie fuera de
 * AdmisionRepositoryJpaAdapter debería conocer esta interfaz — el núcleo
 * (paquete aplicacion) solo conoce RepositorioAdmisiones.
 */
public interface AdmisionJpaRepository extends JpaRepository<Admision, String> {
}
