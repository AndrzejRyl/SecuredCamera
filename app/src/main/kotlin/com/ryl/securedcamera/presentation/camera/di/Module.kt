package com.ryl.securedcamera.presentation.camera.di

import com.ryl.securedcamera.presentation.camera.CameraViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val cameraScreenModule = module {

    viewModel {
        CameraViewModel(
            filesDir = androidContext().filesDir
        )
    }
}