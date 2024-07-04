package com.soccersim.app

import android.app.Application
import com.soccersim.data.di.dataModule
import com.soccersim.domain.di.domainModule
import com.soccersim.presentation.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(
                dataModule, domainModule, viewModelModule
            )
        }
    }
}