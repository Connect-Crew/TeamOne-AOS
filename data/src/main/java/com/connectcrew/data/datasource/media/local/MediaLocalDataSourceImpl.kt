package com.connectcrew.data.datasource.media.local

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.content.ContentResolverCompat
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.connectcrew.data.model.media.Album
import com.connectcrew.data.model.media.Media
import com.connectcrew.data.model.media.asEntity
import com.connectcrew.domain.usecase.media.entity.AlbumEntity
import com.connectcrew.domain.usecase.media.entity.MediaEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import java.io.File
import javax.inject.Inject

internal class MediaLocalDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : MediaLocalDataSource {

    private val cursorProjection: Array<String> = arrayOf(
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.DATA,
        MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
        MediaStore.Images.Media.DATE_ADDED,
        MediaStore.Images.Media.SIZE,
    )
    private val cursorUri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    private val cursorSortOrder: String = String.format("%s %s", MediaStore.Images.Media.DATE_MODIFIED, "DESC")
    private val cursorSelection: String = MediaStore.Images.Media.SIZE + " > ? "
    private val cursorSelectionArgs: Array<String> = arrayOf("0")

    override fun getMediaImages(albumName: String?): Flow<PagingData<MediaEntity>> = Pager(
        config = PagingConfig(pageSize = MEDIA_PAGE_SIZE, enablePlaceholders = false)
    ) {
        object : PagingSource<Int, MediaEntity>() {
            override fun getRefreshKey(state: PagingState<Int, MediaEntity>): Int? = null

            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MediaEntity> {
                val offset = params.key ?: 0

                return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                    context.contentResolver.query(
                        cursorUri,
                        cursorProjection,
                        Bundle().apply {
                            // Sort function
                            putStringArray(
                                ContentResolver.QUERY_ARG_SORT_COLUMNS,
                                arrayOf(MediaStore.Images.Media.DATE_MODIFIED)
                            )
                            putInt(
                                ContentResolver.QUERY_ARG_SORT_DIRECTION,
                                ContentResolver.QUERY_SORT_DIRECTION_DESCENDING
                            )
                            // Limit & Offset
                            putInt(ContentResolver.QUERY_ARG_LIMIT, params.loadSize)
                            putInt(ContentResolver.QUERY_ARG_OFFSET, offset)

                            // Selection
                            putString(
                                ContentResolver.QUERY_ARG_SQL_SELECTION,
                                if (!albumName.isNullOrEmpty() && albumName != TOTAL_ALBUM_NAME) {
                                    cursorSelection.plus(" AND ${MediaStore.Images.Media.BUCKET_DISPLAY_NAME} = ?")
                                } else {
                                    cursorSelection
                                }
                            )
                            putStringArray(
                                ContentResolver.QUERY_ARG_SQL_SELECTION_ARGS,
                                if (!albumName.isNullOrEmpty() && albumName != TOTAL_ALBUM_NAME) {
                                    cursorSelectionArgs.plus(albumName)
                                } else {
                                    cursorSelectionArgs
                                }
                            )
                        },
                        null
                    )
                } else {
                    ContentResolverCompat.query(
                        context.contentResolver,
                        cursorUri,
                        cursorProjection,
                        if (!albumName.isNullOrEmpty() && albumName != TOTAL_ALBUM_NAME) cursorSelection.plus(" AND ${MediaStore.Images.Media.BUCKET_DISPLAY_NAME} = ?") else cursorSelection,
                        if (!albumName.isNullOrEmpty() && albumName != TOTAL_ALBUM_NAME) cursorSelectionArgs.plus(albumName) else cursorSelectionArgs,
                        cursorSortOrder + " LIMIT ${params.loadSize} OFFSET $offset",
                        null
                    )
                }?.use { cursor ->
                    val medias = generateSequence { if (cursor.moveToNext()) cursor else null }
                        .map {
                            it.run {
                                val folderName: String? = getString(getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME))
                                val mediaUri: Uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, getLong(getColumnIndexOrThrow(MediaStore.Images.Media._ID)))
                                val dateTimeMills = getLong(getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)).times(1000)
                                Media(folderName, mediaUri.toString(), dateTimeMills)
                            }
                        }
                        .toList()

                    LoadResult.Page(
                        data = medias.map(Media::asEntity),
                        prevKey = null,
                        nextKey = if (medias.size == params.loadSize) offset.plus(params.loadSize) else null
                    )
                } ?: LoadResult.Error(NullPointerException())
            }
        }
    }.flow

    override suspend fun getAlbums(albumName: String?): List<AlbumEntity> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            context.contentResolver.query(
                cursorUri,
                cursorProjection,
                Bundle().apply {
                    // Sort function
                    putStringArray(
                        ContentResolver.QUERY_ARG_SORT_COLUMNS,
                        arrayOf(MediaStore.Images.Media.DATE_MODIFIED)
                    )
                    putInt(
                        ContentResolver.QUERY_ARG_SORT_DIRECTION,
                        ContentResolver.QUERY_SORT_DIRECTION_DESCENDING
                    )
                    // Selection
                    putString(
                        ContentResolver.QUERY_ARG_SQL_SELECTION,
                        cursorSelection
                    )
                    putStringArray(
                        ContentResolver.QUERY_ARG_SQL_SELECTION_ARGS,
                        cursorSelectionArgs
                    )
                },
                null
            )
        } else {
            ContentResolverCompat.query(
                context.contentResolver,
                cursorUri,
                cursorProjection,
                cursorSelection,
                cursorSelectionArgs,
                cursorSortOrder,
                null
            )
        }?.use { cursor ->
            val albums = generateSequence { if (cursor.moveToNext()) cursor else null }
                .map {
                    it.run {
                        val folderName: String? = getString(getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME))
                        val mediaUri: Uri = getMediaUri()
                        val dateTimeMills = getLong(getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)).times(1000)

                        Media(folderName, mediaUri.toString(), dateTimeMills)
                    }
                }
                .groupBy { media: Media -> media.albumName ?: "Unknown" }
                .toSortedMap { albumName1: String, albumName2: String ->
                    if (albumName2 == "Camera") {
                        1
                    } else {
                        albumName1.compareTo(albumName2, true)
                    }
                }
                .map { entry ->
                    Album(
                        entry.key,
                        entry.value[0].uri,
                        entry.value.size
                    )
                }
            val totalAlbum = Album(
                TOTAL_ALBUM_NAME,
                albums.getOrNull(0)?.thumbnailUri.toString(),
                albums.sumOf { it.mediaSize }
            )
            (listOf(totalAlbum) + albums).map(Album::asEntity)
        } ?: emptyList()
    }

    private fun Cursor.getMediaUri(): Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val id = getLong(getColumnIndexOrThrow(MediaStore.Images.Media._ID))
        ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
    } else {
        val mediaPath = getString(getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
        Uri.fromFile(File(mediaPath))
    }

    companion object {
        private const val TOTAL_ALBUM_NAME = "ALL"
        private const val MEDIA_PAGE_SIZE = 50
    }
}