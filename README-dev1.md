# Cómo integrar esto en el proyecto real (Dev 1)

Este paquete ya está generado con el package name que escogió el equipo:
**`co.edu.uptc.red_hospitalaria`**. Validado contra `Requisitos_G4.docx` y la
tabla oficial de G4.

## 1. Generar el esqueleto (si tu equipo aún no lo hizo)

En https://start.spring.io :

- Project: **Maven**
- Language: **Java**
- Spring Boot: **4.1.1**
- Packaging: **Jar**
- Java: **25**
- Dependencia inicial: **Spring Web**
- Group: `co.edu.uptc`
- Artifact / Name: `red-hospitalaria` (el guion es válido aquí; Spring
  Initializr genera el package name reemplazándolo automáticamente por
  guion bajo → `co.edu.uptc.red_hospitalaria`, que es el que ya usamos)

Dentro del paquete base, crear de una vez los paquetes vacíos: `admisiones`,
`laboratorio`, `farmacia`, `compartido`.

## 2. Ubicar estos archivos

Copia tal cual la carpeta `codigo-dev1-admisiones/src/main/java/co/`
dentro de `src/main/java/` de tu proyecto (así queda
`src/main/java/co/edu/uptc/red_hospitalaria/admisiones/Admision.java`, etc.
**Todo directo dentro de `admisiones/`, sin subcarpeta `domain`** — es el
mismo patrón plano que usa RICA: `investigadores/` contiene `Investigador`,
`InvestigadorService`, `InvestigadorFactory`, `CorreoInstitucional`... todo
al mismo nivel (ver Tutorial 6, "Diseñar los límites de los módulos"). Ya no
hace falta ajustar el `package` porque coincide con el de tu equipo.

Haz lo mismo con `src/test/java/co/...` para las pruebas.

## 3. Clases incluidas (mapeadas a los 6 pasos de la sección 6)

| Archivo | Paso | Qué es |
|---|---|---|
| `EstadoAdmision.java` | 1 · Lenguaje Ubicuo | enum con los estados que usa el negocio |
| `DisponibilidadCama.java` | 2 · Value Object | record con validación en constructor compacto (sin `@Embeddable` todavía — ver nota abajo) |
| `Traslado.java` | (apoyo del Agregado) | Value Object del historial de traslados |
| `TrasladoPacienteService.java` | 3 · Servicio de Dominio | `@Service`, no traslada sin cama confirmada en destino |
| `Admision.java` | 4 · Límite del Agregado | raíz del agregado; constructor de paquete (solo la Factory crea instancias); mini-ADR en `NOTAS-equipo.md` sección 5 |
| `AdmisionFactory.java` | 5 · Factory | `@Component`, valida y construye `Admision` (HU-01) |

### Sobre `@Entity` y `@Embeddable`

En el taller individual de RICA, `Investigador` ya es `@Entity` y
`CorreoInstitucional` ya es `@Embeddable` — porque ese taller trabaja sobre
la copia de `rica-api` que **ya tiene JPA/PostgreSQL** desde el Tutorial 4
del mini-curso. Nuestro proyecto de grupo, en cambio, recién se creó con
**Tutorial 1** (solo dependencia `Spring Web`, sin JPA todavía — ver sección
5 de la guía de arranque). Por eso `Admision` y `DisponibilidadCama` son
clases Java simples por ahora: nada de `@Entity`/`@Embeddable` porque el
proyecto ni siquiera tiene la dependencia `spring-boot-starter-data-jpa` —
agregar esas anotaciones hoy ni compilaría. Eso llega solo cuando el equipo
repita el Tutorial 4 sobre su propio proyecto (la próxima semana, con
arquitectura hexagonal). No es un paso que te falte: es un paso que aún no toca.

## 4. Verificar en tu máquina

Abre el proyecto en IntelliJ y corre los tests (`Admision`, `AdmisionFactory`,
`DisponibilidadCama`, `TrasladoPacienteService`) — deben quedar en verde.
Toma la captura de esa ejecución para `evidencias/dev-1/capturas/`, igual
que pide la sección 6 de la guía.

## 5. Commits y Pull Request (secciones 6 y 7)

```bash
git checkout -b dev-1-admisiones
# ... copiar/ajustar archivos, correr tests ...
git add src/main/java/.../admisiones src/test/java/.../admisiones evidencias/dev-1
git commit -m "feat(admisiones): value object DisponibilidadCama"
git commit -m "feat(admisiones): servicio de dominio TrasladoPacienteService"
git commit -m "feat(admisiones): agregado Admision con limite documentado"
git commit -m "feat(admisiones): factory AdmisionFactory"
git push -u origin dev-1-admisiones
```

Abre el Pull Request de `dev-1-admisiones` hacia `main`. Recuerda lo que
revisa quien aprueba (sección 7): que el código no referencie una entidad
completa de otro subdominio (`OrdenLaboratorio`, `Paciente`) — en este
código eso ya se respeta: `darDeAlta` recibe un `boolean` ya resuelto, no la
entidad de Laboratorio.

## 6. Pendiente para completar HU-02 y HU-10 (opcional, fuera del alcance mínimo)

Si tu equipo quiere avanzar más allá de los 6 pasos mínimos, `DisponibilidadCama`
ya sirve como base para:
- HU-02: un endpoint que liste `DisponibilidadCama` por sede/servicio.
- HU-10: un reporte que sume `camasDisponibles()` de varias sedes.

Eso es responsabilidad de la capa de aplicación/infraestructura, que llega
la próxima semana con arquitectura hexagonal — no hace falta resolverlo hoy.
