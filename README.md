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

## Compilar en tu computadora

Requisitos: JDK 17+ y Android SDK (Android Studio lo instala).

```bash
./gradlew :composeApp:assembleDebug     # APK -> composeApp/build/outputs/apk/debug/
./gradlew :composeApp:installDebug      # instala en un teléfono conectado por USB
./gradlew :composeApp:run               # versión de escritorio
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
