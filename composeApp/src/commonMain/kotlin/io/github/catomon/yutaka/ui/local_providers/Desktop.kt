package io.github.catomon.yutaka.ui.local_providers

import androidx.compose.runtime.compositionLocalOf
import io.github.catomon.yutaka.ui.util.DesktopWindowManager

val LocalWindowManager =  compositionLocalOf<DesktopWindowManager> { error("DesktopWindowManager was not provided") }