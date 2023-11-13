package com.connectcrew.presentation.adapter.home.category

import android.content.res.ColorStateList
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.connectcrew.presentation.R
import com.connectcrew.presentation.adapter.DataBindingViewHolder
import com.connectcrew.presentation.databinding.ItemProjectCategoryBinding
import com.connectcrew.presentation.model.project.ProjectCategoryItem
import com.connectcrew.presentation.model.project.ProjectCategoryType
import com.connectcrew.presentation.util.executeAfter
import com.connectcrew.presentation.util.listener.setOnSingleClickListener

class HomeCategoryViewHolder(
    val binding: ItemProjectCategoryBinding
) : DataBindingViewHolder<ItemProjectCategoryBinding>(binding) {

    fun bind(categoryItem: ProjectCategoryItem, onSelectedCategory: (ProjectCategoryItem) -> Unit) {
        binding.executeAfter {
            when (categoryItem.category) {
                ProjectCategoryType.CATEGORY_ALL -> {
                    if (categoryItem.isSelected) {
                        setCategoryColor(
                            binding = binding,
                            categoryBgColor = R.color.color_eeeeee,
                            categoryBgStrokeWidth = R.color.color_eeeeee,
                            categoryIconRes = R.drawable.ic_category_all,
                            categoryIconColor = R.color.color_424242,
                            categoryTextRes = R.string.category_all
                        )
                    } else {
                        setCategoryColor(
                            binding = binding,
                            categoryBgColor = android.R.color.white,
                            categoryBgStrokeWidth = R.color.color_eeeeee,
                            categoryIconRes = R.drawable.ic_category_all,
                            categoryIconColor = R.color.color_9e9e9e,
                            categoryTextRes = R.string.category_all
                        )
                    }
                }

                ProjectCategoryType.CATEGORY_DEVELOP -> {
                    if (categoryItem.isSelected) {
                        setCategoryColor(
                            binding = binding,
                            categoryBgColor = R.color.color_d8e0f3,
                            categoryBgStrokeWidth = R.color.color_d8e0f3,
                            categoryIconRes = R.drawable.ic_category_develop,
                            categoryIconColor = R.color.color_2e4f9e,
                            categoryTextRes = R.string.category_develop
                        )
                    } else {
                        setCategoryColor(
                            binding = binding,
                            categoryBgColor = android.R.color.white,
                            categoryBgStrokeWidth = R.color.color_eeeeee,
                            categoryIconRes = R.drawable.ic_category_develop,
                            categoryIconColor = R.color.color_9e9e9e,
                            categoryTextRes = R.string.category_develop
                        )
                    }
                }

                ProjectCategoryType.CATEGORY_PLAN -> {
                    if (categoryItem.isSelected) {
                        setCategoryColor(
                            binding = binding,
                            categoryBgColor = R.color.color_d7fff1,
                            categoryBgStrokeWidth = R.color.color_d7fff1,
                            categoryIconRes = R.drawable.ic_category_planner,
                            categoryIconColor = R.color.color_04ad78,
                            categoryTextRes = R.string.category_plan
                        )
                    } else {
                        setCategoryColor(
                            binding = binding,
                            categoryBgColor = android.R.color.white,
                            categoryBgStrokeWidth = R.color.color_eeeeee,
                            categoryIconRes = R.drawable.ic_category_planner,
                            categoryIconColor = R.color.color_9e9e9e,
                            categoryTextRes = R.string.category_plan
                        )
                    }
                }

                ProjectCategoryType.CATEGORY_DESIGN -> {
                    if (categoryItem.isSelected) {
                        setCategoryColor(
                            binding = binding,
                            categoryBgColor = R.color.color_ffe4e4,
                            categoryBgStrokeWidth = R.color.color_ffe4e4,
                            categoryIconRes = R.drawable.ic_category_designer,
                            categoryIconColor = R.color.color_ef4f4f,
                            categoryTextRes = R.string.category_design
                        )
                    } else {
                        setCategoryColor(
                            binding = binding,
                            categoryBgColor = android.R.color.white,
                            categoryBgStrokeWidth = R.color.color_eeeeee,
                            categoryIconRes = R.drawable.ic_category_designer,
                            categoryIconColor = R.color.color_9e9e9e,
                            categoryTextRes = R.string.category_design
                        )
                    }
                }

                ProjectCategoryType.CATEGORY_MARKETING -> {
                    if (categoryItem.isSelected) {
                        setCategoryColor(
                            binding = binding,
                            categoryBgColor = R.color.color_fefce1,
                            categoryBgStrokeWidth = R.color.color_fefce1,
                            categoryIconRes = R.drawable.ic_category_marketing,
                            categoryIconColor = R.color.color_ecbb10,
                            categoryTextRes = R.string.category_marketing
                        )
                    } else {
                        setCategoryColor(
                            binding = binding,
                            categoryBgColor = android.R.color.white,
                            categoryBgStrokeWidth = R.color.color_eeeeee,
                            categoryIconRes = R.drawable.ic_category_marketing,
                            categoryIconColor = R.color.color_9e9e9e,
                            categoryTextRes = R.string.category_marketing
                        )
                    }
                }

                ProjectCategoryType.CATEGORY_SALES -> {
                    if (categoryItem.isSelected) {
                        setCategoryColor(
                            binding = binding,
                            categoryBgColor = R.color.color_ddedff,
                            categoryBgStrokeWidth = R.color.color_ddedff,
                            categoryIconRes = R.drawable.ic_category_sales,
                            categoryIconColor = R.color.color_007aff,
                            categoryTextRes = R.string.category_sales
                        )
                    } else {
                        setCategoryColor(
                            binding = binding,
                            categoryBgColor = android.R.color.white,
                            categoryBgStrokeWidth = R.color.color_eeeeee,
                            categoryIconRes = R.drawable.ic_category_sales,
                            categoryIconColor = R.color.color_9e9e9e,
                            categoryTextRes = R.string.category_sales
                        )
                    }
                }

                ProjectCategoryType.CATEGORY_CS -> {
                    if (categoryItem.isSelected) {
                        setCategoryColor(
                            binding = binding,
                            categoryBgColor = R.color.color_fff3e0,
                            categoryBgStrokeWidth = R.color.color_fff3e0,
                            categoryIconRes = R.drawable.ic_category_cs,
                            categoryIconColor = R.color.color_936013,
                            categoryTextRes = R.string.category_cs
                        )
                    } else {
                        setCategoryColor(
                            binding = binding,
                            categoryBgColor = android.R.color.white,
                            categoryBgStrokeWidth = R.color.color_eeeeee,
                            categoryIconRes = R.drawable.ic_category_cs,
                            categoryIconColor = R.color.color_9e9e9e,
                            categoryTextRes = R.string.category_cs
                        )
                    }
                }

                ProjectCategoryType.CATEGORY_PROFESSIONAL -> {
                    if (categoryItem.isSelected) {
                        setCategoryColor(
                            binding = binding,
                            categoryBgColor = R.color.color_ffddf8,
                            categoryBgStrokeWidth = R.color.color_ffddf8,
                            categoryIconRes = R.drawable.ic_category_professional,
                            categoryIconColor = R.color.color_ff00c7,
                            categoryTextRes = R.string.category_professional
                        )
                    } else {
                        setCategoryColor(
                            binding = binding,
                            categoryBgColor = android.R.color.white,
                            categoryBgStrokeWidth = R.color.color_eeeeee,
                            categoryIconRes = R.drawable.ic_category_professional,
                            categoryIconColor = R.color.color_9e9e9e,
                            categoryTextRes = R.string.category_professional
                        )
                    }
                }

                ProjectCategoryType.CATEGORY_ENGINEERING -> {
                    if (categoryItem.isSelected) {
                        setCategoryColor(
                            binding = binding,
                            categoryBgColor = R.color.color_ffebdd,
                            categoryBgStrokeWidth = R.color.color_ffebdd,
                            categoryIconRes = R.drawable.ic_category_engineering,
                            categoryIconColor = R.color.color_ff6b00,
                            categoryTextRes = R.string.category_engineering
                        )
                    } else {
                        setCategoryColor(
                            binding = binding,
                            categoryBgColor = android.R.color.white,
                            categoryBgStrokeWidth = R.color.color_eeeeee,
                            categoryIconRes = R.drawable.ic_category_engineering,
                            categoryIconColor = R.color.color_9e9e9e,
                            categoryTextRes = R.string.category_engineering
                        )
                    }
                }

                ProjectCategoryType.CATEGORY_MEDIA -> {
                    if (categoryItem.isSelected) {
                        setCategoryColor(
                            binding = binding,
                            categoryBgColor = R.color.color_ede2fc,
                            categoryBgStrokeWidth = R.color.color_ede2fc,
                            categoryIconRes = R.drawable.ic_category_media,
                            categoryIconColor = R.color.color_7416eb,
                            categoryTextRes = R.string.category_media
                        )
                    } else {
                        setCategoryColor(
                            binding = binding,
                            categoryBgColor = android.R.color.white,
                            categoryBgStrokeWidth = R.color.color_eeeeee,
                            categoryIconRes = R.drawable.ic_category_media,
                            categoryIconColor = R.color.color_9e9e9e,
                            categoryTextRes = R.string.category_media
                        )
                    }
                }

                ProjectCategoryType.CATEGORY_ETC -> {
                    if (categoryItem.isSelected) {
                        setCategoryColor(
                            binding = binding,
                            categoryBgColor = R.color.color_eeeeee,
                            categoryBgStrokeWidth = R.color.color_eeeeee,
                            categoryIconRes = R.drawable.ic_category_etc,
                            categoryIconColor = R.color.color_424242,
                            categoryTextRes = R.string.category_etc
                        )
                    } else {
                        setCategoryColor(
                            binding = binding,
                            categoryBgColor = android.R.color.white,
                            categoryBgStrokeWidth = R.color.color_eeeeee,
                            categoryIconRes = R.drawable.ic_category_etc,
                            categoryIconColor = R.color.color_9e9e9e,
                            categoryTextRes = R.string.category_etc
                        )
                    }
                }
            }

            root.setOnSingleClickListener {
                onSelectedCategory.invoke(categoryItem)
            }
        }
    }

    private fun setCategoryColor(
        binding: ItemProjectCategoryBinding,
        @ColorRes categoryBgColor: Int,
        @ColorRes categoryBgStrokeWidth: Int,
        @DrawableRes categoryIconRes: Int,
        @ColorRes categoryIconColor: Int,
        @StringRes categoryTextRes: Int
    ) {
        binding.executeAfter {
            ivProjectCategoryBg.apply {
                setBackgroundColor(ContextCompat.getColor(root.context, categoryBgColor))
                strokeColor = ColorStateList.valueOf(ContextCompat.getColor(root.context, categoryBgStrokeWidth))
            }

            ivProjectCategory.apply {
                imageTintList = ColorStateList.valueOf(ContextCompat.getColor(root.context, categoryIconColor))
                setImageResource(categoryIconRes)
            }

            tvProjectCategory.apply {
                text = binding.root.resources.getString(categoryTextRes)
                setTextColor(ContextCompat.getColor(root.context, categoryIconColor))
            }
        }
    }
}