import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import coil3.compose.AsyncImage
import de.jensklingenberg.ktorfit.Ktorfit
import io.github.catomon.yutaka.data.remote.dto.DanbooruApi
import io.github.catomon.yutaka.data.remote.dto.PostItemDto
import io.github.catomon.yutaka.data.remote.dto.createDanbooruApi
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BasicAuthCredentials
import io.ktor.client.plugins.auth.providers.basic
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import org.jetbrains.compose.resources.painterResource
import yutaka.composeapp.generated.resources.Res
import yutaka.composeapp.generated.resources._894jfct80ip51
import java.awt.Dimension
import java.net.SocketException
import java.util.concurrent.CancellationException

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

val json = Json {
    ignoreUnknownKeys = true
    prettyPrint = true
    isLenient = true
}

@Composable
fun Test(api: DanbooruApi) {
    var posts by remember { mutableStateOf(emptyList<PostItemDto>()) }
    var tries by remember { mutableStateOf(0) }
    var status by remember { mutableStateOf("") }

    LoadPostsEffect(
        api = api, tries = tries,
        onPostsLoaded = { posts = it },
        onStatusUpdate = { status = it }
    )

    Column {
        Row {
            Button({
                tries++
            }) {
                Text("Refresh")
            }

            Text(status)
        }
        if (posts.isEmpty())
            Text("Empty")
        else
            LazyVerticalGrid(GridCells.Adaptive(150.dp), Modifier.fillMaxSize().background(Color.Black)) {
                items(posts.size, key = { posts[it].id }) {
                    val post = posts[it]

                    Box(Modifier.animateItem().size(150.dp, 200.dp).padding(2.dp), contentAlignment = Alignment.Center) {
                        AsyncImage(
                            model = post.previewFileUrl,
                            contentDescription = null,
                            modifier = Modifier.clip(RoundedCornerShape(3.dp)),
                            placeholder = painterResource(Res.drawable._894jfct80ip51),
                            contentScale = ContentScale.Fit,
                            alignment = Alignment.Center
                        )
                    }
                }
            }
    }
}

@Composable
fun LoadPostsEffect(
    api: DanbooruApi,
    tries: Int,
    onPostsLoaded: (List<PostItemDto>) -> Unit,
    onStatusUpdate: (String) -> Unit
) {
    val json = remember {
        Json { ignoreUnknownKeys = true; isLenient = true }
    }

    LaunchedEffect(tries) {
        val posts: MutableList<PostItemDto> = mutableListOf()
        try {
            val jsonArray = api.getPosts().jsonArray
            for (jsonElement in jsonArray) {
                try {
                    val post = json.decodeFromJsonElement(PostItemDto.serializer(), jsonElement)
                    if (post.rating in listOf("g", "s", "q")) {
                        posts.add(post)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            onStatusUpdate("Success")
            onPostsLoaded(posts)
        } catch (e: HttpRequestTimeoutException) {
            onStatusUpdate("Request Timeout")
            e.printStackTrace()
        } catch (e: SocketException) {
            onStatusUpdate(e.message ?: "Unknown error")
            e.printStackTrace()
        } catch (_: CancellationException) {

        } catch (e: Exception) {
            onStatusUpdate(e.message ?: "Unknown error")
            e.printStackTrace()
        }
    }
}