package com.connectcrew.presentation.adapter.home.category

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.connectcrew.presentation.adapter.DataBindingViewHolder
import com.connectcrew.presentation.databinding.ItemProjectCategoryBinding
import com.connectcrew.presentation.util.executeAfter

class HomeCategoryViewHolder(
    val binding: ItemProjectCategoryBinding
) : DataBindingViewHolder<ItemProjectCategoryBinding>(binding) {

    fun bind(@DrawableRes categoryImageRes: Int, @StringRes categoryTextRes: Int) {
        binding.executeAfter {
            ivProjectCategory.setImageResource(categoryImageRes)
            tvProjectCategory.text = tvProjectCategory.context.getText(categoryTextRes)
        }
    }
}