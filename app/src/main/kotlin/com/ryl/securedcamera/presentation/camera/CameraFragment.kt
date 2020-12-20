package com.ryl.securedcamera.presentation.camera

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import com.ryl.securedcamera.R
import io.fotoapparat.Fotoapparat
import io.fotoapparat.configuration.CameraConfiguration
import io.fotoapparat.selector.back
import kotlinx.android.synthetic.main.fragment_camera.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class CameraFragment : Fragment(R.layout.fragment_camera) {

    private lateinit var cameraEngine: Fotoapparat
    private val cameraConfiguration = CameraConfiguration.default()

    private val viewModel by viewModel<CameraViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cameraEngine = Fotoapparat(
            context = requireContext(),
            view = cameraScreenCameraView,
            lensPosition = back(),
            cameraErrorCallback = { viewModel.onCameraError(it) }
        )
        setupObservers()
    }

    override fun onStart() {
        super.onStart()
        cameraEngine.start()
    }

    override fun onStop() {
        super.onStop()
        cameraEngine.stop()
    }

    private fun setupObservers() {
        viewModel.run {
            leanPosition.observe(viewLifecycleOwner) {
                cameraEngine.switchTo(it, cameraConfiguration)
            }
        }
    }
}