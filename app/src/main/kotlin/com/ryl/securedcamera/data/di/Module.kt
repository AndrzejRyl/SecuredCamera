package com.ryl.securedcamera.data.di

import com.ryl.securedcamera.data.crypto.CipherProvider
import com.ryl.securedcamera.data.crypto.ImageEncryptor
import com.ryl.securedcamera.data.crypto.ImageEncryptorImpl
import com.ryl.securedcamera.data.images.ImagesRepository
import com.ryl.securedcamera.data.images.ImagesRepositoryImpl
import com.ryl.securedcamera.utils.androidservices.AndroidServicesProvider
import com.ryl.securedcamera.utils.androidservices.AndroidServicesProviderImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {

    single<ImagesRepository> {
        ImagesRepositoryImpl(
            filesDir = androidContext().filesDir
        )
    }

    single {
        CipherProvider()
    }

    single<ImageEncryptor> {
        ImageEncryptorImpl(
            cacheDestination = androidContext().cacheDir,
            cipherProvider = get()
        )
    }

    single<AndroidServicesProvider> {
        AndroidServicesProviderImpl(androidContext())
    }
}