import androidx.compose.ui.window.application
import io.github.catomon.yutaka.di.commonModule
import io.github.catomon.yutaka.ui.MainWindow
import io.github.catomon.yutaka.ui.WindowConfig
import io.github.catomon.yutaka.ui.util.setComposeExceptionHandler
import io.github.catomon.yutaka.util.echoMsg
import io.github.catomon.yutaka.util.echoWarn
import org.jetbrains.skiko.GraphicsApi
import org.koin.core.context.startKoin
import javax.swing.JOptionPane

fun main() {
    setDefaultExceptionHandler()

    application {
        setComposeExceptionHandler()

        setRenderApi()

        startKoin {
            modules(commonModule)
        }

        MainWindow(::exitApplication)
    }
}

val osName = System.getProperty("os.name").lowercase()
private fun setRenderApi() {
    try {
        if (osName.contains("win")) {
            val renderApi = GraphicsApi.OPENGL.name
            System.setProperty("skiko.renderApi", renderApi)
            echoMsg { "skiko.renderApi = $renderApi" }
        } else {
            System.setProperty("skiko.renderApi", GraphicsApi.SOFTWARE_FAST.name)
            echoMsg { "skiko.renderApi = ${GraphicsApi.SOFTWARE_FAST.name}" }
        }
        WindowConfig.isTransparent = true
    } catch (e: Exception) {
        WindowConfig.isTransparent = false
        echoWarn { "Could not set desired render api. The window may not have transparency if DX12 is not supported." }
        e.printStackTrace()
    }
}

private fun setDefaultExceptionHandler() {
    Thread.setDefaultUncaughtExceptionHandler { _, e ->
        JOptionPane.showMessageDialog(
            null,
            e.stackTraceToString(),
            "Error",
            JOptionPane.ERROR_MESSAGE
        )
    }
}