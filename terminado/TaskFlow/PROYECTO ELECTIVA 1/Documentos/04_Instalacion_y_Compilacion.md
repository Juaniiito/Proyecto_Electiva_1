# TaskFlow — Instalación y compilación

## 1. Requisitos previos

| Componente | Versión recomendada |
|------------|---------------------|
| **Android Studio** | Última estable (Ladybug o superior compatible con el AGP del proyecto) |
| **JDK** | **17** (alineado con `compileOptions` / `jvmTarget` del módulo `app`) |
| **Android SDK** | Instalado vía SDK Manager (API mínima 24, compilar según `compileSdk` del proyecto) |
| **Conexión a Internet** | Para la primera sincronización de Gradle y dependencias Maven/Google |

---

## 2. Abrir el proyecto

1. Inicie **Android Studio**.  
2. **File → Open…**  
3. Seleccione la carpeta:  
   `C:\Users\jaide\OneDrive\Documentos\PROYECTO ELECTIVA 1`  
4. Espere a que termine **Gradle Sync**. Si aparece un asistente de actualización de AGP, siga las recomendaciones del IDE o las indicaciones del docente.

---

## 3. Variables de entorno (línea de comandos)

Si desea compilar con **Gradle desde terminal** (`gradlew`):

1. Instale un **JDK 17** y configure `JAVA_HOME` apuntando a esa instalación.  
2. En Windows PowerShell, ejemplo de comprobación:

```powershell
java -version
echo $env:JAVA_HOME
```

3. Desde la raíz del proyecto:

```powershell
cd "C:\Users\jaide\OneDrive\Documentos\PROYECTO ELECTIVA 1"
.\gradlew.bat assembleDebug
```

---

## 4. Ejecutar en dispositivo o emulador

1. Cree un **AVD** (emulador) con API ≥ 24 o conecte un teléfono con **depuración USB** activada.  
2. Seleccione el dispositivo en la barra de herramientas.  
3. Pulse **Run** (▶) o **Shift+F10**.

---

## 5. Permisos en tiempo de ejecución

- **Android 13 (API 33)+:** la app puede solicitar `POST_NOTIFICATIONS` al abrir la pantalla principal. Si el usuario deniega, las notificaciones pueden no mostrarse.  
- Otros permisos declarados en el manifiesto están descritos en la documentación técnica.

---

## 6. Solución de problemas frecuentes

| Problema | Posible causa | Acción |
|----------|----------------|--------|
| Sync falla por SDK | `local.properties` sin `sdk.dir` | Abrir proyecto desde Android Studio para regenerar o editar `sdk.dir`. |
| Error de Java | JDK incorrecto | En **File → Settings → Build → Gradle**, JDK para Gradle = 17. |
| Room / kapt | Plugins desactualizados | **Sync** y revisar `gradle/libs.versions.toml`. |

---

## 7. Generar APK de depuración

- Menú **Build → Build Bundle(s) / APK(s) → Build APK(s)**.  
- Salida típica: `app/build/outputs/apk/debug/app-debug.apk`.

---

*Instalación y compilación — TaskFlow — Proyecto Electiva 1.*
