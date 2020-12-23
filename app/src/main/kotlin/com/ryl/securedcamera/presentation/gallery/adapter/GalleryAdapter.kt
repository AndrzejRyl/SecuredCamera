package com.ryl.securedcamera.presentation.gallery.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ryl.securedcamera.R
import com.ryl.securedcamera.utils.hide
import com.ryl.securedcamera.utils.show
import kotlinx.android.synthetic.main.item_gallery.view.*
import java.io.File

class GalleryAdapter(
    private val layoutInflater: LayoutInflater,
    private val loadImage: (File, (Bitmap?) -> Unit) -> Unit
) : ListAdapter<GalleryItem, GalleryAdapter.GalleryViewHolder>(ItemDiffer(GalleryItem::image)) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder =
        GalleryViewHolder(parent)

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class GalleryViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        layoutInflater.inflate(R.layout.item_gallery, parent, false)
    ) {

        fun bind(item: GalleryItem) {
            with(itemView) {
                galleryItemProgressBar.show()
                loadImage(item.image) { bitmap ->
                    bitmap?.let { galleryItemImageView.setImageBitmap(it) }
                    galleryItemProgressBar.hide()
                }
            }
        }
    }
}

data class GalleryItem(val image: File)
