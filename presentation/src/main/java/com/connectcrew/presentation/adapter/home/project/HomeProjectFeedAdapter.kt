package com.connectcrew.presentation.adapter.home.project

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.connectcrew.presentation.R
import com.connectcrew.presentation.databinding.ItemProjectFeedBinding
import com.connectcrew.presentation.model.project.ProjectFeed
import com.connectcrew.presentation.util.TimeUtil
import com.connectcrew.presentation.util.executeAfter
import com.connectcrew.presentation.util.listener.setOnSingleClickListener
import com.connectcrew.presentation.util.loadImage
import com.google.android.material.chip.Chip
import java.text.DecimalFormat

class HomeProjectFeedAdapter(
    private val onClickFavoriteProjectFeed: (ProjectFeed) -> Unit,
    private val onClickMemberCount: (ProjectFeed) -> Unit,
    private val onClickProjectFeed: (ProjectFeed) -> Unit,
) : PagingDataAdapter<ProjectFeed, HomeProjectFeedViewHolder>(
    object : DiffUtil.ItemCallback<ProjectFeed>() {
        override fun areItemsTheSame(oldItem: ProjectFeed, newItem: ProjectFeed): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProjectFeed, newItem: ProjectFeed): Boolean {
            return oldItem == newItem
        }

        override fun getChangePayload(oldItem: ProjectFeed, newItem: ProjectFeed): Any? {
            return if (oldItem.id == newItem.id) {
                PAYLOAD_PROJECT
            } else {
                super.getChangePayload(oldItem, newItem)
            }
        }
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeProjectFeedViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return HomeProjectFeedViewHolder(ItemProjectFeedBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: HomeProjectFeedViewHolder, position: Int) {
        val projectFeed = getItem(position) ?: return
        setOnBindViewHolder(holder , projectFeed)
    }

    override fun onBindViewHolder(holder: HomeProjectFeedViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.contains(PAYLOAD_PROJECT)) {
            val projectFeed = getItem(position) ?: return
            setOnBindViewHolder(holder , projectFeed)
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setOnBindViewHolder(holder: HomeProjectFeedViewHolder, projectFeed: ProjectFeed) {
        holder.binding.executeAfter {
            tvProjectTitle.text = projectFeed.title
            tvProjectLocation.text = if (projectFeed.isOnline) root.context.resources.getString(R.string.common_online) else projectFeed.region
            tvProjectCreatedAt.text = TimeUtil.getDateTimeFormatStringForTimeDifference(
                dateTime = projectFeed.createdAt,
                context = tvProjectCreatedAt.context
            )
            val recruitmentColor = if (projectFeed.isEnroll) R.color.color_00aee4 else R.color.color_9e9e9e
            ivMemberCount.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(ivMemberCount.context, recruitmentColor))
            tvMemberCount.apply {
                text = "${projectFeed.totalCurrentCount} / ${projectFeed.totalMaxCount}"
                setTextColor(ContextCompat.getColor(tvMemberCount.context, recruitmentColor))
            }
            llMemberCount.setOnSingleClickListener { onClickMemberCount(projectFeed) }

            tvLikeCount.text = DecimalFormat(root.context.resources.getString(R.string.decimal_format_not_digit)).format(projectFeed.likeCount)
            ivLikeCount.isSelected = projectFeed.isLike
            llLikeCount.setOnSingleClickListener { onClickFavoriteProjectFeed(projectFeed) }
            root.setOnSingleClickListener { onClickProjectFeed(projectFeed) }

            projectFeed.thumbnail
                ?.let { loadImage(ivProjectThumbnail, it) }
                ?: run { ivProjectThumbnail.setImageResource(R.drawable.ic_team_one_logo) }

            (listOf(projectFeed.state, projectFeed.careerMin) + projectFeed.category).mapIndexed { index, item ->
                if (cgProjectTag.children.none { it.tag == item }) {
                    val chip = Chip(cgProjectTag.context, null, R.attr.ProjectFeedChipStyle).apply {
                        tag = item
                        text = item
                        setChipBackgroundColorResource(if (index < 2) R.color.color_f7cdd5 else R.color.color_eeeeee)
                    }
                    cgProjectTag.addView(chip)
                } else {
                    return@executeAfter
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int = R.layout.item_project_feed

    companion object {
        private const val PAYLOAD_PROJECT = "PAYLOAD_PROJECT"
    }
}