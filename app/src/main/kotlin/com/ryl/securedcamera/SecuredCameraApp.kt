package com.ryl.securedcamera

import android.app.Application
import com.ryl.securedcamera.data.di.dataModule
import com.ryl.securedcamera.di.presentationModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class SecuredCameraApp : Application() {

    override fun onCreate() {
        super.onCreate()
        setupDI()
    }

    private fun setupDI() {
        startKoin {
            androidLogger()
            androidContext(this@SecuredCameraApp)
            modules(modules = presentationModules + dataModule)
        }
    }

}