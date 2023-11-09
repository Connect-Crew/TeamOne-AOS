package com.connectcrew.presentation.adapter

import androidx.core.view.doOnAttach
import androidx.core.view.doOnDetach
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.recyclerview.widget.RecyclerView

open class DataBindingViewHolder<T : ViewDataBinding>(binding: T) : RecyclerView.ViewHolder(binding.root) {

    protected var lifecycleOwner: LifecycleOwner? = null

    init {
        itemView.doOnAttach {
            lifecycleOwner = itemView.findViewTreeLifecycleOwner()
            binding.lifecycleOwner = lifecycleOwner
        }

        itemView.doOnDetach {
            lifecycleOwner = null
        }
    }
}