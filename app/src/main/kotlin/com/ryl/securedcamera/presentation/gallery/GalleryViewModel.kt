package com.ryl.securedcamera.presentation.gallery

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.viewModelScope
import com.ryl.securedcamera.data.crypto.ImageEncryptor
import com.ryl.securedcamera.data.images.ImagesRepository
import com.ryl.securedcamera.presentation.gallery.adapter.GalleryItem
import com.ryl.securedcamera.utils.dispatchers.DefaultDispatcherProvider
import com.ryl.securedcamera.utils.dispatchers.DispatcherProvider
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import java.io.File

class GalleryViewModel(
    private val imagesRepository: ImagesRepository,
    private val imageEncryptor: ImageEncryptor,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider()
) : ViewModel() {

    private val _images = MutableLiveData<List<GalleryItem>>(listOf())
    val images = _images.distinctUntilChanged()

    private val decryptImageExceptionHandler = CoroutineExceptionHandler { _, _ ->
        Log.d("Gallery", "Decryption failed")
    }

    fun onScreenStarted() {
        viewModelScope.launch(dispatcherProvider.main) {
            _images.value = imagesRepository.getImages().map { GalleryItem(it.absoluteFile) }
        }
    }

    fun onLoadImageRequested(file: File, callback: (Bitmap?) -> Unit) {
        viewModelScope.launch(dispatcherProvider.main + decryptImageExceptionHandler) {
            imageEncryptor.decryptImageFile(file, callback)
        }
    }
}