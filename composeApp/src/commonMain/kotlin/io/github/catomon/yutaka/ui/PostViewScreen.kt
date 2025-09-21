package io.github.catomon.yutaka.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import io.github.catomon.yutaka.domain.Post
import io.ktor.client.HttpClient
import io.ktor.client.plugins.timeout
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.readRawBytes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

private val client = HttpClient()

suspend fun downloadPost(post: Post): Boolean = withContext(Dispatchers.IO) {
    try {
        val response: HttpResponse = client.get(post.originalUri) {
            timeout {
                requestTimeoutMillis = 600000
                socketTimeoutMillis = 120000
            }
        }
        val bytes = response.readRawBytes()
        val userHome = System.getProperty("user.home") ?: ""
        val desktopPath = File(userHome, "Desktop")
        val fileName = post.id + "." + post.fileExt
        val file = File(desktopPath, fileName)
        if (!file.exists())// {
            file.writeBytes(bytes)
            true
//        } else {
//            false
//        }
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

@Composable
fun PostViewScreen(post: Post, onClose: () -> Unit, modifier: Modifier = Modifier) {
    var newSize by remember { mutableStateOf(IntSize(0, 0)) }
    var currentSize by remember { mutableStateOf(IntSize(0, 0)) }

    LaunchedEffect(newSize) {
        if (currentSize.height < newSize.height || currentSize.width < newSize.width) {
            delay(350)
            currentSize = newSize
        }
    }

    Box(modifier.onSizeChanged {
        newSize = it
        if (currentSize.width == 0 && currentSize.height == 0)
            currentSize = it
    }, contentAlignment = Alignment.Center) {
        Box(Modifier.matchParentSize().padding(bottom = 16.dp)) {
            key(currentSize) {
                AsyncImage(
                    post.originalUri, "", Modifier.fillMaxSize(), contentScale = ContentScale.Fit
                )
            }
        }
    }
}