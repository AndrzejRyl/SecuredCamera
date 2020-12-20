package com.ryl.securedcamera.presentation.gallery

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.viewModelScope
import com.ryl.securedcamera.data.images.ImagesRepository
import com.ryl.securedcamera.presentation.gallery.adapter.GalleryItem
import com.ryl.securedcamera.utils.dispatchers.DefaultDispatcherProvider
import com.ryl.securedcamera.utils.dispatchers.DispatcherProvider
import kotlinx.coroutines.launch

class GalleryViewModel(
    private val imagesRepository: ImagesRepository,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider()
) : ViewModel() {

    private val _images = MutableLiveData<List<GalleryItem>>(listOf())
    val images = _images.distinctUntilChanged()

    fun onScreenStarted() {
        viewModelScope.launch(dispatcherProvider.main) {
            _images.value = imagesRepository.getImages().map { GalleryItem(it.absoluteFile) }
        }
    }
}