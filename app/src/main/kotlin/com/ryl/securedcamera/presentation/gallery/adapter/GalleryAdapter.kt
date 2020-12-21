package com.ryl.securedcamera.presentation.gallery.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ryl.securedcamera.R
import com.ryl.securedcamera.data.crypto.ImageEncryptor
import kotlinx.android.synthetic.main.item_gallery.view.*
import java.io.File

class GalleryAdapter(
    private val layoutInflater: LayoutInflater,
    private val imageEncryptor: ImageEncryptor
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
                val decryptedBitmap = imageEncryptor.decryptImageFile(item.image)
                galleryItemImageView.setImageBitmap(decryptedBitmap)
            }
        }
    }
}

data class GalleryItem(val image: File)
