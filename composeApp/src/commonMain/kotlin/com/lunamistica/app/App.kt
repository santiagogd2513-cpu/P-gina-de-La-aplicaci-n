package com.lunamistica.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

/** Pantallas de la app. */
sealed interface Screen {
    data object Home : Screen
    data object Tarot : Screen
    data object Pendulum : Screen
    data object Baths : Screen
    data object Prayers : Screen
    data object Cleansings : Screen
    data object Numerology : Screen
    data class RitualDetail(val ritual: Ritual, val effect: ShaderEffect) : Screen
    data class Guided(val ritual: Ritual, val effect: ShaderEffect) : Screen
    data class PrayerDetail(val prayer: Prayer) : Screen
}

@Composable
fun App() {
    LunaTheme {
        Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            val stack = remember { mutableStateListOf<Screen>(Screen.Home) }
            val navigate: (Screen) -> Unit = { stack.add(it) }
            val back: () -> Unit = { if (stack.size > 1) stack.removeAt(stack.lastIndex) }

            PlatformBackHandler(enabled = stack.size > 1, onBack = back)

            when (val screen = stack.last()) {
                Screen.Home -> HomeScreen(navigate)
                Screen.Tarot -> TarotScreen(back)
                Screen.Pendulum -> PendulumScreen(back)
                Screen.Baths -> RitualListScreen(Categories.baths, Rituals.baths, back, navigate)
                Screen.Cleansings -> RitualListScreen(Categories.cleansings, Rituals.cleansings, back, navigate)
                Screen.Prayers -> PrayerListScreen(back, navigate)
                Screen.Numerology -> NumerologyScreen(back)
                is Screen.RitualDetail -> RitualDetailScreen(screen.ritual, screen.effect, back, navigate)
                is Screen.Guided -> GuidedRitualScreen(screen.ritual, screen.effect, back)
                is Screen.PrayerDetail -> PrayerDetailScreen(screen.prayer, back)
            }
        }
    }
}
