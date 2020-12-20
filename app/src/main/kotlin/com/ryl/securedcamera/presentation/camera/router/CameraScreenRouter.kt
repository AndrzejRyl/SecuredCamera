package com.ryl.securedcamera.presentation.camera.router

import androidx.navigation.NavController
import com.ryl.securedcamera.R

interface CameraScreenRouter {

    fun navigateToGallery()
}

class CameraScreenRouterImpl(private val navController: NavController) : CameraScreenRouter {

    override fun navigateToGallery() = navController.navigate(R.id.galleryFragment)
}