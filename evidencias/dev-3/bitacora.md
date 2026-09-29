# Bitácora — Dev 3 · Farmacia y cobertura

Subdominio asignado: **Farmacia y cobertura** (NOTAS-equipo.md, sección 4).

Artefactos del taller (sección 6 de la guía): `EstadoSolicitudMedicamento` (lenguaje ubicuo), `Cobertura` (Value Object), `VerificacionCoberturaService` (Servicio de Dominio), `SolicitudMedicamento` (raíz del Agregado) y `SolicitudMedicamentoFactory` (Factory).

Rama de trabajo: `dev-3-farmacia`.

| Paso | Actividad | Tiempo estimado (guía) | Tiempo real | Fecha | Commit (rama `dev-3-farmacia`) |
| --- | --- | --- | --- | --- | --- |
| 1 | Lenguaje Ubicuo — `EstadoSolicitudMedicamento` | 20 min | ~1 min | 28/09/2026 | `b1556e3` feat(farmacia): lenguaje ubicuo EstadoSolicitudMedicamento |
| 2 | Value Object — `Cobertura` (+ `CoberturaTest`) | 45 min | ~1 min | 28/09/2026 | `541dbef` feat(farmacia): value object Cobertura |
| 3 | Servicio de Dominio — `VerificacionCoberturaService` + puerto `ConsultaCoberturaExterna` (+ test) | 35 min | ~1 min | 28/09/2026 | `0e534d6` feat(farmacia): servicio de dominio VerificacionCoberturaService |
| 4 | Límite del Agregado — `SolicitudMedicamento` (+ test; mini-ADR en NOTAS-equipo.md, sección 5.1) | 15 min | ~1 min | 28/09/2026 | `e8aba07` feat(farmacia): agregado SolicitudMedicamento con limite documentado |
| 5 | Factory — `SolicitudMedicamentoFactory` (+ `SolicitudMedicamentoFactoryTest`) | 35 min | ~1 min | 28/09/2026 | `47f00f7` feat(farmacia): factory SolicitudMedicamentoFactory |
| 6 | Commit y push — rama y Pull Request hacia `main` | 5 min | ~12 min | 28/09/2026 | (abre este PR) |

**Notas de proceso**

- Tiempos reales: los cinco pasos se hicieron en una sola sesión continua — el historial de git tiene los commits de los pasos entre las 23:03 y las 23:06 del 28/09, y las capturas de `mvnw.cmd -B test` muestran las corridas en ese mismo rango; la columna «Tiempo real» refleja exactamente eso.
- El paso 4 (Agregado) se implementó antes que el paso 3 (Servicio de Dominio): el servicio opera sobre `SolicitudMedicamento`, así que la entidad debía existir primero. Los cinco pasos quedaron completos; solo se ajustó ese orden para que cada commit compile y la suite quede en verde (mismo criterio que usó el taller de RICA).
- Después de **cada paso** se corrió `mvnw.cmd -B test` completo — capturas en `capturas/` (por ejemplo `paso-2-value-object-tests.png`). Estado final: **24 tests, 0 fallos, `BUILD SUCCESS`**.
- Tests por pieza: `CoberturaTest` (4), `SolicitudMedicamentoTest` (6), `VerificacionCoberturaServiceTest` (3), `SolicitudMedicamentoFactoryTest` (5).
- Nota de integración: al clonar `main`, `RedHospitalariaApplicationTests#contextLoads` fallaba por clases duplicadas de `admisiones` que quedaron tras el merge del taller hexagonal (PR #3). Se corrigió en el commit `1a1acef` de esta misma rama (eliminando las versiones planas sin uso) para poder correr la suite — se avisa en la descripción del PR.
