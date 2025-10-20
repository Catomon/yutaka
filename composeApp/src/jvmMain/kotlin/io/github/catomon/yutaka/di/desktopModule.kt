package io.github.catomon.yutaka.di

import io.github.catomon.yutaka.data.local.platform.ImageSaverImpl
import io.github.catomon.yutaka.domain.ImageSaver
import org.koin.dsl.module

val desktopModule = module {
    single<ImageSaver> { ImageSaverImpl() }
}