package io.github.catomon.yutaka

import android.app.Application
import android.content.Context
import io.github.catomon.yutaka.di.androidModule
import io.github.catomon.yutaka.di.commonModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

var appContext: Context? = null

class YutakaApp : Application() {
    override fun onCreate() {
        super.onCreate()

        appContext = this

        startKoin {
            androidLogger()
            androidContext(this@YutakaApp)
            modules(commonModule, androidModule)
        }
    }

    override fun onTerminate() {
        super.onTerminate()

        appContext = null
    }
}