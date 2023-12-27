package com.connectcrew.presentation.screen.feature.mediapicker

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.connectcrew.presentation.R
import com.connectcrew.presentation.adapter.media.MediaAdapter
import com.connectcrew.presentation.adapter.media.MediaAlbumAdapter
import com.connectcrew.presentation.databinding.FragmentMediaPickerBinding
import com.connectcrew.presentation.screen.base.BaseFragment
import com.connectcrew.presentation.util.Const.KEY_SELECTED_MEDIA_PATHS
import com.connectcrew.presentation.util.launchAndRepeatWithViewLifecycle
import com.connectcrew.presentation.util.listener.setOnSingleClickListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MediaPickerFragment : BaseFragment<FragmentMediaPickerBinding>(R.layout.fragment_media_picker) {

    private lateinit var requestPermissionCallback: ActivityResultLauncher<Array<String>>

    private val mediaPickerViewModel: MediaPickerViewModel by viewModels()

    private val mediaAdapter by lazy {
        MediaAdapter(mediaPickerViewModel::setSelectedMediaItem)
    }

    private val mediaAlbumAdapter by lazy {
        MediaAlbumAdapter(mediaPickerViewModel::setSelectedAlbum)
    }

    private val permissionDialog by lazy {
        MaterialAlertDialogBuilder(requireContext())
            .setCancelable(false)
            .setTitle(resources.getString(R.string.media_picker_permission_storage))
            .setMessage(resources.getString(R.string.media_picker_permission_setting_storage_description))
            .setNegativeButton(resources.getString(R.string.common_close)) { dialog, _: Int ->
                dialog.dismiss()
                findNavController().navigateUp()
            }.setPositiveButton(resources.getString(R.string.common_confirm)) { dialog, _: Int ->
                val action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val data = Uri.parse("package:${requireActivity().applicationInfo.packageName}")
                val intent = Intent(action, data).apply {
                    addCategory(Intent.CATEGORY_DEFAULT)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                requireActivity().startActivity(intent)
                dialog.dismiss()
            }.show()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requestPermissionCallback = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { _ ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                dataBinding.llUploadPhotoPicker.isVisible = !isPermissionGranted(arrayOf(Manifest.permission.READ_MEDIA_IMAGES))
            }
            mediaPickerViewModel.initializeMediaAlbum(isPermissionGranted())
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataBinding.apply {
            viewModel = mediaPickerViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        initView()
        initListener()
        initObserver()
    }

    override fun onResume() {
        super.onResume()
        requestStoragePermission()
    }

    private fun initView() {
        with(dataBinding) {
            rvMediaAlbum.apply {
                adapter = mediaAlbumAdapter
                layoutManager = LinearLayoutManager(requireContext())
                itemAnimator = null
                setHasFixedSize(true)
            }

            rvMediaImage.apply {
                adapter = mediaAdapter
                layoutManager = GridLayoutManager(requireContext(), if (resources.getBoolean(R.bool.isTablet)) 5 else 3)
                itemAnimator = null
                setHasFixedSize(true)
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                llUploadPhotoPicker.isVisible = !isPermissionGranted(arrayOf(Manifest.permission.READ_MEDIA_IMAGES))
            }
        }
    }

    private fun initListener() {
        with(dataBinding) {
            tlMedia.apply {
                setNavigationOnClickListener {
                    if (!findNavController().navigateUp()) {
                        requireActivity()
                    }
                }

                setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.menu_confirm -> {
                            mediaPickerViewModel.navigateToBack()
                            true
                        }

                        else -> false
                    }
                }
            }

            btnUploadPhotoPicker.setOnSingleClickListener {
                requestPermissionCallback.launch(getStoragePermission())
            }
        }
    }

    private fun initObserver() {
        launchAndRepeatWithViewLifecycle {
            launch {
                mediaPickerViewModel.medias.collectLatest {
                    mediaAdapter.submitData(it)
                }
            }

            launch {
                mediaPickerViewModel.mediaAlbums.collect {
                    mediaAlbumAdapter.submitList(it)
                }
            }

            launch {
                mediaPickerViewModel.selectedMedias.collect {
                    dataBinding.tlMedia.menu.findItem(R.id.menu_confirm).isVisible = it.isNotEmpty()
                }
            }

            launch {
                mediaAdapter.loadStateFlow
                    .distinctUntilChangedBy { it.refresh }
                    .collect { loadState ->
                        val state = loadState.source.refresh
                        dataBinding.llMediaEmpty.isVisible = state is LoadState.NotLoading && mediaAdapter.itemCount == 0
                    }
            }

            launch {
                mediaPickerViewModel.navigateToBack.collect {
                    findNavController().run {
                        previousBackStackEntry?.savedStateHandle?.apply {
                            set(KEY_SELECTED_MEDIA_PATHS, it)
                            navigateUp()
                        }
                    }
                }
            }

            launch {
                mediaPickerViewModel.messageRes.collect {
                    showToast(resources.getString(it, mediaPickerViewModel.selectedMaxCount))
                }
            }
        }
    }

    private fun requestStoragePermission() {
        val isShowPermissionRequest = getStoragePermission().all { shouldShowRequestPermissionRationale(it) }
        val isNotShowingPermission = getStoragePermission().all { ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), it) }

        when {
            isPermissionGranted() -> mediaPickerViewModel.initializeMediaAlbum(true)
            !isShowPermissionRequest || !isNotShowingPermission -> requestPermissionCallback.launch(getStoragePermission())
            else -> showPermissionDeniedAlert()
        }
    }

    private fun showPermissionDeniedAlert() {
        if (permissionDialog.isShowing) return
        permissionDialog.show()
    }

    private fun isPermissionGranted(permissions: Array<String> = getStoragePermission()): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE && permissions.contentEquals(getStoragePermission())) {
            ContextCompat.checkSelfPermission(requireContext(), permissions[1]) == PackageManager.PERMISSION_GRANTED
        } else {
            getStoragePermission().all { ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED }
        }
    }

    override fun onDestroyView() {
        with(dataBinding) {
            rvMediaAlbum.adapter = null
            rvMediaImage.adapter = null
            permissionDialog.dismiss()
        }
        super.onDestroyView()
    }

    companion object {
        fun getStoragePermission() = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE -> arrayOf(Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED)
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
            else -> arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }
}