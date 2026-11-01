# Shader

Galería de efectos visuales en tiempo real (shaders), hecha con **Kotlin + Compose Multiplatform**.
Un solo código para **Android**, **escritorio** (Windows / macOS / Linux) e **iOS**.

Efectos incluidos: Plasma, Túnel, Aurora, Metabolas, Fractal (Julia) y Fuego.
En la vista de detalle puedes tocar o arrastrar para interactuar, cambiar la velocidad y pausar.

## Descargar el APK (Android)

Cada push compila el APK automáticamente con GitHub Actions:

1. Ve a la pestaña **Releases** del repositorio (o **Actions → Build APK → Artifacts**).
2. Descarga `Shader-N.apk` en tu teléfono.
3. Ábrelo y permite *instalar apps de origen desconocido* cuando Android lo pida.

> Los shaders en tiempo real (AGSL) requieren **Android 13 o superior**. En Android 7–12 la app
> funciona igual, pero muestra una versión simplificada de cada efecto.

## Compilar en tu computadora

Requisitos: JDK 17+ y Android SDK (Android Studio lo instala).

```bash
./gradlew :composeApp:assembleDebug     # APK -> composeApp/build/outputs/apk/debug/
./gradlew :composeApp:installDebug      # instala en un teléfono conectado por USB
./gradlew :composeApp:run               # versión de escritorio
./gradlew :composeApp:desktopTest       # compila todos los shaders y genera vistas previas
```

**iOS** (requiere Mac con Xcode): el módulo genera el framework `ComposeApp`; desde Swift se usa
`MainViewControllerKt.MainViewController()`. Lo más fácil es crear el proyecto Xcode con el
[asistente de Kotlin Multiplatform](https://kmp.jetbrains.com/) y apuntarlo a este módulo.

## Estructura

```
composeApp/src/
  commonMain/   UI compartida (App.kt) y el código de los shaders (ShaderLibrary.kt)
  androidMain/  MainActivity y render con AGSL (android.graphics.RuntimeShader)
  desktopMain/  ventana de escritorio y render con Skia (SkSL)
  iosMain/      punto de entrada iOS y render con Skia (SkSL)
```

Los shaders están escritos una sola vez: AGSL y SkSL comparten sintaxis.
Para añadir un efecto, agrega un `ShaderEffect` en `ShaderLibrary.kt` y súmalo a `ShaderLibrary.all`.
