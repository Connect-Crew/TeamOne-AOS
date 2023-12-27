package com.connectcrew.presentation.util.delegate

import com.connectcrew.domain.di.ApplicationScope
import com.connectcrew.presentation.model.project.ProjectFeed
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import javax.inject.Inject

interface ProjectFeedViewModelDelegate {

    val projectFeedUpdateActionFlow: SharedFlow<ProjectFeedUpdateActionFlow>

    fun createProjectFeedAction(projectFeed: ProjectFeed?, updateAt: ZonedDateTime)

    fun updateProjectFeedAction(projectFeed: ProjectFeed?, updateAt: ZonedDateTime)

    fun deleteProjectFeedAction(projectFeed: ProjectFeed?, updateAt: ZonedDateTime)

    fun invalidateProjectFeedAction(updateAt: ZonedDateTime)
}

sealed class ProjectFeedUpdateActionFlow(open val projectFeed: ProjectFeed?, open val updateAt: ZonedDateTime) {

    data object None : ProjectFeedUpdateActionFlow(null, ZonedDateTime.now())

    data class CreateProjectFeed(override val projectFeed: ProjectFeed?, override val updateAt: ZonedDateTime) : ProjectFeedUpdateActionFlow(projectFeed, updateAt)

    data class UpdateProjectFeed(override val projectFeed: ProjectFeed?, override val updateAt: ZonedDateTime) : ProjectFeedUpdateActionFlow(projectFeed, updateAt)

    data class DeleteProjectFeed(override val projectFeed: ProjectFeed?, override val updateAt: ZonedDateTime) : ProjectFeedUpdateActionFlow(projectFeed, updateAt)

    data class InvalidateProjectFeed(override val updateAt: ZonedDateTime) : ProjectFeedUpdateActionFlow(null, ZonedDateTime.now())
}

class ProjectFeedViewModelDelegateDelegateImpl @Inject constructor(
    @ApplicationScope private val applicationScope: CoroutineScope
) : ProjectFeedViewModelDelegate {

    private val _projectFeedUpdateActionFlow = MutableSharedFlow<ProjectFeedUpdateActionFlow>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_LATEST,
        replay = 3
    )

    override val projectFeedUpdateActionFlow: SharedFlow<ProjectFeedUpdateActionFlow> = _projectFeedUpdateActionFlow

    override fun createProjectFeedAction(projectFeed: ProjectFeed?, updateAt: ZonedDateTime) {
        applicationScope.launch {
            _projectFeedUpdateActionFlow.emit(ProjectFeedUpdateActionFlow.CreateProjectFeed(projectFeed, updateAt))
        }
    }

    override fun updateProjectFeedAction(projectFeed: ProjectFeed?, updateAt: ZonedDateTime) {
        applicationScope.launch {
            _projectFeedUpdateActionFlow.emit(ProjectFeedUpdateActionFlow.UpdateProjectFeed(projectFeed, updateAt))
        }
    }

    override fun deleteProjectFeedAction(projectFeed: ProjectFeed?, updateAt: ZonedDateTime) {
        applicationScope.launch {
            _projectFeedUpdateActionFlow.emit(ProjectFeedUpdateActionFlow.DeleteProjectFeed(projectFeed, updateAt))
        }
    }

    override fun invalidateProjectFeedAction(updateAt: ZonedDateTime) {
        applicationScope.launch {
            _projectFeedUpdateActionFlow.emit(ProjectFeedUpdateActionFlow.InvalidateProjectFeed(updateAt))
        }
    }
}