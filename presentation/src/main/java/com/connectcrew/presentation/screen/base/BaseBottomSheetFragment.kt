package com.connectcrew.presentation.screen.base

import android.app.Dialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDialog
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.connectcrew.presentation.R
import com.connectcrew.presentation.databinding.DialogLoadingBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.google.android.material.R as materialR

abstract class BaseBottomSheetFragment<T : ViewDataBinding> : BottomSheetDialogFragment() {

    abstract val layoutResId: Int

    private var _dataBinding: T? = null
    protected val dataBinding: T
        get() = _dataBinding!!

    private val loadingDialog: AppCompatDialog by lazy {
        DialogLoadingBinding.inflate(layoutInflater, null, false)
            .run {
                AppCompatDialog(requireContext())
                    .apply {
                        setCancelable(false)
                        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        setContentView(this@run.root)
                    }
            }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog: Dialog = super.onCreateDialog(savedInstanceState)

        if (dialog is BottomSheetDialog) {
            dialog.setOnShowListener {
                val bottomSheetView = dialog.findViewById<View>(materialR.id.design_bottom_sheet)

                bottomSheetView?.post {
                    dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED

                    val newMaterialShapeDrawable: MaterialShapeDrawable = createMaterialShapeDrawable(bottomSheetView)
                    ViewCompat.setBackground(bottomSheetView, newMaterialShapeDrawable)
                }
            }
        }

        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _dataBinding = DataBindingUtil.inflate(
            inflater,
            layoutResId,
            container,
            false
        )
        return dataBinding.root
    }

    @ColorRes
    protected open val backgroundColor: Int = R.color.color_fdfdfd

    private fun createMaterialShapeDrawable(bottomSheet: View): MaterialShapeDrawable {
        val shapeAppearanceModel = ShapeAppearanceModel.builder(context, 0, R.style.ShapeAppearance_TeamOne_MediumComponent).build()

        //Create a new MaterialShapeDrawable (you can't use the original MaterialShapeDrawable in the BottomSheet)
        val currentMaterialShapeDrawable = bottomSheet.background as? MaterialShapeDrawable ?: MaterialShapeDrawable()
        val newMaterialShapeDrawable = MaterialShapeDrawable(shapeAppearanceModel)
        //Copy the attributes in the new MaterialShapeDrawable
        newMaterialShapeDrawable.initializeElevationOverlay(context)
        newMaterialShapeDrawable.fillColor = backgroundColor?.let { ColorStateList.valueOf(ContextCompat.getColor(requireContext(), it)) } ?: currentMaterialShapeDrawable.fillColor
        newMaterialShapeDrawable.tintList = currentMaterialShapeDrawable.tintList
        newMaterialShapeDrawable.elevation = currentMaterialShapeDrawable.elevation
        newMaterialShapeDrawable.strokeWidth = currentMaterialShapeDrawable.strokeWidth
        newMaterialShapeDrawable.strokeColor = currentMaterialShapeDrawable.strokeColor
        return newMaterialShapeDrawable
    }

    fun showLoadingDialog() {
        if (!loadingDialog.isShowing) {
            loadingDialog.show()
        }
    }

    fun hideLoadingDialog() {
        if (loadingDialog.isShowing) {
            loadingDialog.dismiss()
        }
    }

    fun showToast(@StringRes id: Int) {
        Toast.makeText(requireContext(), id, Toast.LENGTH_SHORT).show()
    }

    fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _dataBinding = null
    }
}