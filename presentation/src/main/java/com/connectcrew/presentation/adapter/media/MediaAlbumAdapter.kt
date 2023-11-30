package com.connectcrew.presentation.adapter.media

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.connectcrew.presentation.R
import com.connectcrew.presentation.databinding.ItemMediaAlbumBinding
import com.connectcrew.presentation.model.media.Album
import com.connectcrew.presentation.util.executeAfter
import com.connectcrew.presentation.util.listener.setOnSingleClickListener

class MediaAlbumAdapter(
    private val selectedMediaAlbum: (Album) -> Unit
) : ListAdapter<Album, MediaAlbumViewHolder>(
    object : DiffUtil.ItemCallback<Album>() {
        override fun areItemsTheSame(oldItem: Album, newItem: Album): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Album, newItem: Album): Boolean {
            return oldItem == newItem
        }
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaAlbumViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MediaAlbumViewHolder(ItemMediaAlbumBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: MediaAlbumViewHolder, position: Int) {
        val albumItem = getItem(position) ?: return

        holder.binding.executeAfter {
            album = albumItem
            root.setOnSingleClickListener { selectedMediaAlbum(albumItem) }
        }
    }

    override fun getItemViewType(position: Int) = R.layout.item_media_album
}