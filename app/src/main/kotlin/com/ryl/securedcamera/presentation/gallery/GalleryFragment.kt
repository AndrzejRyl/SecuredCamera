package com.ryl.securedcamera.presentation.gallery

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ryl.securedcamera.R
import com.ryl.securedcamera.presentation.gallery.adapter.GalleryAdapter
import kotlinx.android.synthetic.main.fragment_gallery.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class GalleryFragment : Fragment(R.layout.fragment_gallery) {

    private val viewModel by viewModel<GalleryViewModel>()

    private val galleryAdapter by lazy {
        GalleryAdapter(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupObservers()
        viewModel.onScreenStarted()
    }


    private fun setupViews() {
        galleryScreenGalleryRecyclerView.adapter = galleryAdapter
        galleryScreenGalleryRecyclerView.layoutManager =
            StaggeredGridLayoutManager(COLUMNS_COUNT, StaggeredGridLayoutManager.VERTICAL)
    }

    private fun setupObservers() {
        viewModel.images.observe(viewLifecycleOwner) {
            galleryAdapter.submitList(it)
        }
    }

    companion object {
        private const val COLUMNS_COUNT = 2
    }
}