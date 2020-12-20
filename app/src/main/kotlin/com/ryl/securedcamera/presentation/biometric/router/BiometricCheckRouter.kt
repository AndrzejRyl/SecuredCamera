package com.ryl.securedcamera.presentation.biometric.router

import androidx.navigation.NavController
import com.ryl.securedcamera.R

interface BiometricCheckRouter {

    fun navigateToCameraScreen()
}

class BiometricCheckRouterImpl(private val navController: NavController) : BiometricCheckRouter {

    override fun navigateToCameraScreen() = navController.navigate(R.id.cameraFragment)
}