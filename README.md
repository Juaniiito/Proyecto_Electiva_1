# 📱 Método Cosecha - Gestor de Tareas Android

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Platform](https://img.shields.io/badge/Platform-Android-green.svg)](https://www.android.com/)
[![Language](https://img.shields.io/badge/Language-Kotlin%20%7C%20Java-blue.svg)](https://kotlinlang.org/)

Aplicación móvil Android para la gestión de tareas personales, desarrollada como proyecto académico. Ofrece una solución completa para organizar eficientemente tus tareas clasificándolas en secciones inteligentes: **Hoy**, **Próximos días** y **Algún día**, facilitando la priorización y seguimiento de actividades.

---

## 📋 Tabla de Contenidos

- [Características](#características)
- [Requisitos](#requisitos)
- [Instalación](#instalación)
- [Uso](#uso)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Referencias](#referencias)
- [Autor](#autor)
- [Licencia](#licencia)

---

## ✨ Características

✅ **Crear tareas** - Agrega nuevas tareas con descripción y detalles  
✅ **Editar tareas** - Modifica tareas existentes en cualquier momento  
✅ **Eliminar tareas** - Borra tareas completadas o innecesarias  
✅ **Categorización inteligente** - Organiza automáticamente en:
   - 📌 **Hoy**: Tareas para hoy
   - 📅 **Próximos días**: Tareas futuras próximas
   - 📌 **Algún día**: Tareas sin fecha específica

✅ **Interfaz minimalista** - Diseño limpio y fácil de usar  
✅ **Seguimiento de actividades** - Visualiza el estado de tus tareas  
✅ **Persistencia de datos** - Las tareas se guardan localmente

---

## 🔧 Requisitos

- **Android**: API 21 (Android 5.0) o superior
- **Kotlin/Java**: JDK 11 o superior
- **Android Studio**: Versión 2020.3 o posterior
- **Gradle**: Versión 7.0 o superior
- **RAM**: Mínimo 2GB para desarrollo

---

## 📥 Instalación

### Opción 1: Desde Android Studio

```bash
# 1. Clona el repositorio
git clone https://github.com/Juaniiito/Proyecto_Electiva_1.git

# 2. Abre el proyecto en Android Studio
# File → Open → Selecciona la carpeta del proyecto

# 3. Espera a que Gradle sincronice las dependencias

# 4. Conecta un dispositivo Android o inicia un emulador

# 5. Ejecuta la aplicación
# Run → Run 'app' (o presiona Shift + F10)
```

### Opción 2: Desde línea de comandos

```bash
# 1. Clona el repositorio
git clone https://github.com/Juaniiito/Proyecto_Electiva_1.git
cd Proyecto_Electiva_1

# 2. Compila la aplicación
./gradlew build

# 3. Instala en dispositivo/emulador
./gradlew installDebug

# 4. Ejecuta la aplicación
adb shell am start -n com.example.proyecto_electiva_1/.MainActivity
```

---

## 🚀 Uso

### Flujo Principal

1. **Inicia la aplicación** - La pantalla principal muestra todas tus tareas categorizadas
2. **Crea una tarea** - Presiona el botón "+" y completa los detalles
3. **Organiza tus tareas** - Las tareas se categorizan automáticamente según la fecha
4. **Edita o elimina** - Toca una tarea para editarla o desliza para eliminarla
5. **Marca como completada** - Marca tareas completadas (opcional)

### Atajos

| Acción | Gesto |
|--------|-------|
| Crear tarea | Botón flotante (+) |
| Editar tarea | Tocar la tarea |
| Eliminar tarea | Deslizar hacia la izquierda |
| Ver detalles | Tocar la tarea |

---

## 📁 Estructura del Proyecto

```
Proyecto_Electiva_1/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/proyecto_electiva_1/
│   │   │   │   ├── activities/          # Actividades (pantallas)
│   │   │   │   ├── fragments/           # Fragmentos
│   │   │   │   ├── models/              # Modelos de datos
│   │   │   │   ├── database/            # Configuración de BD
│   │   │   │   ├── adapters/            # Adaptadores de listas
│   │   │   │   └── utils/               # Utilidades
│   │   │   ├── res/
│   │   │   │   ├── layout/              # Archivos XML de interfaz
│   │   │   │   ├── drawable/            # Imágenes y vectores
│   │   │   │   ├── values/              # Strings, colores, estilos
│   │   │   │   └── menu/                # Menús
│   │   │   └── AndroidManifest.xml
│   │   └── test/                        # Pruebas unitarias
│   ├── build.gradle                     # Configuración de compilación
│   └── proguard-rules.pro               # Reglas ProGuard
├── docs/
│   ├── ideas.md                         # Ideas iniciales
│   ├── funcionalidades.md               # Funcionalidades detalladas
│   └── ui.md                            # Diseño de interfaz
├── build.gradle                         # Configuración raíz
└── README.md                            # Este archivo
```

---

## 📚 Documentación

- [Ideas Iniciales del Proyecto](docs/ideas.md)
- [Funcionalidades de la Aplicación](docs/funcionalidades.md)
- [Diseño de la Interfaz de Usuario](docs/ui.md)

---

## 🔗 Referencias

Inspirado en aplicaciones profesionales de gestión de tareas:

- [Todoist App](https://www.todoist.com/es) - Gestor de tareas en la nube
- [Microsoft To Do](https://to-do.office.com/tasks/) - Gestor de tareas integrado con Microsoft
- [Tick Tick](https://ticktick.com/) - Aplicación de productividad avanzada

---

## 👨‍💻 Autor

**Juan Manuel Ducuara Soache**
- GitHub: [@Juaniiito](https://github.com/Juaniiito)
- Proyecto desarrollado como trabajo académico para la materia Electiva 1

---

## 📄 Licencia

Este proyecto está bajo la licencia **MIT** - ver el archivo [LICENSE](LICENSE) para más detalles.

La licencia MIT permite uso libre, comercial y privado, con la única condición de incluir una copia de la licencia y del copyright.

---

## 🤝 Contribuciones

Las contribuciones son bienvenidas. Para cambios significativos:

1. Fork el repositorio
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

---

## 📝 Notas de Desarrollo

- El proyecto usa SQLite para persistencia de datos local
- Compatible con Android 5.0 (API 21) y versiones posteriores
- Desarrollado con Kotlin/Java nativo (sin frameworks externos pesados)

---

**⭐ Si te fue útil, considera dejar una estrella en el repositorio.**
