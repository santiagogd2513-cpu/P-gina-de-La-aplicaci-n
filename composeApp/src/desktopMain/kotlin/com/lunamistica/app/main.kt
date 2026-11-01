package com.lunamistica.app

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import java.awt.Dimension

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Luna Mística",
        icon = painterResource("icon.png"),
        state = rememberWindowState(width = 460.dp, height = 860.dp),
    ) {
        window.minimumSize = Dimension(380, 600)
        App()
    }
}
