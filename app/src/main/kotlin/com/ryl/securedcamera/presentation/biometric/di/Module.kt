package com.ryl.securedcamera.presentation.biometric.di

import com.ryl.securedcamera.presentation.biometric.BiometricCheckViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val biometricCheckModule = module {
    viewModel { BiometricCheckViewModel() }
}