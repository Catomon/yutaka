package io.github.catomon.yutaka.ui.v2

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.request.crossfade
import coil3.request.maxBitmapSize
import coil3.size.Precision
import coil3.size.Size
import io.github.catomon.yutaka.ui.v2.components.yutakaWindowDecoration
import io.github.catomon.yutaka.ui.v2.theme.YutakaTheme
import io.github.catomon.yutaka.ui.viewmodel.MainViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun YutakaV2App(
    window: java.awt.Window,
    viewModel: MainViewModel = koinViewModel(),
    modifier: Modifier = Modifier,
) {
    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .crossfade(true)
            .maxBitmapSize(Size.ORIGINAL)
            .precision(Precision.INEXACT)
            .build()
    }

    YutakaTheme {
        YutakaV2Screen(
            viewModel = viewModel,
            window = window,
            modifier = modifier
                .fillMaxSize()
                .yutakaWindowDecoration(),
        )
    }
}
