package io.github.catomon.yutaka.ui.v2

import io.github.catomon.yutaka.data.local.platform.ensureYutakaDownloadsFolder
import io.github.catomon.yutaka.domain.Post
import java.awt.Desktop
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

/** Web page of the post on its booru. */
fun Post.postPageUrl(): String =
    if (host.contains("safebooru")) "https://safebooru.org/index.php?page=post&s=view&id=$id"
    else "https://danbooru.donmai.us/posts/$id"

/** The artist source when it is a URL, the post page otherwise. */
fun Post.sourceUrl(): String =
    source.takeIf { it.startsWith("http://") || it.startsWith("https://") } ?: postPageUrl()

fun openDownloadsFolder(): Boolean = runCatching {
    val folder = ensureYutakaDownloadsFolder()
    if (!Desktop.isDesktopSupported()) return false
    val awt = Desktop.getDesktop()
    if (!awt.isSupported(Desktop.Action.OPEN)) return false
    awt.open(folder)
    true
}.getOrDefault(false)

fun copyToClipboard(text: String) {
    runCatching {
        Toolkit.getDefaultToolkit().systemClipboard.setContents(StringSelection(text), null)
    }
}
