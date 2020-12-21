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

    private fun setupObservers() {
        viewModel.error.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            requireActivity().finish()
        }
    }
}