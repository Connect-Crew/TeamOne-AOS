package com.connectcrew.presentation.adapter.media

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.connectcrew.presentation.R
import com.connectcrew.presentation.databinding.ItemMediaBinding
import com.connectcrew.presentation.model.media.Media
import com.connectcrew.presentation.util.executeAfter
import com.connectcrew.presentation.util.listener.setOnSingleClickListener

class MediaAdapter(
    private val selectedMediaItem: (Media) -> Unit
) : PagingDataAdapter<Media, MediaViewHolder>(
    object : DiffUtil.ItemCallback<Media>() {
        override fun areItemsTheSame(oldItem: Media, newItem: Media): Boolean {
            return oldItem.uri == newItem.uri
        }

        override fun areContentsTheSame(oldItem: Media, newItem: Media): Boolean {
            return oldItem == newItem
        }
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MediaViewHolder(ItemMediaBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        val mediaItem = getItem(position) ?: return
        holder.binding.executeAfter {
            media = mediaItem
            root.setOnSingleClickListener { selectedMediaItem(mediaItem) }
        }
    }

    override fun getItemViewType(position: Int) = R.layout.item_media
}