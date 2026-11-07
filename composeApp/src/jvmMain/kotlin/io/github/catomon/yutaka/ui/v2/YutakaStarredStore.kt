package io.github.catomon.yutaka.ui.v2

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import io.github.catomon.yutaka.data.local.platform.userFolderPath
import io.github.catomon.yutaka.domain.Post
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import java.io.File

/**
 * Bookmarks saved as JSON in the Yutaka documents folder.
 * The list is newest-starred first so the saved view has a natural order.
 */
class YutakaStarredStore {
    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
    }
    private val file = File(userFolderPath, "starred_posts.json")

    var posts by mutableStateOf<List<Post>>(emptyList())
        private set

    init {
        load()
    }

    fun isStarred(post: Post): Boolean =
        posts.any { it.host == post.host && it.id == post.id }

    fun toggle(post: Post) {
        posts = if (isStarred(post)) {
            posts.filterNot { it.host == post.host && it.id == post.id }
        } else {
            listOf(post) + posts
        }
        save()
    }

    private fun load() {
        runCatching {
            if (!file.exists()) return
            posts = json.decodeFromString(ListSerializer(Post.serializer()), file.readText())
        }
    }

    private fun save() {
        runCatching {
            file.parentFile?.mkdirs()
            file.writeText(json.encodeToString(ListSerializer(Post.serializer()), posts))
        }
    }
}
