package com.ryl.securedcamera.presentation.camera

import androidx.lifecycle.*
import com.ryl.securedcamera.data.crypto.ImageEncryptor
import com.ryl.securedcamera.data.images.ImagesRepository
import com.ryl.securedcamera.presentation.camera.model.CameraScreenEffect
import com.ryl.securedcamera.presentation.camera.router.CameraScreenRouter
import com.ryl.securedcamera.utils.SingleLiveEvent
import com.ryl.securedcamera.utils.dispatchers.DefaultDispatcherProvider
import com.ryl.securedcamera.utils.dispatchers.DispatcherProvider
import io.fotoapparat.exception.camera.CameraException
import io.fotoapparat.selector.LensPositionSelector
import io.fotoapparat.selector.back
import io.fotoapparat.selector.front
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import java.io.File

class CameraViewModel(
    private val imagesRepository: ImagesRepository,
    private val imageEncryptor: ImageEncryptor,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider()
) : ViewModel() {

    private val _lensPosition = MutableLiveData<LensPosition>(LensPosition.BACK)
    val lensPosition: LiveData<LensPosition> = _lensPosition.distinctUntilChanged()

    private val _errors = SingleLiveEvent<String>()
    val errors = _errors

    private val _effects = SingleLiveEvent<CameraScreenEffect>()
    val effects = _effects

    private val _encryptionProgress = MutableLiveData<Int>(INITIAL_PROGRESS)
    val encryptionProgress = _encryptionProgress.distinctUntilChanged()

    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        _errors.value = throwable.message
    }

    fun onScreenStart() {
        viewModelScope.launch(dispatcherProvider.main + errorHandler) {
            imagesRepository.createGalleryFolderIfNecessary()
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
        viewModelScope.launch(dispatcherProvider.main + errorHandler) {
            _effects.value = CameraScreenEffect.TakePicture(generatePictureFile())
        }
    }

    private suspend fun generatePictureFile(): File = imagesRepository.generateNewImageFile()

    fun onPictureTaken(file: File, router: CameraScreenRouter) {
        viewModelScope.launch(dispatcherProvider.main + errorHandler) {
            imageEncryptor.encryptImageFile(file) {
                _encryptionProgress.value = it
            }
            router.navigateToGallery()
        }
    }

    companion object {
        const val INITIAL_PROGRESS = 0
        const val FULL_PROGRESS = 100
    }
}

enum class LensPosition(val selector: LensPositionSelector) {
    FRONT(front()), BACK(back())
}