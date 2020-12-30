package com.ryl.securedcamera.utils.androidservices

import android.content.Context
import androidx.biometric.BiometricManager

interface AndroidServicesProvider {

    fun biometricManager(): BiometricManager
}

class AndroidServicesProviderImpl(private val context: Context): AndroidServicesProvider {

    override fun biometricManager(): BiometricManager = BiometricManager.from(context)
}