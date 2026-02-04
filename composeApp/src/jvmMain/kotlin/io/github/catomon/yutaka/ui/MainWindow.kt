package io.github.catomon.yutaka.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.rememberWindowState
import io.github.catomon.yutaka.DesktopApp
import io.github.catomon.yutaka.ui.local_providers.LocalWindowManager
import io.github.catomon.yutaka.ui.modifiers.luckyWindowDecoration
import io.github.catomon.yutaka.ui.util.DesktopWindowManager
import io.github.catomon.yutaka.ui.util.WindowDraggableArea
import org.jetbrains.compose.resources.painterResource
import yutaka.composeapp.generated.resources.Res
import yutaka.composeapp.generated.resources.icon
import java.awt.Dimension

object WindowConfig {
    const val WIDTH = 800
    const val HEIGHT = 600

    var isTransparent = true

    val isTraySupported = androidx.compose.ui.window.isTraySupported
}

@Composable
fun MainWindow(exitApplication: () -> Unit) {
    val windowState = rememberWindowState(width = WindowConfig.WIDTH.dp, height = WindowConfig.HEIGHT.dp)
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
            window.minimumSize = Dimension(350, 600)

            val windowManager = DesktopWindowManager(
                minimize = {
                    windowState.isMinimized = !windowState.isMinimized
                    windowState.isMinimized
                },
                maximize = {
                    if (windowState.placement != WindowPlacement.Floating) {
                        windowState.placement = WindowPlacement.Floating
                        false
                    } else {
                        if (windowState.placement != WindowPlacement.Maximized) {
                            windowState.placement = WindowPlacement.Maximized
                            true
                        } else false
                    }
                },
                closeWindow = exitApplication
            )

            CompositionLocalProvider(LocalWindowManager provides windowManager) {
                WindowDraggableArea {
                    DesktopApp(modifier = Modifier.luckyWindowDecoration())
                }
            }
        }
}