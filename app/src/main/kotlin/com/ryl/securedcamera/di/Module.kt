package com.ryl.securedcamera.di

import com.ryl.securedcamera.presentation.biometric.di.biometricCheckModule
import com.ryl.securedcamera.presentation.camera.di.cameraScreenModule
import com.ryl.securedcamera.presentation.gallery.di.galleryScreenModule
import com.ryl.securedcamera.presentation.main.di.mainScreenModule

val presentationModules = listOf(
    mainScreenModule,
    cameraScreenModule,
    galleryScreenModule,
    biometricCheckModule
)