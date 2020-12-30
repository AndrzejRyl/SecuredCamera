package com.ryl.securedcamera.presentation.biometric

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK
import androidx.biometric.BiometricPrompt
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import com.ryl.securedcamera.data.crypto.ImageEncryptor
import com.ryl.securedcamera.presentation.biometric.model.BiometricScreenAuthFlow
import com.ryl.securedcamera.presentation.biometric.router.BiometricCheckRouter
import com.ryl.securedcamera.utils.SingleLiveEvent
import com.ryl.securedcamera.utils.androidservices.AndroidServicesProvider

class BiometricCheckViewModel(
    private val imageEncryptor: ImageEncryptor,
    private val androidServicesProvider: AndroidServicesProvider
) : ViewModel() {

    private val _error = SingleLiveEvent<String>()
    val error = _error

    private val _authenticationFlow = MutableLiveData<BiometricScreenAuthFlow>()
    val authenticationFlow = _authenticationFlow.distinctUntilChanged()

    fun onScreenStarted() {
        val biometricManager = androidServicesProvider.biometricManager()

        when (biometricManager.canAuthenticate(BIOMETRIC_WEAK)) {
            BiometricManager.BIOMETRIC_SUCCESS ->
                _authenticationFlow.value = BiometricScreenAuthFlow.Authenticate
            else -> _authenticationFlow.value = BiometricScreenAuthFlow.BiometricNotAvailableDialog
        }
    }

    fun onAuthError(errorCode: Int, errorMessage: CharSequence) {
        onError()
    }

    private fun onError() {
        _error.value = "Authentication failed"
    }

    fun onAuthSuccess(
        authenticationResult: BiometricPrompt.AuthenticationResult,
        router: BiometricCheckRouter
    ) {
        authenticationResult.cryptoObject?.cipher?.let {
            imageEncryptor.encryptCipher = it
            router.navigateToCameraScreen()
        } ?: onError()
    }

    fun onAuthFailure() {
        onError()
    }
}