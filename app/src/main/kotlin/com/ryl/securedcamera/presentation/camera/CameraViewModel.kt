package com.ryl.securedcamera.presentation.camera

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import io.fotoapparat.exception.camera.CameraException
import io.fotoapparat.selector.LensPositionSelector
import io.fotoapparat.selector.back
import io.fotoapparat.selector.front

class CameraViewModel : ViewModel() {

    private val _lensPosition = MutableLiveData<LensPosition>(LensPosition.BACK)
    val lensPosition: LiveData<LensPosition> = _lensPosition.distinctUntilChanged()


    fun onCameraError(cameraException: CameraException) {
        // todo
    }

    fun onChangeCameraClicked() {
        _lensPosition.value = when (_lensPosition.value) {
            LensPosition.BACK -> LensPosition.FRONT
            else -> LensPosition.BACK
        }
    }

    fun onTakePictureClicked() {
        TODO("Not yet implemented")
    }

}

enum class LensPosition(val selector: LensPositionSelector) {
    FRONT(front()), BACK(back())
}