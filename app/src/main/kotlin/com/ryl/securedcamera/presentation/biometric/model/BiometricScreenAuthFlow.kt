package com.ryl.securedcamera.presentation.biometric.model

sealed class BiometricScreenAuthFlow {
    object Authenticate: BiometricScreenAuthFlow()
    object BiometricNotAvailableDialog: BiometricScreenAuthFlow()
}