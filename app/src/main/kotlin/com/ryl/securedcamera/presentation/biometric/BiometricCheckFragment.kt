package com.ryl.securedcamera.presentation.biometric

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.ryl.securedcamera.R
import com.ryl.securedcamera.data.crypto.CipherProvider
import com.ryl.securedcamera.presentation.biometric.model.BiometricScreenAuthFlow
import com.ryl.securedcamera.presentation.biometric.router.BiometricCheckRouter
import com.ryl.securedcamera.presentation.biometric.router.BiometricCheckRouterImpl
import com.ryl.securedcamera.utils.biometricCallbacks
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class BiometricCheckFragment : Fragment(R.layout.fragment_biometric) {

    private val viewModel by viewModel<BiometricCheckViewModel>()
    private val router: BiometricCheckRouter by lazy { BiometricCheckRouterImpl(findNavController()) }
    private val cipherProvider: CipherProvider by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()

        viewModel.onScreenStarted()
    }

    private fun setupObservers() {
        viewModel.authenticationFlow.observe(viewLifecycleOwner) {
            when (it) {
                is BiometricScreenAuthFlow.Authenticate -> authenticate()
                is BiometricScreenAuthFlow.BiometricNotAvailableDialog -> showBiometricNotAvailableError()
            }
        }
        viewModel.error.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            requireActivity().finish()
        }
    }

    private fun showBiometricNotAvailableError() {
        Toast.makeText(
            requireContext(),
            "Biometric sensor is required for this app to work",
            Toast.LENGTH_LONG
        ).show()
        requireActivity().finish()
    }

    private fun authenticate() {
        val executor = ContextCompat.getMainExecutor(requireContext())
        val biometricPrompt = BiometricPrompt(this, executor,
            biometricCallbacks(
                { errorCode, errorMessage -> viewModel.onAuthError(errorCode, errorMessage) },
                { viewModel.onAuthSuccess(it, router) },
                { viewModel.onAuthFailure() }
            )
        )

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(getString(R.string.biometric_dialog_title))
            .setSubtitle(getString(R.string.biometric_dialog_subtitle))
            .setNegativeButtonText(getString(R.string.biometric_dialog_cancel))
            .build()

        biometricPrompt.authenticate(
            promptInfo,
            BiometricPrompt.CryptoObject(cipherProvider.provideInitializedEncryptCipher())
        )
    }
}