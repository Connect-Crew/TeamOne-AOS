package com.connectcrew.presentation.screen.feature.mediapicker

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.connectcrew.domain.usecase.media.GetMediaAlbumUseCase
import com.connectcrew.domain.usecase.media.GetMediaImagesUseCase
import com.connectcrew.domain.usecase.media.entity.AlbumEntity
import com.connectcrew.domain.usecase.media.entity.MediaEntity
import com.connectcrew.domain.util.data
import com.connectcrew.presentation.R
import com.connectcrew.presentation.model.media.Album
import com.connectcrew.presentation.model.media.Media
import com.connectcrew.presentation.model.media.asItem
import com.connectcrew.presentation.screen.base.BaseViewModel
import com.connectcrew.presentation.util.event.EventFlow
import com.connectcrew.presentation.util.event.MutableEventFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MediaPickerViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getMediaImagesUseCase: GetMediaImagesUseCase,
    private val getMediaAlbumUseCase: GetMediaAlbumUseCase
) : BaseViewModel() {

    val selectedMedias = savedStateHandle.getStateFlow<List<Media>>(KEY_SELECTED_IMAGE_PATHS, emptyList())
    val selectedMaxCount
        get() = savedStateHandle.get<Int>(KEY_MAX_COUNT) ?: 0

    private val selectedAlbum = MutableStateFlow<Album?>(null)

    private val _mediaAlbums = MutableStateFlow<List<Album>>(emptyList())
    val mediaAlbums: StateFlow<List<Album>> = _mediaAlbums
        .combine(selectedAlbum) { albums, selectedAlbum -> albums.map { it.copy(isSelected = it == selectedAlbum) } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val medias = selectedAlbum
        .filterNotNull()
        .flatMapLatest { getMediaImagesUseCase(it.name) }
        .map { it.map(MediaEntity::asItem) }
        .cachedIn(viewModelScope)
        .combine(selectedMedias) { mediaData: PagingData<Media>, selectedPaths: List<Media> ->
            mediaData.map { media ->
                selectedPaths.indexOf(media).let {
                    media.copy(selectedPosition = if (it == NOT_FOUND_PATH_INDEX) null else it.plus(1))
                }
            }
        }

    private val _navigateToBack = MutableEventFlow<List<String>>()
    val navigateToBack: EventFlow<List<String>> = _navigateToBack

    fun initializeMediaAlbum(isApprove: Boolean) {
        if (!isApprove) return

        viewModelScope.launch {
            val albums = getMediaAlbumUseCase(TOTAL_ALBUM_NAME).data?.map(AlbumEntity::asItem) ?: emptyList()
            _mediaAlbums.value = albums
            setSelectedAlbum(albums.firstOrNull() ?: Album(name = TOTAL_ALBUM_NAME, "", 0))
        }
    }

    fun setSelectedAlbum(selectedAlbum: Album) {
        this.selectedAlbum.update { selectedAlbum }
    }

    fun setSelectedMediaItem(media: Media) {
        savedStateHandle.set(
            KEY_SELECTED_IMAGE_PATHS,
            selectedMedias.value
                .toMutableList()
                .apply {
                    if (any { it.uri == media.uri }) {
                        removeAll { it.uri == media.uri }
                    } else {
                        if (size >= selectedMaxCount) {
                            setMessage(R.string.media_picker_max_selected_description)
                            return@apply
                        } else {
                            add(media)
                        }
                    }
                }
        )
    }

    fun navigateToBack() {
        viewModelScope.launch {
            _navigateToBack.emit(selectedMedias.value.map { it.uri })
        }
    }

    companion object {
        private const val KEY_MAX_COUNT = "max_select_count"
        private const val KEY_SELECTED_IMAGE_PATHS = "select_image_paths"
        private const val TOTAL_ALBUM_NAME = "ALL"
        private const val NOT_FOUND_PATH_INDEX = -1
    }
}