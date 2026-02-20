package io.github.catomon.yutaka

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.view.WindowInsetsControllerCompat
import io.github.catomon.yutaka.ui.local_providers.LocalWindowManager
import io.github.catomon.yutaka.ui.util.DesktopWindowManager

class AppActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val windowManager = DesktopWindowManager(
                minimize = {
                    false
                },
                maximize = {
                    true
                },
                closeWindow = {

                }
            )

            CompositionLocalProvider(LocalWindowManager provides windowManager) {
                MobileApp()
            }
        }
    }
}
