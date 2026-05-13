# TaskFlow — Documentación técnica

## 1. Visión general de la arquitectura

La aplicación sigue el patrón **MVVM (Model–View–ViewModel)** recomendado por Android:

| Capa | Rol en TaskFlow |
|------|------------------|
| **Vista (View)** | `Activity`, `Fragment`, layouts XML, `RecyclerView` y adaptadores. Solo presenta datos y envía eventos del usuario. |
| **ViewModel** | `MainViewModel`, `TaskEditorViewModel`. Exponen estado observable (`StateFlow`) y llaman al repositorio; sobreviven a cambios de configuración. |
| **Modelo / datos** | `TaskEntity`, `TaskDao`, `TaskDatabase`, `TaskRepository`. Fuente única de verdad en disco vía Room. |

No se usa lógica de negocio compleja en los Fragments: filtrado, agrupación por secciones y operaciones CRUD se coordinan desde el **ViewModel** y el **repositorio**.

---

## 2. Persistencia: Room

### 2.1 Entidad

`TaskEntity` representa una fila en la tabla `tasks`:

- Identificador autogenerado (`id`).
- `title`, `description`, `category`.
- `dueDateMillis` (nullable): instante en milisegundos (medianoche local ajustada según flujo de fecha en el editor).
- `priority`: cadena almacenada (`HIGH`, `MEDIUM`, `LOW`) mapeada al enum `TaskPriority`.
- `completed`: booleano.
- `createdAtMillis`: ordenación auxiliar.

### 2.2 DAO

`TaskDao` expone:

- `observeAll(): Flow<List<TaskEntity>>` — flujo reactivo para la UI.
- Operaciones suspendidas: `getById`, `insert`, `update`, `delete`.

### 2.3 Base de datos

`TaskDatabase` es una clase `RoomDatabase` versión 1. En entorno de desarrollo puede usarse migración destructiva según configuración del proyecto.

### 2.4 Repositorio

`TaskRepository` encapsula el DAO y ofrece una API estable a los ViewModels (`observeTasks`, `getTask`, `upsert`, `delete`).

---

## 3. Lógica de secciones inteligentes

La clase `TaskSectionMapper` asigna cada tarea **no completada** a una de:

- `TODAY`
- `UPCOMING`
- `SOMEDAY`

Las **completadas** se listan aparte en la capa de presentación (`MainViewModel.buildSectionedList`).

Reglas implementadas (resumen):

1. Si `completed` → sección lógica `COMPLETED` en la lista de UI.
2. Si prioridad **HIGH** → **Hoy** (prioridad inmediata).
3. Si hay fecha de vencimiento: comparación por **día civil** con la zona horaria del dispositivo: hoy o pasado → **Hoy**; futuro → **Próximos días**.
4. Si no hay fecha → **Algún día** (incluye media/baja sin fecha; alta sin fecha sigue en Hoy por el punto 2).

---

## 4. Interfaz: RecyclerView y lista seccionada

- `MainListItem` es un tipo sellado: **cabecera de sección** o **fila de tarea**.
- `MainListAdapter` (`ListAdapter` + `DiffUtil.ItemCallback`) dibuja dos tipos de vista: encabezado y tarjeta de tarea.
- `MainViewModel` combina el flujo de Room con **búsqueda** (`StateFlow<String>`) y **filtro de prioridad** (`StateFlow<TaskPriority?>`) y genera la lista plana con cabeceras.

Animaciones: `DefaultItemAnimator` con duraciones cortas en el `RecyclerView` principal.

---

## 5. Navegación

**Android Jetpack Navigation** con grafo `res/navigation/nav_graph.xml`:

- `MainFragment` — destino inicial.
- `TaskEditorFragment` — argumento `taskId` (`long`); valor `0` indica creación; valor positivo indica edición.

Navegación desde la lista: `bundleOf("taskId" to id)`.

---

## 6. Formulario de edición y estado guardado

`TaskEditorViewModel` usa `SavedStateHandle` (factory `AbstractSavedStateViewModelFactory`) para recuperar `taskId` tras procesos de muerte del proceso.

- Carga inicial de texto en el fragment una sola vez (`hydrated`) al recibir la entidad desde Room.
- Fecha y prioridad se sincronizan vía `StateFlow` de UI parcial.
- **Guardar** lee título, descripción y categoría directamente de los campos y valida título no vacío.

**Selector de fecha:** `MaterialDatePicker`; conversión entre selección UTC del picker y “mediodía local” para reducir ambigüedades de zona horaria.

---

## 7. Notificaciones y alarmas

### 7.1 Canal

`NotificationHelper` crea un **NotificationChannel** en Android O+.

### 7.2 Programación

`TaskAlarmScheduler` usa `AlarmManager` con `setExactAndAllowWhileIdle` para disparar un `PendingIntent` hacia `ReminderReceiver`.

### 7.3 Recepción

`ReminderReceiver` (corrutinas + `goAsync`) consulta la base vía `TaskFlowApplication.repository` y muestra notificación si la tarea sigue pendiente.

### 7.4 Arranque del dispositivo

`BootReceiver` reprograma alarmas para tareas pendientes con fecha, tras `BOOT_COMPLETED`.

### 7.5 Permisos en manifiesto

Incluyen, entre otros: `POST_NOTIFICATIONS`, `SCHEDULE_EXACT_ALARM`, `RECEIVE_BOOT_COMPLETED` (según versión del manifiesto del proyecto).

---

## 8. Inyección ligera de dependencias

`TaskFlowApplication` construye `TaskDatabase` y `TaskRepository` al iniciar la aplicación. Los `ViewModelFactory` reciben el `Application` y el repositorio.

---

## 9. Compatibilidad de APIs

- Uso de **Java 8+ APIs de tiempo** (`java.time`) en código de aplicación con **core library desugaring** habilitado en Gradle para soportar API 24+.

---

## 10. Pruebas

- Pruebas unitarias en JVM (ejemplo: `TaskSectionMapperTest`) para validar reglas de sección.
- Prueba instrumentada de ejemplo verifica el `packageName` de la aplicación.

---

*Documentación técnica — TaskFlow — Proyecto Electiva 1.*
