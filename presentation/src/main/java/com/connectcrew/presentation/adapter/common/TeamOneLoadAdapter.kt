package com.connectcrew.presentation.adapter.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.connectcrew.presentation.databinding.ItemTeamOneLoadBinding
import com.connectcrew.presentation.util.executeAfter
import com.connectcrew.presentation.util.listener.setOnSingleClickListener

class TeamOneLoadAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<TeamOneLoadViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): TeamOneLoadViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return TeamOneLoadViewHolder(ItemTeamOneLoadBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: TeamOneLoadViewHolder, loadState: LoadState) {
        holder.binding.executeAfter {
            isLoading = loadState is LoadState.Loading
            viewNetworkError.btnNetworkError.setOnSingleClickListener {
                retry.invoke()
            }
        }
    }
}