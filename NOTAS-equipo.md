# NOTAS-equipo.md — Grupo 4 · Red hospitalaria multi-sede

> Entregable de equipo (secciones 2 a 5 de la Lección 3). Basado en
> `Requisitos_G4.docx`. Se hizo primero el brainstorming completo por
> historia de usuario, y solo después se agrupó en Bounded Contexts —
> siguiendo el orden que pide la guía (Event Storming → Bounded Contexts →
> subdominios), no al revés.

## 1. Event Storming — recorrido historia por historia

Se recorrieron las 10 HU de `Requisitos_G4.docx` en orden, preguntando para
cada una: *"¿qué pasó, en pasado, cuando esto ocurre?"*. Algunas HU no
generan un evento propio porque son consultas (no cambian el estado del
dominio) — se anotan igual, para dejar constancia de que se discutieron y
no que se pasaron por alto.

| HU | Historia (resumen) | ¿Qué evento(s) de dominio produce? | Discusión |
|---|---|---|---|
| HU-01 | Registrar ingreso de un paciente en una sede | **PacienteAdmitido** | Se consideró separar en `PacienteRegistrado` + `CamaAsignada`, pero para una sola sede/ingreso es un mismo hecho atómico — se deja uno solo. |
| HU-02 | Consultar disponibilidad de camas por sede y servicio | *(ninguno — es consulta)* | Se propuso `DisponibilidadConsultada`, pero no cambia nada en el dominio; es una lectura que se apoya en el estado que dejan otros eventos (HU-01, HU-03). |
| HU-03 | Trasladar un paciente entre sedes o servicios | **PacienteTrasladado** | También se discutió un evento de fallo (`TrasladoRechazado`, cuando no hay cama en destino) — se deja fuera de la lista principal porque es un camino de error, no un hito del flujo feliz, pero se documenta como regla de negocio (sección 3 del `Requisitos_G4.docx`). |
| HU-04 | Solicitar un examen de laboratorio | **ExamenSolicitado** | Sin discusión, es directo. |
| HU-05 | Registrar el resultado de un examen (propio o de equipo externo) | **ResultadoExamenRegistrado** | Se discutió si "resultado externo" merecía su propio evento (`ResultadoRecibidoDeEquipoExterno`); se decidió que no — desde el punto de vista del dominio clínico, el origen del dato (interno/externo) es un detalle de infraestructura, el evento de negocio es el mismo: el resultado quedó registrado y ya se puede consultar. |
| HU-06 | Recibir y despachar solicitudes de medicamentos | **MedicamentoSolicitado** y **MedicamentoDespachado** | Es una sola HU pero describe dos momentos distintos ("recibir" ≠ "despachar"), con un estado intermedio (solicitud pendiente) entre ambos — se separan en dos eventos. |
| HU-07 | Verificar la cobertura del paciente ante una aseguradora externa | **CoberturaVerificada** | El actor que dispara la consulta es *personal de admisiones*, pero el resultado (qué % cubre la aseguradora) solo importa para decidir si se despacha un medicamento — se vuelve relevante en la sección 3. |
| HU-08 | Consultar el estado consolidado del paciente entre sedes | *(ninguno — es consulta)* | Lectura agregada sobre varios subdominios; no genera estado nuevo. |
| HU-09 | Dar de alta médica y cerrar la admisión | **PacienteDadoDeAlta** | Igual que HU-03, existe un camino de rechazo (`AltaRechazadaPorOrdenesPendientes`) que se deja documentado como regla de negocio, no como evento del flujo principal. |
| HU-10 | Reporte de ocupación de camas por sede | *(ninguno — es reporte)* | Agregación de lecturas sobre el estado que ya dejan HU-01/HU-03/HU-09. |

### Lista final de eventos, ordenados cronológicamente

Con los 8 eventos que sí cambian estado, se armó la línea de tiempo de un
caso típico (un paciente entra, se le atiende, y sale):

| # | Evento | HU |
|---|---|---|
| 1 | PacienteAdmitido | HU-01 |
| 2 | ExamenSolicitado | HU-04 |
| 3 | **ResultadoExamenRegistrado** ⭐ | HU-05 |
| 4 | MedicamentoSolicitado | HU-06 |
| 5 | CoberturaVerificada | HU-07 |
| 6 | MedicamentoDespachado | HU-06 |
| 7 | **PacienteTrasladado** ⭐ | HU-03 |
| 8 | **PacienteDadoDeAlta** ⭐ | HU-09 |

