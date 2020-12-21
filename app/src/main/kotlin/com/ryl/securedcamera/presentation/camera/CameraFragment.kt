package com.ryl.securedcamera.presentation.camera

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.ryl.securedcamera.R
import com.ryl.securedcamera.presentation.camera.model.CameraScreenEffect
import com.ryl.securedcamera.presentation.camera.router.CameraScreenRouter
import com.ryl.securedcamera.presentation.camera.router.CameraScreenRouterImpl
import io.fotoapparat.Fotoapparat
import io.fotoapparat.configuration.CameraConfiguration
import io.fotoapparat.selector.back
import kotlinx.android.synthetic.main.fragment_camera.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class CameraFragment : Fragment(R.layout.fragment_camera) {

    private lateinit var cameraEngine: Fotoapparat
    private val cameraConfiguration = CameraConfiguration.default()

    private val viewModel by viewModel<CameraViewModel>()
    private val router: CameraScreenRouter by lazy { CameraScreenRouterImpl(findNavController()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cameraEngine = Fotoapparat(
            context = requireContext(),
            view = cameraScreenCameraView,
            lensPosition = back(),
            cameraErrorCallback = { viewModel.onCameraError(it) }
        )
        setupListeners()
        setupObservers()
        viewModel.onScreenStart()
    }

    override fun onStart() {
        super.onStart()
        cameraEngine.start()
    }

    override fun onStop() {
        super.onStop()
        cameraEngine.stop()
    }

    private fun setupListeners() {
        cameraScreenChangeCameraButton.setOnClickListener { viewModel.onChangeCameraClicked() }
        cameraScreenTakePictureButton.setOnClickListener { viewModel.onTakePictureClicked() }
    }

    private fun setupObservers() {
        viewModel.run {
            lensPosition.observe(viewLifecycleOwner) {
                cameraEngine.switchTo(it.selector, cameraConfiguration)
            }
            errors.observe(viewLifecycleOwner) {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
            effects.observe(viewLifecycleOwner) {
                when (it) {
                    is CameraScreenEffect.TakePicture -> takePicture(it.file)
                }
            }
        }
    }

    private fun takePicture(file: File) {
        cameraEngine
            .takePicture()
            .saveToFile(file)
            .whenAvailable { viewModel.onPictureTaken(file, router) }
    }
}