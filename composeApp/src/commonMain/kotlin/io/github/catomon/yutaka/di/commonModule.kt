package io.github.catomon.yutaka.di

import de.jensklingenberg.ktorfit.Ktorfit
import io.github.catomon.yutaka.data.PostRepositoryImpl
import io.github.catomon.yutaka.data.local.ImageDownloaderImpl
import io.github.catomon.yutaka.data.local.platform.createPostDatabase
import io.github.catomon.yutaka.data.remote.DanbooruApi
import io.github.catomon.yutaka.data.remote.DanbooruProvider
import io.github.catomon.yutaka.data.remote.SafebooruApi
import io.github.catomon.yutaka.data.remote.SafebooruProvider
import io.github.catomon.yutaka.data.remote.createDanbooruApi
import io.github.catomon.yutaka.data.remote.createSafebooruApi
import io.github.catomon.yutaka.domain.ImageDownloader
import io.github.catomon.yutaka.domain.PostRepository
import io.github.catomon.yutaka.tokenBooru
import io.github.catomon.yutaka.ui.viewmodel.MainViewModel
import io.github.catomon.yutaka.usernameBooru
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BasicAuthCredentials
import io.ktor.client.plugins.auth.providers.basic
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val commonModule = module {
    viewModel { MainViewModel(get(), get()) }

    single<ImageDownloader> { ImageDownloaderImpl(get()) }

    single<PostRepository> {
        val useDanbooru = false //todo config and in-app settings option
        val danbooru =
            if (useDanbooru) getOrNull<DanbooruApi>(DanbooruApi::class)?.let { DanbooruProvider(it) } else null
        val booru = danbooru ?: getOrNull<SafebooruApi>(SafebooruApi::class)?.let { SafebooruProvider(it) }
        ?: throw IllegalStateException("No booru providers")
        PostRepositoryImpl(createPostDatabase(), booru)
    }

    single<DanbooruApi> {
        Ktorfit.Builder()
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
            .createDanbooruApi()
    }

    single<SafebooruApi> {
        Ktorfit.Builder()
            .httpClient {
                install(ContentNegotiation) {
                    json(Json {
                        ignoreUnknownKeys = true
                        prettyPrint = true
                    })
                }
            }
            .baseUrl("https://safebooru.org/")
            .build()
            .createSafebooruApi()
    }
}

//val appModule = module {
//    viewModel<KagaminViewModel> { KagaminViewModel(get()) }
//}

//val androidModule = module {
//    single { ExoPlayer.Builder(get()).build() }
//    single<AudioPlayerService> { AudioPlayerServiceImpl(get()) }
//}