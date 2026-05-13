# TaskFlow — Estructura del proyecto

Raíz del repositorio: carpeta **PROYECTO ELECTIVA 1** (hermano de la carpeta `Documentos`).

```
PROYECTO ELECTIVA 1/
├── Documentos/                    ← Esta documentación (PDF/Markdown)
├── app/
│   ├── build.gradle.kts
│   ├── proguard-rules.pro
│   └── src/
│       ├── main/
│       │   ├── AndroidManifest.xml
│       │   ├── java/com/taskflow/app/
│       │   │   ├── MainActivity.kt
│       │   │   ├── TaskFlowApplication.kt
│       │   │   ├── data/
│       │   │   │   ├── TaskRepository.kt
│       │   │   │   ├── local/
│       │   │   │   │   ├── TaskEntity.kt
│       │   │   │   │   ├── TaskDao.kt
│       │   │   │   │   └── TaskDatabase.kt
│       │   │   │   └── model/
│       │   │   │       ├── TaskPriority.kt
│       │   │   │       ├── TaskSection.kt
│       │   │   │       └── TaskSectionMapper.kt
│       │   │   ├── notification/
│       │   │   │   ├── BootReceiver.kt
│       │   │   │   ├── NotificationHelper.kt
│       │   │   │   ├── ReminderReceiver.kt
│       │   │   │   └── TaskAlarmScheduler.kt
│       │   │   └── ui/
│       │   │       ├── main/
│       │   │       │   ├── MainFragment.kt
│       │   │       │   ├── MainViewModel.kt
│       │   │       │   ├── MainViewModelFactory.kt
│       │   │       │   ├── MainListAdapter.kt
│       │   │       │   ├── MainListItem.kt
│       │   │       │   └── MainListDiffCallback.kt
│       │   │       └── editor/
│       │   │           ├── TaskEditorFragment.kt
│       │   │           ├── TaskEditorViewModel.kt
│       │   │           └── TaskEditorViewModelFactory.kt
│       │   └── res/
│       │       ├── layout/
│       │       ├── navigation/
│       │       ├── menu/
│       │       ├── values/
│       │       ├── values-night/
│       │       ├── drawable/
│       │       ├── mipmap-*/
│       │       └── xml/
│       ├── test/                  # Pruebas unitarias (JVM)
│       └── androidTest/           # Pruebas instrumentadas
├── gradle/
│   └── libs.versions.toml
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── gradlew / gradlew.bat
└── local.properties               # SDK local (no versionar en equipo ajeno)
```

## Responsabilidad por archivo clave

| Ruta | Responsabilidad |
|------|------------------|
| `MainActivity.kt` | Contenedor: establece `activity_main` con `NavHostFragment`. |
| `TaskFlowApplication.kt` | Inicializa Room, repositorio global y canal de notificaciones. |
| `TaskEntity.kt` | Modelo de persistencia Room. |
| `TaskDao.kt` | Consultas y mutaciones SQL vía Room. |
| `TaskRepository.kt` | API de datos para ViewModels. |
| `TaskSectionMapper.kt` | Reglas de negocio: asignación a sección. |
| `MainViewModel.kt` | Estado de lista, búsqueda, filtros, acciones completar/eliminar. |
| `MainListAdapter.kt` | Presentación de filas y cabeceras en `RecyclerView`. |
| `TaskEditorViewModel.kt` | Estado del formulario, guardado, validación básica. |
| `TaskAlarmScheduler.kt` / `ReminderReceiver.kt` | Recordatorios. |

---

*Estructura del proyecto — TaskFlow — Proyecto Electiva 1.*
