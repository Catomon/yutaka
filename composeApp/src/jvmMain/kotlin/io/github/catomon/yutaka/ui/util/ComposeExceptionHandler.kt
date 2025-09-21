package io.github.catomon.yutaka.ui.util

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import java.io.File
import kotlin.concurrent.thread

fun ApplicationScope.setComposeExceptionHandler() {
    Thread.setDefaultUncaughtExceptionHandler { _, e ->
        e.printStackTrace()

        try {
            File("last_error.txt").writeText(e.stackTraceToString())
        } catch (e: Exception) {
            e.printStackTrace()
        }

        thread {
            application {
                Window(
                    onCloseRequest = ::exitApplication,
                    state = rememberWindowState(width = 300.dp, height = 250.dp),
                    visible = true,
                    title = "Error",
                ) {
                    val clipboard = LocalClipboardManager.current

                    Box(contentAlignment = Alignment.Companion.Center) {
                        SelectionContainer {
                            Text(
                                e.stackTraceToString(), Modifier.Companion.fillMaxSize().verticalScroll(
                                    rememberScrollState()
                                )
                            )
                        }
                        Button({
                            clipboard.setText(AnnotatedString(e.stackTraceToString()))
                        }, Modifier.Companion.align(Alignment.Companion.BottomCenter)) {
                            Text("Copy")
                        }
                    }
                }
            }
        }

        exitApplication()
    }
}