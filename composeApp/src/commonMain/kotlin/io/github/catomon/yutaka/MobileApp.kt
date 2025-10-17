package io.github.catomon.yutaka

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.request.crossfade
import coil3.request.maxBitmapSize
import coil3.size.Precision
import coil3.size.Size
import io.github.catomon.yutaka.ui.DesktopMainScreen
import io.github.catomon.yutaka.ui.VerticalMainScreen
import io.github.catomon.yutaka.ui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
internal fun MobileApp(modifier: Modifier = Modifier) = AppTheme(modifier = modifier) {
    //WindowInsets.safeDrawing
    // .windowInsetsPadding()

    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .crossfade(true)
            .maxBitmapSize(Size.ORIGINAL)
            .precision(Precision.INEXACT)
            .build()
    }

    VerticalMainScreen()
}