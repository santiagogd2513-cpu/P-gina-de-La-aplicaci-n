import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.compose")
    id("com.android.application")
}

kotlin {
    androidTarget {
        compilerOptions { jvmTarget.set(JvmTarget.JVM_17) }
    }

    jvm("desktop")

    listOf(iosX64(), iosArm64(), iosSimulatorArm64()).forEach { target ->
        target.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.1")
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
        androidMain.dependencies {
            implementation("androidx.activity:activity-compose:1.9.3")
        }
        val desktopMain by getting
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
        }
        val desktopTest by getting
        desktopTest.dependencies {
            implementation(kotlin("test"))
            implementation(compose.desktop.currentOs)
        }
    }
}

android {
    namespace = "com.lunamistica.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.lunamistica.app"
        minSdk = 24
        targetSdk = 35
        versionCode = 4
        versionName = "4.0"
    }

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")

    // Firma fija guardada en el repositorio: así cada APK nuevo puede instalarse
    // encima del anterior (si cada compilación usara una clave distinta, Android
    // rechaza la actualización con "No se instaló la app").
    // Para publicar en Google Play usa una clave privada que NO esté en el repositorio.
    signingConfigs {
        getByName("debug") {
            storeFile = file("signing/luna-debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        // Permite usar java.time (kotlinx-datetime) en Android 7.
        isCoreLibraryDesugaringEnabled = true
    }
    packaging {
        resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
}

dependencies {
    "coreLibraryDesugaring"("com.android.tools:desugar_jdk_libs:2.1.3")
}

// Versión de la app de escritorio (MAYOR.MENOR.COMPILACIÓN). En GitHub Actions se pasa
// -PappVersion=5.0.<número de compilación> para que cada instalador actualice al anterior.
val desktopVersion = (project.findProperty("appVersion") as String?) ?: "5.0.0"

compose.desktop {
    application {
        mainClass = "com.lunamistica.app.MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Exe, TargetFormat.Msi, TargetFormat.Dmg, TargetFormat.Deb)
            packageName = "Luna Mistica"
            packageVersion = desktopVersion
            description = "Tarot, péndulo, baños, rezos, limpiezas y numerología"
            vendor = "Luna Mística"
            // Módulos de Java incluidos en el instalador (java.prefs guarda la cuenta del registro).
            modules("java.instrument", "java.management", "java.naming", "java.prefs", "jdk.unsupported")

            windows {
                iconFile.set(project.file("icons/icon.ico"))
                menu = true
                menuGroup = "Luna Mistica"
                shortcut = true
                dirChooser = true
                perUserInstall = true
                // No cambiar: permite que un instalador nuevo actualice la versión instalada.
                upgradeUuid = "7d74d957-ea6c-4082-99ef-2b19976b40ab"
            }
            linux {
                iconFile.set(project.file("icons/icon.png"))
            }
        }
    }
}
