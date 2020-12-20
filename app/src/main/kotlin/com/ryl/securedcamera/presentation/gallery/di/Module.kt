package com.ryl.securedcamera.presentation.gallery.di

import com.ryl.securedcamera.presentation.gallery.GalleryViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val galleryScreenModule = module {

    viewModel {
        GalleryViewModel(
            imagesRepository = get()
        )
    }
}