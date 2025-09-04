import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import de.jensklingenberg.ktorfit.Ktorfit
import io.github.catomon.yutaka.data.remote.dto.DanbooruApi
import io.github.catomon.yutaka.data.remote.dto.createDanbooruApi
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BasicAuthCredentials
import io.ktor.client.plugins.auth.providers.basic
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import java.awt.Dimension

fun main() = application {

    val ktorfit = Ktorfit.Builder()
        .httpClient {
            install(Auth) {
                basic {
                    credentials {
                        BasicAuthCredentials(
                            usernameBooru,
                            tokenBooru
                        )
                    }
                }
            }

            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                })
            }
        }
        .baseUrl("https://danbooru.donmai.us/")
        .build()

    val danbooruApi = ktorfit.createDanbooruApi()

    Window(
        title = "yutaka",
        state = rememberWindowState(width = 800.dp, height = 600.dp),
        onCloseRequest = ::exitApplication,
    ) {
        window.minimumSize = Dimension(350, 600)

        Test(danbooruApi)
    }
}

@Composable
fun Test(api: DanbooruApi) {
    var text by remember { mutableStateOf("Loading...") }

    LaunchedEffect(Unit) {
        text = api.getPosts()//.firstOrNull().toString()
    }

    Text(text)
}