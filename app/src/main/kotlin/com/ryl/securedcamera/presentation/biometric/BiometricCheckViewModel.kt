package com.ryl.securedcamera.presentation.biometric

import androidx.biometric.BiometricPrompt
import androidx.lifecycle.ViewModel
import com.ryl.securedcamera.data.crypto.ImageEncryptor
import com.ryl.securedcamera.presentation.biometric.router.BiometricCheckRouter
import com.ryl.securedcamera.utils.SingleLiveEvent

class BiometricCheckViewModel(
    private val imageEncryptor: ImageEncryptor
) : ViewModel() {

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
    ) = router.navigateToCameraScreen().also {
        imageEncryptor.cipher = authenticationResult.cryptoObject?.cipher
    }

    fun onAuthFailure() {
        onError()
    }
}