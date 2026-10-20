# Fase hexagonal - Dev 3 (Farmacia y cobertura)

## Hexagono del subdominio

| Elemento | Clase / paquete |
| --- | --- |
| Nucleo - Agregado | `farmacia.dominio.SolicitudMedicamento` |
| Nucleo - Value Object | `farmacia.dominio.Cobertura` |
| Nucleo - Lenguaje Ubicuo | `farmacia.dominio.EstadoSolicitudMedicamento` |
| Puerto primario | `farmacia.aplicacion.SolicitudMedicamentoUseCase` |
| Caso de uso | `farmacia.aplicacion.SolicitudMedicamentoService` |
| Servicio de Dominio | `farmacia.aplicacion.VerificacionCoberturaService` |
| Factory | `farmacia.aplicacion.SolicitudMedicamentoFactory` |
| Puerto secundario - persistencia | `farmacia.aplicacion.RepositorioSolicitudesMedicamento` |
| Puerto secundario - aseguradora | `farmacia.aplicacion.ConsultaCoberturaExterna` |
| Adaptador primario | `farmacia.infraestructura.entrada.rest.SolicitudMedicamentoController` |
| Adaptador secundario - persistencia | `farmacia.infraestructura.salida.persistencia.RepositorioSolicitudesMedicamentoMemoria` |
| Adaptador secundario - aseguradora | `farmacia.infraestructura.salida.aseguradora.CoberturaAseguradoraStubAdapter` |

## Endpoints

| Metodo | Ruta | Caso de uso |
| --- | --- | --- |
| POST | `/api/farmacia/solicitudes` | Recibir solicitud (HU-06) |
| POST | `/api/farmacia/solicitudes/{id}/verificar-cobertura` | Verificar cobertura (HU-07) |
| POST | `/api/farmacia/solicitudes/{id}/despachar` | Despachar medicamento (HU-06) |
| GET | `/api/farmacia/solicitudes/{id}` | Consultar solicitud |
| GET | `/api/farmacia/solicitudes` | Listar solicitudes |

## Regla de dependencia

- `dominio` no importa Spring, JPA, HTTP ni el cliente de la aseguradora.
- `aplicacion` depende solo de interfaces (puertos) y del modelo de dominio.
- `infraestructura` implementa los puertos: el controlador consume el puerto
  primario y los adaptadores implementan los puertos secundarios.
- Los DTOs viven solo en `infraestructura.entrada.rest.dto`; el nucleo nunca
  los recibe.

## Pruebas

- `SolicitudMedicamentoServiceTest`: 6 pruebas con un repositorio fake, sin Spring.
- `SolicitudMedicamentoControllerTest`: 5 pruebas con MockMvc standalone.
- Suite completa: `mvnw.cmd -B test`.
