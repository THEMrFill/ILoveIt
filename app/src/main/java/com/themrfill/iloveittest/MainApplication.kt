package com.themrfill.iloveittest

import android.app.Application
import com.themrfill.iloveittest.di.networkModule
import com.themrfill.iloveittest.di.vmModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(vmModule, networkModule)
        }
    }
}