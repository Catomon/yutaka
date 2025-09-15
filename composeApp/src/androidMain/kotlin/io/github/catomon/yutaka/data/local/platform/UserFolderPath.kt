package io.github.catomon.yutaka.data.local.platform

import io.github.catomon.yutaka.YutakaApp
import io.github.catomon.yutaka.appContext
import java.io.File

actual val userFolderPath: String = File((appContext as YutakaApp).filesDir.toURI()).absolutePath