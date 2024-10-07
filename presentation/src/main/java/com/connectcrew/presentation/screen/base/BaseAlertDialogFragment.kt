package com.connectcrew.presentation.screen.base

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDialog
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.connectcrew.presentation.databinding.DialogLoadingBinding
import com.connectcrew.presentation.util.MessageType
import com.google.android.material.dialog.MaterialAlertDialogBuilder

abstract class BaseAlertDialogFragment<T : ViewDataBinding> : DialogFragment() {

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
        _dataBinding = DataBindingUtil.inflate(layoutInflater, layoutResId, null, false)

        return MaterialAlertDialogBuilder(requireContext())
            .setView(dataBinding.root)
            .create()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return dataBinding.root
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

    fun showToast(toastType: MessageType) {
        when (toastType) {
            is MessageType.ResourceType -> showToast(toastType.message)
            is MessageType.ValueType -> showToast(toastType.message)
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