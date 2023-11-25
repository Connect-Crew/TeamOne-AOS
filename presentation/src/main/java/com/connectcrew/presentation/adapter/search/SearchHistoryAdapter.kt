package com.connectcrew.presentation.adapter.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.connectcrew.presentation.R
import com.connectcrew.presentation.databinding.ItemSearchHistoryBinding
import com.connectcrew.presentation.model.search.SearchHistoryItem
import com.connectcrew.presentation.util.executeAfter
import com.connectcrew.presentation.util.listener.setOnSingleClickListener

class SearchHistoryAdapter(
    private val onClickDeleteHistory: (String) -> Unit,
    private val onClickSearchHistory: (String) -> Unit
) : PagingDataAdapter<SearchHistoryItem, SearchHistoryViewHolder>(
    object : DiffUtil.ItemCallback<SearchHistoryItem>() {
        override fun areItemsTheSame(oldItem: SearchHistoryItem, newItem: SearchHistoryItem): Boolean {
            return oldItem.content == newItem.content
        }

        override fun areContentsTheSame(oldItem: SearchHistoryItem, newItem: SearchHistoryItem): Boolean {
            return oldItem == newItem
        }
    }
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHistoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return SearchHistoryViewHolder(ItemSearchHistoryBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: SearchHistoryViewHolder, position: Int) {
        val searchHistory = getItem(position) ?: return
        holder.binding.executeAfter {
            tvSearchHistory.text = searchHistory.content
            ivSearchHistoryDelete.setOnSingleClickListener { onClickDeleteHistory(searchHistory.content) }
            root.setOnSingleClickListener { onClickSearchHistory(searchHistory.content) }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_search_history
    }
}