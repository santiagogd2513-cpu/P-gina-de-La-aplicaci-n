# Luna Mística

App de bienestar espiritual hecha con **Kotlin + Compose Multiplatform**: un solo código para
**Android**, **escritorio** (Windows / macOS / Linux) e **iOS**.
Se adapta automáticamente al **modo claro u oscuro del sistema**.

## Secciones

| Sección | Qué incluye |
|---|---|
| 🔮 **Tarot** | Los 22 Arcanos Mayores con significado al derecho e invertido. Tiradas: carta del día, una carta, pasado·presente·futuro y amor. Las cartas se giran con animación. |
| 💎 **Péndulo** | Pregunta de sí / no con un péndulo animado, y alineación energética de los 7 chakras (lectura, recomendaciones y armonización). |
| 🛁 **Baños** | 7 baños rituales (prosperidad, amor propio, protección, descarga, paz, abrir caminos, buena suerte) con lista de materiales marcable, pasos y modo guiado. |
| 🙏 **Rezos** | 8 oraciones, con vela virtual que se enciende y contador de repeticiones. |
| 🌿 **Limpiezas** | 6 limpiezas energéticas (sahumerio, sal, huevo, vela, cristales, respiración) con modo guiado. |
| 🔢 **Numerología** | Camino de vida, expresión, alma, personalidad, día de nacimiento y año personal (sistema pitagórico, con números maestros). |

La pantalla de inicio muestra además la **fase lunar del día** y qué tipo de ritual le favorece.
Los fondos animados son shaders (AGSL en Android 13+, SkSL en escritorio e iOS).

## Descargar el APK (Android)

Cada push compila el APK automáticamente con GitHub Actions:

1. Ve a la pestaña **Releases** del repositorio (o **Actions → Build APK → Artifacts**).
2. Descarga `LunaMistica-N.apk` en el teléfono.
3. Ábrelo y permite *instalar apps de origen desconocido* cuando Android lo pida.

> Funciona desde Android 7. Los fondos animados completos requieren Android 13 o superior;
> en versiones anteriores se muestra un degradado animado más sencillo.

## Instalar en Windows

Cada push también genera un instalador para Windows 10/11 en **Releases**:

1. Descarga `LunaMistica-Setup-N.exe`.
2. Ejecútalo. Si aparece "Windows protegió su PC", pulsa **Más información → Ejecutar de todas formas**
   (el instalador no está firmado con un certificado de pago).
3. Sigue el asistente; se crea un acceso directo en el escritorio y en el menú Inicio.

No necesita permisos de administrador ni tener Java instalado (va incluido). Los instaladores nuevos
actualizan la versión ya instalada.

## Página web

**https://santiagogd2513-cpu.github.io/P-gina-de-La-aplicaci-n/**

La carpeta `site/` contiene la página de descarga (modo oscuro). Cada vez que cambia algo dentro de
`site/`, `.github/workflows/pages.yml` la copia a la rama `gh-pages`, desde donde GitHub Pages la publica.

Los botones enlazan a `releases/latest/download/LunaMistica.apk` y
`releases/latest/download/LunaMistica-Setup.exe`: cada compilación sube esos archivos con nombre fijo a
la última versión publicada, así que la página siempre descarga lo más nuevo sin tocarla.

## Compilar en tu computadora

Requisitos: JDK 17+ y Android SDK (Android Studio lo instala).

```bash
./gradlew :composeApp:assembleDebug     # APK -> composeApp/build/outputs/apk/debug/
./gradlew :composeApp:installDebug      # instala en un teléfono conectado por USB
./gradlew :composeApp:run               # versión de escritorio
./gradlew :composeApp:packageExe        # instalador .exe (ejecutar en Windows)
./gradlew :composeApp:desktopTest       # pruebas (shaders, numerología, fase lunar)
```

**iOS** (requiere Mac con Xcode): el módulo genera el framework `ComposeApp`; desde Swift se usa
`MainViewControllerKt.MainViewController()`. Lo más fácil es crear el proyecto Xcode con el
[asistente de Kotlin Multiplatform](https://kmp.jetbrains.com/) y apuntarlo a este módulo.

## Estructura

```
composeApp/src/commonMain/kotlin/com/lunamistica/app/
  App.kt, Theme.kt, HomeScreen.kt      navegación, tema claro/oscuro, inicio
  TarotData.kt, TarotScreen.kt          tarot
  PendulumScreen.kt                     péndulo y chakras
  RitualsData.kt, RitualScreens.kt      baños, limpiezas y rezos (contenido y pantallas)
  Numerology.kt, NumerologyScreen.kt    numerología
  MoonPhase.kt                          fase lunar
  ShaderLibrary.kt                      fondos animados
```

Para añadir contenido (un baño, una oración, etc.) basta con editar `RitualsData.kt`.
