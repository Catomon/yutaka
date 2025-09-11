package io.github.catomon.yutaka.di

import de.jensklingenberg.ktorfit.Ktorfit
import io.github.catomon.yutaka.data.remote.dto.DanbooruApi
import io.github.catomon.yutaka.data.remote.dto.createDanbooruApi
import io.github.catomon.yutaka.tokenBooru
import io.github.catomon.yutaka.usernameBooru
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BasicAuthCredentials
import io.ktor.client.plugins.auth.providers.basic
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val commonModule = module {
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
}

//val appModule = module {
//    viewModel<KagaminViewModel> { KagaminViewModel(get()) }
//}

//val androidModule = module {
//    single { ExoPlayer.Builder(get()).build() }
//    single<AudioPlayerService> { AudioPlayerServiceImpl(get()) }
//}