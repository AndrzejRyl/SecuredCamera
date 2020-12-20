package com.ryl.securedcamera.presentation.biometric

import androidx.biometric.BiometricPrompt
import androidx.lifecycle.ViewModel
import com.ryl.securedcamera.presentation.biometric.router.BiometricCheckRouter
import com.ryl.securedcamera.utils.SingleLiveEvent

class BiometricCheckViewModel : ViewModel() {

    private val _error = SingleLiveEvent<String>()
    val error = _error

    fun onAuthError(errorCode: Int, errorMessage: CharSequence) {
        onError()
    }

    private fun onError() {
        _error.value = "Authentication failed"
    }

    fun onAuthSuccess(
        authenticationResult: BiometricPrompt.AuthenticationResult,
        router: BiometricCheckRouter
    ) = router.navigateToCameraScreen()

    fun onAuthFailure() {
        onError()
    }
}