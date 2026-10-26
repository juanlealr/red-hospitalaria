package co.edu.uptc.red_hospitalaria.admisiones.infraestructura.entrada.controlador;

import co.edu.uptc.red_hospitalaria.admisiones.aplicacion.AdmisionUseCase;
import co.edu.uptc.red_hospitalaria.admisiones.dominio.Admision;
import co.edu.uptc.red_hospitalaria.admisiones.dominio.DisponibilidadCama;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admisiones")
public class AdmisionController {

    private final AdmisionUseCase admisionUseCase;

    public AdmisionController(AdmisionUseCase admisionUseCase) {
        this.admisionUseCase = admisionUseCase;
    }

    @PostMapping("/ingreso")
    public ResponseEntity<Admision> registrarIngreso(
            @RequestParam String pacienteId,
            @RequestParam String sedeId,
            @RequestBody DisponibilidadCama disponibilidadSede) {
        Admision admision = admisionUseCase.registrarIngreso(pacienteId, sedeId, disponibilidadSede);
        return ResponseEntity.ok(admision);
    }

    @GetMapping
    public ResponseEntity<List<Admision>> listarTodas() {
        return ResponseEntity.ok(admisionUseCase.listarTodas());
    }
}
