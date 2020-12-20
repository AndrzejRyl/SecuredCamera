package com.ryl.securedcamera.presentation.camera.model

import java.io.File

sealed class CameraScreenEffect {

    data class TakePicture(val file: File): CameraScreenEffect()
}