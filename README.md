# 📱 Metodo Cosecha 

Aplicación móvil para Android desarrollada en Kotlin que ofrece una solución completa para la gestión eficiente de tareas personales y académicas. Su diseño minimalista y funcional permite a los usuarios organizar sus actividades diarias con un enfoque estructurado y profesional.

**Autor:** Juan Manuel Ducuara Soache

---

## 🚀 Estado del Proyecto

**Versión:** Adelanto v0.1.0 (Demo Funcional)

El proyecto se encuentra en fase de desarrollo temprano. Actualmente se ha implementado una versión demo que demuestra la interfaz de usuario y la navegación básica.

---

## ✅ Funcionalidades Implementadas (Adelanto)

| Funcionalidad | Estado | Descripción |
|---------------|--------|-------------|
| 📋 Visualización de tareas | ✅ Completado | Pantalla principal con tareas organizadas por categorías (Hoy, Mañana, Durante la semana) |
| ✅ Checkbox interactivos | ✅ Completado | Permite marcar/desmarcar tareas completadas con retroalimentación visual |
| 🧭 Navegación entre pantallas | ✅ Completado | Botón "Siguiente" que navega a una pantalla de adelanto |
| ↩️ Botón de retorno | ✅ Completado | Pantalla de adelanto con botón para volver a la pantalla principal |
| 🎨 Interfaz de usuario | ✅ Completado | Diseño con CardView, colores por categoría y scroll vertical |
| 📱 Adaptabilidad | ✅ Completado | Compatible con diferentes tamaños de pantalla |

---

## 🔜 Funcionalidades Planificadas (Próximas Versiones)

### Versión 1.1
| Funcionalidad | Prioridad | Descripción |
|---------------|-----------|-------------|
| ➕ Agregar tareas | Alta | Formulario para crear nuevas tareas con título, fecha y hora |
| ✏️ Editar tareas | Alta | Modificar tareas existentes |
| 🗑️ Eliminar tareas | Alta | Swipe o botón para eliminar tareas |
| 💾 Persistencia de datos | Alta | Guardar tareas localmente con Room Database |

### Versión 1.2
| Funcionalidad | Prioridad | Descripción |
|---------------|-----------|-------------|
| 🔔 Recordatorios | Media | Notificaciones push para tareas próximas |
| 📅 Calendario integrado | Media | Vista de calendario para visualizar tareas por fecha |
| 🏷️ Categorías personalizadas | Media | Crear y gestionar categorías propias |

### Versión 2.0 (Final)
| Funcionalidad | Prioridad | Descripción |
|---------------|-----------|-------------|
| 🌙 Modo oscuro | Baja | Tema oscuro para la aplicación |
| ☁️ Sincronización en la nube | Baja | Backup y sincronización entre dispositivos |
| 📊 Estadísticas | Baja | Gráficos de productividad y cumplimiento |
| 🧩 Widgets | Baja | Widgets en pantalla principal del teléfono |

---

## 🛠️ Tecnologías Utilizadas

| Tecnología | Versión | Uso |
|------------|---------|-----|
| Kotlin | 1.9.x | Lenguaje principal de la aplicación |
| Android SDK | API 24+ | Mínimo soportado (Android 7.0 Nougat) |
| Material Design | 1.12.0 | Componentes de interfaz de usuario |
| CardView | 1.0.0 | Tarjetas para mostrar tareas |
| RecyclerView | 1.3.2 | (Planificado) Lista dinámica de tareas |
| Room Database | - | (Planificado) Almacenamiento local |

---

## 📁 Estructura del Proyecto
App/src/main/
├── java/com/clase/electiva_1_proyecto/
│ ├── MainActivity.kt           # Pantalla principal de tareas
│ └── AvanceActivity.kt         # Pantalla de adelanto
├── res/
│ ├── layout/
│ │ ├── activity_main.xml       # Layout principal
│ │ └── activity_avance.xml     # Layout de adelanto
│ └── values/
│ ├── strings.xml               # Textos de la aplicación
│ └── themes.xml                # Estilos y temas
└── AndroidManifest.xml         # Configuración de la app
