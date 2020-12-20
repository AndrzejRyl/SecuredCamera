package com.ryl.securedcamera.data.images

import java.io.File

class ImagesRepositoryImpl(
    private val filesDir: File
) : ImagesRepository {

    override suspend fun getImages(): List<File> {
        val galleryFolder = File(filesDir, GALLERY_PATH)
        return galleryFolder.listFiles()?.toList() ?: listOf()
    }

    override suspend fun generateNewImageFile(): File =
        File(filesDir, GALLERY_PATH + "/" + System.currentTimeMillis() + ".jpg")

    override suspend fun createGalleryFolderIfNecessary() {
        val galleryFolder = File(filesDir, GALLERY_PATH)
        if (!galleryFolder.exists()) {
            galleryFolder.mkdir()
        }
    }

    companion object {
        private const val GALLERY_PATH = "secure_gallery"
    }
}