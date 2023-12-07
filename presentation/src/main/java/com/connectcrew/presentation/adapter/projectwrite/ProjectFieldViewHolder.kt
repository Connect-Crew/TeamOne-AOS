package com.connectcrew.presentation.adapter.projectwrite

import android.content.res.ColorStateList
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.connectcrew.presentation.R
import com.connectcrew.presentation.adapter.DataBindingViewHolder
import com.connectcrew.presentation.databinding.ItemProjectWriteFieldBinding
import com.connectcrew.presentation.model.project.ProjectFieldInfo
import com.connectcrew.presentation.screen.feature.projectwrite.ProjectWriteFieldType
import com.connectcrew.presentation.util.executeAfter
import com.connectcrew.presentation.util.isNull
import com.connectcrew.presentation.util.listener.setOnSingleClickListener

class ProjectFieldViewHolder(
    val binding: ItemProjectWriteFieldBinding
) : DataBindingViewHolder<ItemProjectWriteFieldBinding>(binding) {

    fun bind(projectInfo: ProjectFieldInfo, selectedProjectField: (ProjectFieldInfo) -> Unit) {
        binding.executeAfter {
            if (!projectInfo.categoryIcon.isNull()) {
                ivProjectWriteField.setImageResource(projectInfo.categoryIcon!!)
                ivProjectWriteField.isVisible = true
            } else {
                ivProjectWriteField.isVisible = false
            }

            when (projectInfo.category) {
                ProjectWriteFieldType.TYPE_IT -> R.color.color_e8f4ff to R.color.color_014b92
                ProjectWriteFieldType.TYPE_APP -> R.color.color_fbdaf5 to R.color.color_850d6f
                ProjectWriteFieldType.TYPE_TRAVEL -> R.color.color_ebf6e2 to R.color.color_50ab0a
                ProjectWriteFieldType.TYPE_ECOMMERCE -> R.color.color_fffadd to R.color.color_a88d00
                ProjectWriteFieldType.TYPE_COMMUNITY -> R.color.color_e5e4f0 to R.color.color_514e87
                ProjectWriteFieldType.TYPE_EDUCATION -> R.color.color_ede0ff to R.color.color_7719f1
                ProjectWriteFieldType.TYPE_HEALTH_LIFE -> R.color.color_e7fef1 to R.color.color_046b33
                ProjectWriteFieldType.TYPE_BABY_PET -> R.color.color_f2ece5 to R.color.color_705335
                ProjectWriteFieldType.TYPE_LOVE -> R.color.color_fde6ec to R.color.color_d31145
                ProjectWriteFieldType.TYPE_GAME -> R.color.color_e7ffff to R.color.color_008081
                ProjectWriteFieldType.TYPE_FOOD -> R.color.color_ffe9cf to R.color.color_f18500
                ProjectWriteFieldType.TYPE_FINANCE -> R.color.color_ddedff to R.color.color_006fe9
                ProjectWriteFieldType.TYPE_HOUSE -> R.color.color_fdecf4 to R.color.color_f04f9b
                ProjectWriteFieldType.TYPE_AI -> R.color.color_e2e6f5 to R.color.color_26356e
                ProjectWriteFieldType.TYPE_ETC -> R.color.color_f2f2f2 to R.color.color_616161
            }.let { (bgColor, strokeColor) ->
                setProjectFiledColor(binding, projectInfo.category.value, projectInfo.isSelected, bgColor, strokeColor)
            }

            root.setOnSingleClickListener {
                selectedProjectField.invoke(projectInfo)
            }
        }
    }

    private fun setProjectFiledColor(
        binding: ItemProjectWriteFieldBinding,
        fieldText: String,
        isSelected: Boolean,
        @ColorRes fieldBgColor: Int,
        @ColorRes fieldBgStrokeColor: Int,
    ) {
        with(binding) {
            sivProjectWriteFiledBg.apply {
                strokeColor = if (isSelected) {
                    setBackgroundColor(ContextCompat.getColor(context, fieldBgColor))
                    ColorStateList.valueOf(ContextCompat.getColor(root.context, fieldBgStrokeColor))
                } else {
                    setBackgroundColor(ContextCompat.getColor(context, android.R.color.white))
                    ColorStateList.valueOf(ContextCompat.getColor(root.context, R.color.color_eeeeee))
                }
            }
            tvProjectWriteField.apply {
                text = fieldText
                setTextColor(ContextCompat.getColor(context, if (isSelected) fieldBgStrokeColor else R.color.color_616161))
            }
        }
    }
}