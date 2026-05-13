# TaskFlow — Modelo de datos y reglas de negocio

## 1. Modelo lógico de una tarea

Una tarea en TaskFlow se representa en base de datos como `TaskEntity` con los siguientes conceptos:

| Campo | Tipo lógico | Descripción |
|-------|-------------|-------------|
| `id` | Long | Identificador único (autogenerado). |
| `title` | Texto | Título obligatorio en la UI al guardar. |
| `description` | Texto | Detalle libre. |
| `dueDateMillis` | Long o null | Fecha/hora objetivo en milisegundos desde época; null = sin fecha. |
| `priority` | Enum persistido como String | `HIGH`, `MEDIUM`, `LOW`. |
| `category` | Texto | Etiqueta de ámbito (ej. Estudio, Trabajo). |
| `completed` | Boolean | Si está tachada / cumplida. |
| `createdAtMillis` | Long | Marca temporal de creación (ordenación en “Algún día”). |

---

## 2. Prioridades y color en la UI

En la lista, cada tarea muestra una **pastilla (chip)** de prioridad con colores definidos en recursos (`values/colors.xml`):

- **Alta:** tonos tipo error / rojizos (llama la atención).  
- **Media:** tonos violeta / neutros cálidos.  
- **Baja:** tonos verdes suaves.

La semántica de productividad es: **pocas tareas en “Alta”** para no saturar la sección **Hoy**.

---

## 3. Reglas de asignación a secciones (resumen formal)

Sea `Z` la zona horaria del dispositivo y `today` la fecha local actual.

Para una tarea con `completed == false`:

1. Si `priority == HIGH` → sección **Hoy** (`TODAY`).  
2. En caso contrario, si `dueDateMillis != null`:  
   - Sea `D` la fecha local correspondiente a `dueDateMillis`.  
   - Si `D` ≤ `today` → **Hoy**.  
   - Si `D` > `today` → **Próximos días** (`UPCOMING`).  
3. Si `dueDateMillis == null` → **Algún día** (`SOMEDAY`).

Para `completed == true`, la UI las agrupa bajo **Completadas** al final de la lista (no compiten con las secciones anteriores).

---

## 4. Interacción con recordatorios

- Al **guardar** una tarea con fecha y pendiente de completar, la lógica de la app puede **programar** una alarma asociada al identificador de la tarea.  
- Al **completar** o **eliminar**, las alarmas correspondientes deben **cancelarse** para evitar notificaciones obsoletas.

(Detalle de hora exacta del disparador: ver implementación en `TaskAlarmScheduler` y comentarios en código fuente.)

---

## 5. Índices y rendimiento (Room)

La entidad declara índices sobre campos usados en filtrado u ordenación (por ejemplo `completed`, `dueDateMillis`), lo que ayuda al crecimiento de la lista de tareas.

---

## 6. Categorías sugeridas

En `res/values/arrays.xml` existen categorías por defecto (Personal, Estudio, Trabajo, etc.). El usuario puede escribir categorías personalizadas en el campo correspondiente.

---

*Modelo de datos y reglas — TaskFlow — Proyecto Electiva 1.*
