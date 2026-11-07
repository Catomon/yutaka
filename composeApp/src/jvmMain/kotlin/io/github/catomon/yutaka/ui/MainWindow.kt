package io.github.catomon.yutaka.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberWindowState
import io.github.catomon.yutaka.ui.local_providers.LocalWindowManager
import io.github.catomon.yutaka.ui.util.DesktopWindowManager
import io.github.catomon.yutaka.ui.v2.YutakaV2App
import org.jetbrains.compose.resources.painterResource
import yutaka.composeapp.generated.resources.Res
import yutaka.composeapp.generated.resources.icon
import java.awt.Dimension

object WindowConfig {
    const val WIDTH = 1000
    const val HEIGHT = 700

    var isTransparent = true

    val isTraySupported = androidx.compose.ui.window.isTraySupported
}

@Composable
fun MainWindow(exitApplication: () -> Unit) {
    val windowState = rememberWindowState(
        width = WindowConfig.WIDTH.dp,
        height = WindowConfig.HEIGHT.dp,
        position = WindowPosition(Alignment.Center),
    )
    var isVisible by remember { mutableStateOf(true) }

    if (isVisible)
        Window(
            title = "Yutaka",
            state = windowState,
            onCloseRequest = exitApplication,
            undecorated = true,
            transparent = WindowConfig.isTransparent,
            icon = painterResource(Res.drawable.icon)
        ) {
            window.minimumSize = Dimension(760, 540)
            window.maximumSize = Dimension(1600, 1100)

            val windowManager = DesktopWindowManager(
                minimize = {
                    windowState.isMinimized = !windowState.isMinimized
                    windowState.isMinimized
                },
                maximize = {
                    if (windowState.placement == WindowPlacement.Maximized) {
                        windowState.size = DpSize(WindowConfig.WIDTH.dp, WindowConfig.HEIGHT.dp)
                        windowState.position = WindowPosition(Alignment.Center)
                        windowState.placement = WindowPlacement.Floating
                        false
                    } else {
                        windowState.placement = WindowPlacement.Maximized
                        true
                    }
                },
                closeWindow = exitApplication
            )

            CompositionLocalProvider(LocalWindowManager provides windowManager) {
                YutakaV2App(window = window)
            }
        }
}
