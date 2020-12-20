package com.ryl.securedcamera.presentation.camera

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import io.fotoapparat.exception.camera.CameraException
import io.fotoapparat.selector.LensPositionSelector
import io.fotoapparat.selector.back

class CameraViewModel : ViewModel() {

    private val _leanPosition = MutableLiveData<LensPositionSelector>(back())
    val leanPosition: LiveData<LensPositionSelector> = _leanPosition.distinctUntilChanged()


    fun onCameraError(cameraException: CameraException) {

    }

}
