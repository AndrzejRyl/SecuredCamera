package com.ryl.securedcamera.utils

import androidx.biometric.BiometricPrompt

fun biometricCallbacks(
    onAuthenticationError: (Int, CharSequence) -> Unit,
    onAuthenticationSuccess: (BiometricPrompt.AuthenticationResult) -> Unit,
    onAuthenticationFailure: () -> Unit
): BiometricPrompt.AuthenticationCallback {
    return object : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationError(
            errorCode: Int,
            errString: CharSequence
        ) {
            super.onAuthenticationError(errorCode, errString)
            onAuthenticationError(errorCode, errString)
        }

        override fun onAuthenticationSucceeded(
            result: BiometricPrompt.AuthenticationResult
        ) {
            super.onAuthenticationSucceeded(result)
            onAuthenticationSuccess(result)
        }

        override fun onAuthenticationFailed() {
            super.onAuthenticationFailed()
            onAuthenticationFailure()
        }
    }
}