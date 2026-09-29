# Paso 1 · Lenguaje Ubicuo — Dev 3 (Farmacia y cobertura)

Auditoría de nombres contra `Requisitos_G4.docx` y el Event Storming del equipo (`NOTAS-equipo.md`), hecha **antes** de escribir las clases del subdominio, para que los nombres del código salgan del negocio y no al revés.

## Términos revisados y decisión

| Término del negocio (fuente) | Decisión en el código | Por qué |
| --- | --- | --- |
| "solicitud de medicamentos" (HU-06) | `SolicitudMedicamento` (raíz del Agregado) | Es la unidad de trabajo del farmacéutico; el docx usa "solicitud", no "receta". |
| "recibir … solicitudes" (HU-06) | `SolicitudMedicamentoFactory.recibirSolicitud(...)` | Verbo del negocio convertido en operación de la Factory; el estado inicial es `RECIBIDA`. |
| "despachar" (HU-06) | `SolicitudMedicamento.despachar(...)` → estado `DESPACHADA` | El despacho es comportamiento del Agregado, no un setter ni una tabla aparte. |
| "cobertura" (HU-07; entidad clave del docx) | `Cobertura` (Value Object, porcentaje 0..100) | Al negocio solo le importa el porcentaje, no "cuál" cobertura: no tiene identidad propia. |
| "verificar la cobertura … ante una aseguradora externa" (HU-07) | `VerificacionCoberturaService` + puerto `ConsultaCoberturaExterna` | La operación cruza la solicitud con un sistema externo: es un Servicio de Dominio, no un método de una entidad. |
| eventos `MedicamentoSolicitado` / `CoberturaVerificada` / `MedicamentoDespachado` (NOTAS-equipo.md §1) | estados `RECIBIDA` / `COBERTURA_VERIFICADA` / `DESPACHADA` | El ciclo de estados coincide uno a uno con los eventos que el equipo ya había identificado. |
| "paciente", "médico" (actores del docx) | `pacienteId`, `medicoId` (solo por id) | Referencias a otros subdominios por identidad (regla 3 de Vernon); el objeto completo vive en Admisiones. |

## Decisiones descartadas

- `Receta` / `Prescripcion`: el docx no usa esos términos; habría sido vocabulario inventado.
- `Despacho` como clase con identidad: hoy el despacho es un cambio de estado + fecha dentro de la solicitud (evento `MedicamentoDespachado`); no tiene identidad propia que rastrear.
- `CoberturaAseguradora` como Entidad: si solo importan los valores (porcentaje), es Value Object — convertirlo en Entidad agregaría una identidad que nadie necesita.
