package io.github.catomon.yutaka

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.request.crossfade
import coil3.request.maxBitmapSize
import coil3.size.Precision
import coil3.size.Size
import io.github.catomon.yutaka.di.commonModule
import io.github.catomon.yutaka.domain.PostRepository
import io.github.catomon.yutaka.ui.MainScreen
import io.github.catomon.yutaka.ui.theme.AppTheme
import io.github.catomon.yutaka.ui.viewmodel.MainViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.get

@Preview
@Composable
internal fun DesktopApp(modifier: Modifier = Modifier) = AppTheme(modifier = modifier) {
    //WindowInsets.safeDrawing
    // .windowInsetsPadding()

    startKoin {
        modules(commonModule)
    }

    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .crossfade(true)
            .maxBitmapSize(Size.ORIGINAL)
            .precision(Precision.INEXACT)
            .build()
    }

    MainScreen()
}