**Nota sobre el orden:** `PacienteTrasladado` no siempre ocurre en el
puesto 7 — un paciente se puede trasladar apenas es admitido, o nunca. Se
ubica ahí porque es el orden más común en el caso típico que se está
narrando (se complica la condición tras ver el resultado del examen y hay
que moverlo a una sede con el servicio adecuado), no porque sea una regla
fija. Lo mismo aplica, en menor medida, a `CoberturaVerificada`: podría
pasar apenas se admite al paciente; se dejó junto a la solicitud de
medicamento porque es el punto donde de verdad *importa* para el flujo (si
no está cubierto, cambia lo que pasa después).

## 2. Eventos pivote ⭐ — por qué son esos tres y no otros

Se revisaron los 8 eventos contra la definición de la guía: *"no los
resuelve un solo objeto porque necesitan información de varios, o cambia
quién es responsable de lo que sigue"*. De los 8, solo 3 pasaron esa
prueba con claridad:

- **PacienteTrasladado** — necesita la `Admision` (quién es el paciente,
  su estado actual) **y** la disponibilidad de camas de la sede *destino*
  (un dato que no vive en `Admision`). Cambia la responsabilidad clínica
  de una sede a otra.
- **ResultadoExamenRegistrado** — necesita la orden de laboratorio
  original, el resultado (propio o de un equipo externo), y cambia quién
  es responsable de actuar: de "laboratorio está trabajando en esto" a
  "el médico ya puede decidir con esta información".
