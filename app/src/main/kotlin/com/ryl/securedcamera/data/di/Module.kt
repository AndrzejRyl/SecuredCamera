package com.ryl.securedcamera.data.di

import com.ryl.securedcamera.data.images.ImagesRepository
import com.ryl.securedcamera.data.images.ImagesRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {

    single<ImagesRepository> {
        ImagesRepositoryImpl(
            filesDir = androidContext().filesDir
        )
    }
}