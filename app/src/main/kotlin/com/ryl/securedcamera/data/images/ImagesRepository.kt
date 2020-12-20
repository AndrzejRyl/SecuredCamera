package com.ryl.securedcamera.data.images

import java.io.File

interface ImagesRepository {

    suspend fun getImages(): List<File>
    suspend fun generateNewImageFile(): File
    suspend fun createGalleryFolderIfNecessary()
}