- **PacienteDadoDeAlta** — no lo puede resolver solo Admisiones: la regla
  de negocio ("el alta se rechaza si hay órdenes de laboratorio
  pendientes") obliga a cruzar hacia el estado de Laboratorio antes de
  poder cerrar la admisión.

Se descartaron como pivote, con su razón:

| Evento candidato descartado | Por qué no es pivote |
|---|---|
| PacienteAdmitido | Lo resuelve un solo objeto (`Admision` nueva) con un solo dato externo (disponibilidad de cama en el momento), sin que cambie de responsable a mitad de camino. |
| MedicamentoSolicitado / MedicamentoDespachado | Cada uno lo resuelve su propio flujo (`SolicitudMedicamento`); `CoberturaVerificada` alimenta la decisión pero no obliga a que el evento en sí cruce de contexto. |
| CoberturaVerificada | Es una consulta a un sistema externo que **informa** una decisión posterior, pero no es en sí mismo el punto donde cambia el dueño de la responsabilidad — quien decide seguir sigue siendo Farmacia. |

## 3. De eventos a Bounded Contexts candidatos

Alrededor de cada evento pivote se probaron agrupaciones y se filtraron
con la tabla de buena señal / mala señal de la guía.

**Primer intento (descartado):** agrupar por *actor* en vez de por evento
— un contexto "Admisiones" (todo lo que toca el personal de admisiones,
incluida `CoberturaVerificada` porque HU-07 dice "como personal de
admisiones..."). Se descartó al aplicar la mala señal *"el corte sigue
capas o roles, no eventos de negocio"*: `CoberturaVerificada` solo importa
para decidir un despacho de medicamento (HU-06); meterla en Admisiones
haría que Farmacia dependiera de Admisiones para saber si puede despachar,
invirtiendo la dependencia natural (Farmacia es quien necesita el dato
antes de actuar).

**Agrupación final**, construida alrededor de los 3 eventos pivote:

| Bounded Context candidato | Eventos que agrupa | Se origina en | Por qué es un buen corte |
|---|---|---|---|
| **Admisiones y camas** | PacienteAdmitido, PacienteTrasladado, PacienteDadoDeAlta | PacienteTrasladado | Buena señal: "cama", "sede", "traslado" significan una sola cosa aquí — dónde está el paciente ahora y cómo llegó ahí. Se apoya en 2 de los 3 eventos pivote. |
| **Laboratorio** | ExamenSolicitado, ResultadoExamenRegistrado | ResultadoExamenRegistrado | Buena señal: el ciclo completo (solicitud → resultado, propio o externo) vive aislado; nadie fuera de Laboratorio necesita saber *cómo* se obtuvo el resultado, solo *que* existe (lo que consulta Admisiones para el alta). |
| **Farmacia y cobertura** | MedicamentoSolicitado, MedicamentoDespachado, CoberturaVerificada | (ninguno de los 3 pivote cae aquí directamente, pero agrupa la regla "no se despacha sin solicitud ni sin cobertura verificada") | Buena señal: "cobertura" y "despacho" solo tienen sentido juntos — no se puede evaluar una solicitud de medicamento sin saber qué cubre la aseguradora. Mala señal evitada: no se dividió por actor (admisiones vs. farmacéutico). |

Prueba de "mala señal" aplicada a los tres: ¿el mismo término significa
cosas distintas en cada uno? Sí — "sede" en Admisiones es dónde está el
paciente; en Laboratorio y Farmacia ni siquiera aparece como concepto de
primer orden (solo importa la sede para enrutar la solicitud, no como
Lenguaje Ubicuo propio). Eso confirma que el corte es correcto.

## 4. Asignación de subdominios

Con los 3 Bounded Contexts candidatos y 3 integrantes, la asignación fue
directa — se revisó que los tres quedaran de tamaño comparable (2-3
eventos cada uno, ninguno concentra más de la mitad de la lista):

| Integrante | Subdominio candidato | Raíz del Agregado | Value Object candidato | Servicio de Dominio candidato |
|---|---|---|---|---|
| **Dev 1** | Admisiones y camas | `Admision` | `DisponibilidadCama` | `TrasladoPacienteService` — no traslada sin cama confirmada en el destino |
| **Dev 2** | Laboratorio | `OrdenLaboratorio` | `ResultadoExamen` — exige valor y unidad | `CierreOrdenLaboratorioService` — una orden solo se cierra con resultado registrado |
| **Dev 3** | Farmacia y cobertura | `SolicitudMedicamento` | `Cobertura` — porcentaje válido entre 0 y 100 | `VerificacionCoberturaService` — consulta la cobertura externa antes de despachar |

Cada integrante es dueño principal de su subdominio: decide su Lenguaje
Ubicuo y su Agregado cuando hay dudas, pero el resto del equipo puede
opinar y revisar por Pull Request (sección 7 de la guía).

## 5. Límite del Agregado — mini-ADR (Dev 1 · Admision)

Mismo formato de 4 preguntas que usó el taller individual sobre `Investigador`:

| Pregunta | Respuesta |
|---|---|
| ¿Cuál es la raíz del Agregado? | `Admision` — será la única clase con `@Entity` en el paquete `admisiones` cuando el proyecto llegue a persistencia (Tutorial 4). Por ahora, sin JPA, es una clase Java simple con constructor de paquete. |
| ¿Qué vive dentro del límite? | `id`, `pacienteId`, `sedeActualId`, `estado`, `fechaIngreso`, `fechaAlta` y `historialTraslados` (lista de `Traslado`, un Value Object). |
| ¿Por qué `OrdenLaboratorio` (subdominio de Dev 2) NO está dentro de este límite? | Porque pertenece a otro Bounded Context con otro dueño. Admision solo necesita **saber si hay órdenes pendientes** para decidir el alta (HU-09) — no necesita gestionarlas. Por eso `darDeAlta(...)` recibe un `boolean` ya resuelto, nunca la entidad `OrdenLaboratorio`. |
| ¿Qué pasaría si alguien agrega un campo `List<OrdenLaboratorio> ordenes` directo en `Admision`? | Rompería el límite: cada cambio en una orden de laboratorio (que Dev 2 controla) dispararía una transacción sobre `Admision`; los dos subdominios dejarían de poder evolucionar y desplegarse por separado — exactamente el riesgo que señala la guía sobre Agregados grandes. |

## 6. Repositorio de GitHub

- Enlace: `https://github.com/juanlealr/red-hospitalaria`
- Primer commit: esqueleto Spring Boot (`Project: Maven`, `Language: Java`,
  `Spring Boot: 4.1.1`, `Packaging: Jar`, `Java: 25`, dependencia `Spring Web`).
- Paquetes vacíos creados desde el primer commit: uno por Bounded Context
  (`admisiones`, `laboratorio`, `farmacia`), más `compartido` para lo que no
  pertenece a ninguno — mismo patrón que `rica-api`.