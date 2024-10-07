package com.connectcrew.presentation.adapter.menuspinner

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import com.connectcrew.presentation.R
import com.connectcrew.presentation.databinding.ItemMenuSpinnerBinding
import com.connectcrew.presentation.util.executeAfter
import com.connectcrew.presentation.util.listener.setOnSingleClickListener

class MenuSpinnerAdapter(
    context: Context,
    private val menuItems: List<String>,
    private val selectedMenuItem: (String) -> Unit
) : ArrayAdapter<String>(context, R.layout.item_menu_spinner, menuItems) {

    override fun getCount() = menuItems.size

    override fun getItem(position: Int) = menuItems[position]

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMenuSpinnerBinding.inflate(inflater, parent, false)

        with(binding) {
            tvMenuItem.isVisible = false
            ivMoreInfo.isVisible = true
        }
        return binding.root
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMenuSpinnerBinding.inflate(inflater, parent, false)
        binding.executeAfter {
            ivMoreInfo.isVisible = false
            tvMenuItem.apply {
                isVisible = true
                text = menuItems[position]
            }

            root.setOnSingleClickListener { selectedMenuItem(menuItems[position]) }
        }

        return binding.root
    }

    override fun getItemViewType(position: Int) = R.layout.item_menu_spinner
}