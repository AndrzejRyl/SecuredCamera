package com.ryl.securedcamera.presentation.camera

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import com.moodreaders.bookdeck.mvi.utils.SingleLiveEvent
import com.ryl.securedcamera.presentation.camera.model.CameraScreenEffect
import com.ryl.securedcamera.presentation.camera.router.CameraScreenRouter
import io.fotoapparat.exception.camera.CameraException
import io.fotoapparat.selector.LensPositionSelector
import io.fotoapparat.selector.back
import io.fotoapparat.selector.front
import java.io.File

class CameraViewModel(private val filesDir: File) : ViewModel() {

    private val _lensPosition = MutableLiveData<LensPosition>(LensPosition.BACK)
    val lensPosition: LiveData<LensPosition> = _lensPosition.distinctUntilChanged()

    private val _errors = SingleLiveEvent<String>()
    val errors = _errors

    private val _effects = SingleLiveEvent<CameraScreenEffect>()
    val effects = _effects

    fun onScreenStart() {
        val galleryFolder = File(filesDir, GALLERY_PATH)
        if (!galleryFolder.exists()) {
            galleryFolder.mkdir()
        }
    }

    fun onCameraError(cameraException: CameraException) {
        _errors.value = cameraException.message
    }

    fun onChangeCameraClicked() {
        _lensPosition.value = when (_lensPosition.value) {
            LensPosition.BACK -> LensPosition.FRONT
            else -> LensPosition.BACK
        }
    }

    fun onTakePictureClicked() {
        _effects.value = CameraScreenEffect.TakePicture(generatePictureFile())
    }

    private fun generatePictureFile(): File =
        File(filesDir, GALLERY_PATH + "/" + System.currentTimeMillis() + ".jpg")

    fun onPictureTaken(router: CameraScreenRouter) = router.navigateToGallery()

    companion object {
        private const val GALLERY_PATH = "secure_gallery"
    }
}

enum class LensPosition(val selector: LensPositionSelector) {
    FRONT(front()), BACK(back())
}