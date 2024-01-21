package com.connectcrew.presentation.screen.feature.projectwrite

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.connectcrew.domain.usecase.project.CreateProjectFeedUseCase
import com.connectcrew.domain.usecase.project.GetProjectInfoUseCase
import com.connectcrew.domain.usecase.project.UpdateProjectFeedUseCase
import com.connectcrew.domain.util.ApiResult
import com.connectcrew.domain.util.TeamOneException
import com.connectcrew.domain.util.asResult
import com.connectcrew.domain.util.data
import com.connectcrew.domain.util.succeeded
import com.connectcrew.presentation.R
import com.connectcrew.presentation.model.project.ProjectFeedDetail
import com.connectcrew.presentation.model.project.ProjectFieldInfo
import com.connectcrew.presentation.model.project.ProjectInfo
import com.connectcrew.presentation.model.project.ProjectInfoContainer
import com.connectcrew.presentation.model.project.ProjectJobInfo
import com.connectcrew.presentation.model.project.ProjectJobUiModel
import com.connectcrew.presentation.model.project.asEntity
import com.connectcrew.presentation.model.project.asItem
import com.connectcrew.presentation.screen.base.BaseViewModel
import com.connectcrew.presentation.util.EditTextState
import com.connectcrew.presentation.util.Success
import com.connectcrew.presentation.util.delegate.ProjectFeedViewModelDelegate
import com.connectcrew.presentation.util.event.EventFlow
import com.connectcrew.presentation.util.event.MutableEventFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.IOException
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class ProjectWriteContainerViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val projectInfoUseCase: GetProjectInfoUseCase,
    private val createProjectFeedUseCase: CreateProjectFeedUseCase,
    private val updateProjectFeedUseCase: UpdateProjectFeedUseCase,
    projectFeedViewModelDelegate: ProjectFeedViewModelDelegate
) : BaseViewModel(), ProjectFeedViewModelDelegate by projectFeedViewModelDelegate {

    val projectFeedDetail
        get() = savedStateHandle.get<ProjectFeedDetail?>(KEY_PROJECT_FEED_DETAIL)

    val isUpdateProject: Boolean
        get() = projectFeedDetail != null

    private val projectInfoResult = loadDataSignal
        .flatMapLatest { projectInfoUseCase(Unit).asResult() }
        .onEach { if (it.succeeded) _projectContainerInfo.value = it.data?.asItem() }
        .stateIn(viewModelScope, SharingStarted.Lazily, ApiResult.Loading)

    private val _projectContainerInfo = MutableStateFlow<ProjectInfoContainer?>(null)
    val projectContainerInfo: StateFlow<ProjectInfoContainer?> = _projectContainerInfo

    val projectWriteInitializerUiState = combine(projectInfoResult, projectContainerInfo, ::Pair)
        .map { (apiResult, item) ->
            when (apiResult) {
                is ApiResult.Loading -> if (item == null) InitializerUiState.Loading else InitializerUiState.Success
                is ApiResult.Success -> InitializerUiState.Success
                is ApiResult.Error -> InitializerUiState.Error
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), InitializerUiState.Loading)

    val projectProgress = savedStateHandle.getStateFlow(KEY_PROJECT_WRITE_STEP, 0)

    // region Step 1
    val projectStep1State = projectProgress
        .map { getProjectState(1, it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ProjectWriteProgressState.STATE_IDLE)

    val projectTitle = savedStateHandle.getStateFlow(KEY_PROJECT_WRITE_TITLE, "")
    val projectTitleEditTextState = savedStateHandle.getStateFlow<EditTextState>(KEY_PROJECT_WRITE_TITLE_EDIT_TEXT_STATE, EditTextState.Loading)
    val enableProjectTitle = projectTitleEditTextState
        .map { it.Success }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)
    // endregion Step 1

    // region Step 2
    val projectStep2State = projectProgress
        .map { getProjectState(2, it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ProjectWriteProgressState.STATE_IDLE)

    val projectProgressState = savedStateHandle.getStateFlow<ProjectWriteProgressState?>(KEY_PROJECT_WRITE_PERIOD_PROGRESS_STATE, null)
    val projectLocationType = savedStateHandle.getStateFlow<ProjectWriteLocationType?>(KEY_PROJECT_WRITE_LOCATION_TYPE, null)
    val projectLocation = savedStateHandle.getStateFlow<ProjectInfo?>(KEY_PROJECT_WRITE_LOCATION, null)
    val enableProjectPeriodAndLocation = combine(projectProgressState, projectLocationType, projectLocation, ::Triple)
        .map { (it.first != null && it.second != null && (if (it.second != ProjectWriteLocationType.TYPE_ONLINE) it.third != null else true)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)
    // endregion Step 2

    // region Step 3
    val projectStep3State = projectProgress
        .map { getProjectState(3, it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ProjectWriteProgressState.STATE_IDLE)

    val projectPurposeType = savedStateHandle.getStateFlow<ProjectWritePurposeType?>(KEY_PROJECT_WRITE_PURPOSE_TYPE, null)
    val projectMinCareer = savedStateHandle.getStateFlow(KEY_PROJECT_WRITE_MIN_CAREER, ProjectWriteCareerType.NONE)
    val projectMaxCareer = savedStateHandle.getStateFlow(KEY_PROJECT_WRITE_MAX_CAREER, ProjectWriteCareerType.NONE)
    val isNoLimitCheck = savedStateHandle.getStateFlow(KEY_PROJECT_WRITE_CAREER_NO_LIMIT, false)
    val enableProjectPurposeAndCareer = combine(projectPurposeType, projectMinCareer, projectMaxCareer, isNoLimitCheck) { type, minCareer, maxCareer, isNoLimitCareer -> Triple(type, Pair(minCareer, maxCareer), isNoLimitCareer) }
        .map { (type, careerRange, isNoLimitCareer) -> (type != null && isNoLimitCareer) || (type != null && careerRange.first != ProjectWriteCareerType.NONE && careerRange.second != ProjectWriteCareerType.NONE) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)
    // endregion Step 3

    // region Step 4
    val projectStep4State = projectProgress
        .map { getProjectState(4, it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ProjectWriteProgressState.STATE_IDLE)

    val projectSelectedFiled = savedStateHandle.getStateFlow<List<ProjectFieldInfo>>(KEY_PROJECT_WRITE_SELECT_FIELD, emptyList())
    // endregion Step 4

    // region Step 5
    val projectStep5State = projectProgress
        .map { getProjectState(5, it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ProjectWriteProgressState.STATE_IDLE)

    val projectBannerUrls = savedStateHandle.getStateFlow<List<String>>(KEY_PROJECT_WRITE_BANNER_URL, emptyList())
    private val projectRemoveBannerUrls = savedStateHandle.getStateFlow<List<String>>(KEY_PROJECT_WRITE_REMOVE_BANNER_URL, emptyList()) // 프로젝트 수정 시에만 사용됨

    val projectIntroduction = savedStateHandle.getStateFlow(KEY_PROJECT_WRITE_INTRODUCTION, "")
    private val projectIntroductionEditTextState = savedStateHandle.getStateFlow<EditTextState>(KEY_PROJECT_WRITE_INTRODUCTION_EDIT_TEXT_STATE, EditTextState.Loading)

    val selectedLeaderMainJobCategory = savedStateHandle.getStateFlow<ProjectJobInfo?>(KEY_PROJECT_WRITE_LEADER_MAIN_JOB, null)
    val selectedLeaderSubJobCategory = savedStateHandle.getStateFlow<ProjectInfo?>(KEY_PROJECT_WRITE_LEADER_SUB_JOB, null)

    val selectedMemberMainJobCategory = savedStateHandle.getStateFlow<ProjectJobInfo?>(KEY_PROJECT_WRITE_MEMBER_MAIN_JOB, null)
    val selectedMemberSubJobCategory = savedStateHandle.getStateFlow<ProjectInfo?>(KEY_PROJECT_WRITE_MEMBER_SUB_JOB, null)

    private val _recruitmentMembers = MutableStateFlow<List<ProjectJobUiModel>>(emptyList())
    val recruitmentMembers: StateFlow<List<ProjectJobUiModel>> = _recruitmentMembers

    val projectTechStacks = savedStateHandle.getStateFlow<List<String>>(KEY_PROJECT_WRITE_TECH_STACK, emptyList())

    val enableProjectPost = combine(
        projectIntroductionEditTextState,
        selectedLeaderMainJobCategory,
        selectedLeaderSubJobCategory,
        recruitmentMembers
    ) { projectIntroduction, leaderMainJob, leaderSubJob, recruitmentMembers ->
        Triple(projectIntroduction, leaderMainJob to leaderSubJob, recruitmentMembers)
    }.map { (editTextState, leaderCategory, members) ->
        editTextState.Success && leaderCategory.first != null && leaderCategory.second != null && members.isNotEmpty()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)
    // endregion Step 5

    private val _navigateToMediaPicker = MutableEventFlow<Int>()
    val navigateToMediaPicker: EventFlow<Int> = _navigateToMediaPicker

    private val _navigateToExit = MutableEventFlow<Unit>()
    val navigateToExit: EventFlow<Unit> = _navigateToExit

    private val _navigateToProjectDetail = MutableEventFlow<Long>()
    val navigateToProjectDetail: EventFlow<Long> = _navigateToProjectDetail

    fun updateProject(projectFeed: ProjectFeedDetail) {
        // 프로젝트 제목
        setProjectTitle(projectFeed.title)

        // 프로젝트 진행 상태 (진행 전, 진행 중)
        setProjectPeriodProgress(ProjectWriteProgressState.entries.find { it.text == projectFeed.state } ?: ProjectWriteProgressState.STATE_IDLE)

        // 프로젝트 모집 분류 (온라인 / 오프라인 / 온,오프라인)
        setProjectLocationType(
            if (projectFeed.isOnline) {
                if (projectFeed.region == "미설정") ProjectWriteLocationType.TYPE_ONLINE else ProjectWriteLocationType.TYPE_ALL
            } else {
                ProjectWriteLocationType.TYPE_OFFLINE
            }
        )

        // 프로젝트 모집 지역 (강릉, 서울, 부산...)
        projectContainerInfo.value
            ?.regions
            ?.find { it.name == projectFeed.region }
            ?.let { setProjectLocation(it) }

        // 프로젝트 목적 (포트폴리오 , 예비 창업)
        setProjectPurposeType(ProjectWritePurposeType.entries.find { it.text == projectFeed.goal } ?: ProjectWritePurposeType.TYPE_PORTFOLIO)

        // 프로젝트 모집 경력. 경력무관일 경우.
        setProjectCareerNoLimit(projectFeed.careerMin == ProjectWriteCareerType.NONE.value && projectFeed.careerMax == ProjectWriteCareerType.NONE.value)

        // 프로젝트 모집 최소 경력.
        setProjectCareer(isMin = true, career = projectFeed.careerMin)

        // 프로젝트 모집 최대 경력.
        setProjectCareer(isMin = false, career = projectFeed.careerMax)

        // 프로젝트 카테고리 (여행, 연애, 애견)
        projectFeed.category
            .mapNotNull { selectedCategory -> projectContainerInfo.value?.category?.find { it.category.value == selectedCategory } }
            .map { setProjectField(it) }

        // 프로젝트 리더 직무
        projectFeed.leader.parts.firstOrNull()?.let {
            setProjectLeaderMainJob(it.category)
            setProjectLeaderSubJob(it.value)
        }

        // 프로젝트 멤버 직무
        projectFeed.recruitStatus.groupBy { it.partKey }.map {
            val part = it.value.first()
            setProjectMemberMainJob(part.category)
            setProjectMemberSubJob(part.part)
            updateRecruitmentMembersCount(
                ProjectJobUiModel(
                    mainJobName = part.category,
                    key = part.partKey,
                    name = part.part,
                    maxCount = part.maxCount,
                    comment = part.comment
                )
            )
        }

        // 프로젝트 대표 배너
        setProjectBanner(projectFeed.bannerImageUrls, false)

        // 프로젝트 소개 문구
        setProjectIntroduction(projectFeed.projectIntroduction)

        // 프로젝트에 사용되는 기술 스택
        projectFeed.skills.map { setProjectTechStacks(it) }
    }

    fun setWriteProgress(progress: Int) {
        if (projectProgress.value == progress) return
        savedStateHandle.set(KEY_PROJECT_WRITE_STEP, progress)
    }

    fun setProjectTitle(title: String) {
        if (projectTitle.value == title) return
        savedStateHandle.set(KEY_PROJECT_WRITE_TITLE, title)

        if (projectTitle.value.length < 2) {
            setProjectTitleEditTextState(EditTextState.Error(R.string.project_write_title_error))
        } else {
            setProjectTitleEditTextState(EditTextState.Success)
        }
    }

    private fun setProjectTitleEditTextState(editTextState: EditTextState) {
        if (projectTitleEditTextState.value == editTextState) return
        savedStateHandle.set(KEY_PROJECT_WRITE_TITLE_EDIT_TEXT_STATE, editTextState)
    }

    private fun getProjectState(selectProgress: Int, currentProgress: Int): ProjectWriteProgressState {
        return when {
            selectProgress == currentProgress -> ProjectWriteProgressState.STATE_PROCEEDING
            currentProgress > selectProgress -> ProjectWriteProgressState.STATE_PROGRESS_COMPLETED
            else -> ProjectWriteProgressState.STATE_IDLE
        }
    }

    fun setProjectPeriodProgress(progressState: ProjectWriteProgressState) {
        savedStateHandle.set(KEY_PROJECT_WRITE_PERIOD_PROGRESS_STATE, progressState)
    }

    fun setProjectLocationType(locationType: ProjectWriteLocationType) {
        if (projectLocationType.value == locationType) return
        savedStateHandle.set(KEY_PROJECT_WRITE_LOCATION_TYPE, locationType)

        if (projectLocationType.value == ProjectWriteLocationType.TYPE_ONLINE) {
            savedStateHandle.set(KEY_PROJECT_WRITE_LOCATION, null)
        }
    }

    fun setProjectLocation(projectInfo: ProjectInfo) {
        if (projectLocationType.value == ProjectWriteLocationType.TYPE_ONLINE) {
            savedStateHandle.set(KEY_PROJECT_WRITE_LOCATION, null)
        } else {
            savedStateHandle.set(KEY_PROJECT_WRITE_LOCATION, if (projectLocation.value?.key == projectInfo.key) null else projectInfo)
        }
    }

    fun setProjectPurposeType(purposeType: ProjectWritePurposeType) {
        if (projectPurposeType.value == purposeType) return
        savedStateHandle.set(KEY_PROJECT_WRITE_PURPOSE_TYPE, purposeType)
    }

    fun setProjectCareerNoLimit(isCheck: Boolean) {
        savedStateHandle.set(KEY_PROJECT_WRITE_CAREER_NO_LIMIT, isCheck)
        if (isCheck) {
            setProjectCareer(true, ProjectWriteCareerType.NONE.value)
            setProjectCareer(false, ProjectWriteCareerType.NONE.value)
        }
    }

    fun setProjectCareer(isMin: Boolean, career: String) {
        val careerType = ProjectWriteCareerType.entries.find { it.value == career } ?: ProjectWriteCareerType.NONE

        if (isMin) {
            savedStateHandle.set(KEY_PROJECT_WRITE_MIN_CAREER, careerType)

            if (careerType.index > projectMaxCareer.value.index
                && projectMinCareer.value != ProjectWriteCareerType.NONE
                && projectMaxCareer.value != ProjectWriteCareerType.NONE
            ) {
                savedStateHandle.set(KEY_PROJECT_WRITE_MIN_CAREER, ProjectWriteCareerType.NONE)
                setMessage(R.string.project_write_career_min_choice_error)
            }
        } else {
            savedStateHandle.set(KEY_PROJECT_WRITE_MAX_CAREER, careerType)

            if (careerType.index < projectMinCareer.value.index
                && projectMinCareer.value != ProjectWriteCareerType.NONE
                && projectMaxCareer.value != ProjectWriteCareerType.NONE
            ) {
                savedStateHandle.set(KEY_PROJECT_WRITE_MAX_CAREER, ProjectWriteCareerType.NONE)
                setMessage(R.string.project_write_career_max_choice_error)
            }
        }
    }

    fun setProjectField(projectFieldInfo: ProjectFieldInfo) {
        savedStateHandle.set(
            KEY_PROJECT_WRITE_SELECT_FIELD,
            projectSelectedFiled.value
                .toMutableList()
                .apply {
                    if (any { it.category.key == projectFieldInfo.category.key }) {
                        removeAll { it.category.key == projectFieldInfo.category.key }
                    } else {
                        if (size >= PROJECT_WRITE_FIELD_MAX_SIZE) {
                            setMessage(R.string.project_write_field_max_selected_description)
                        } else {
                            add(projectFieldInfo)
                        }
                    }
                }
        )
    }

    fun setProjectBanner(imageUrl: List<String>, isRemove: Boolean) {
        if (isRemove && !imageUrl.first().startsWith("content")) { // 공고 수정 시, 등록된 배너를 삭제하는 경우
            savedStateHandle.set(
                KEY_PROJECT_WRITE_REMOVE_BANNER_URL,
                projectRemoveBannerUrls.value.toMutableList().apply {
                    addAll(imageUrl)
                }
            )
        }

        savedStateHandle.set(
            KEY_PROJECT_WRITE_BANNER_URL,
            projectBannerUrls.value.toMutableList().apply {
                if (!isRemove) addAll(imageUrl) else remove(imageUrl.first())
            }
        )
    }

    fun setProjectIntroduction(text: String) {
        savedStateHandle.set(KEY_PROJECT_WRITE_INTRODUCTION, text)

        if (text.isEmpty()) {
            setInputProjectIntroductionTextState(EditTextState.Loading)
        } else {
            setInputProjectIntroductionTextState(EditTextState.Success)
        }
    }

    private fun setInputProjectIntroductionTextState(editTextState: EditTextState) {
        if (projectIntroductionEditTextState == editTextState) return
        savedStateHandle.set(KEY_PROJECT_WRITE_INTRODUCTION_EDIT_TEXT_STATE, editTextState)
    }

    fun setProjectLeaderMainJob(job: String) {
        projectContainerInfo.value?.jobs
            ?.find { it.name == job }
            ?.let { savedStateHandle.set(KEY_PROJECT_WRITE_LEADER_MAIN_JOB, it) }
    }

    fun setProjectLeaderSubJob(job: String?) {
        selectedLeaderMainJobCategory.value
            ?.value
            ?.find { it.name == job }
            .let { savedStateHandle.set(KEY_PROJECT_WRITE_LEADER_SUB_JOB, it) }
    }

    fun setProjectMemberMainJob(job: String) {
        projectContainerInfo.value?.jobs
            ?.find { it.name == job }
            ?.let { savedStateHandle.set(KEY_PROJECT_WRITE_MEMBER_MAIN_JOB, it) }
    }

    fun setProjectMemberSubJob(job: String?) {
        selectedMemberMainJobCategory.value
            ?.value
            ?.find { it.name == job }
            .let { subJobInfo ->
                savedStateHandle.set(KEY_PROJECT_WRITE_MEMBER_SUB_JOB, subJobInfo)

                subJobInfo?.name?.let { mainJobName ->
                    _recruitmentMembers.value = recruitmentMembers.value
                        .toMutableList()
                        .apply {
                            val projectJobUiModel = ProjectJobUiModel(mainJobName, subJobInfo.key, subJobInfo.name)

                            if (sumOf { it.maxCount } >= PROJECT_WRITE_MAXIMUM_MEMBER_COUNT) {
                                setMessage(R.string.project_write_post_part_max_count)
                            } else if (none { it.key == projectJobUiModel.key }) {
                                add(projectJobUiModel)
                            }
                        }
                }
            }
    }

    fun removeRecruitmentMembers(jobInfo: ProjectJobUiModel) {
        _recruitmentMembers.value = recruitmentMembers.value
            .toMutableList()
            .apply { remove(jobInfo) }
    }

    fun updateRecruitmentMembersCount(jobInfo: ProjectJobUiModel) {
        val members = recruitmentMembers.value
            .toMutableList()
            .apply { find { jobInfo.key == it.key }?.let { set(indexOf(it), jobInfo) } }

        if (members.sumOf { it.maxCount } > PROJECT_WRITE_MAXIMUM_MEMBER_COUNT) {
            setMessage(R.string.project_write_post_part_max_count)
        } else {
            _recruitmentMembers.value = members
        }
    }

    fun updateRecruitmentMembersComment(jobInfo: ProjectJobUiModel) {
        _recruitmentMembers.value = recruitmentMembers.value
            .toMutableList()
            .apply { find { jobInfo.key == it.key }?.let { set(indexOf(it), jobInfo) } }
    }

    fun setProjectTechStacks(techStack: String) {
        savedStateHandle.set(
            KEY_PROJECT_WRITE_TECH_STACK,
            projectTechStacks.value.toMutableList().apply {
                if (any { it == techStack }) return
                add(techStack)
            }
        )
    }

    fun removeProjectTechStacks(techStack: String) {
        savedStateHandle.set(
            KEY_PROJECT_WRITE_TECH_STACK,
            projectTechStacks.value.toMutableList().apply {
                removeAll { it == techStack }
            }
        )
    }

    fun navigateToMediaPicker() {
        viewModelScope.launch {
            _navigateToMediaPicker.emit(PROJECT_WRITE_BANNER_MAX_SIZE - projectBannerUrls.value.size)
        }
    }

    fun navigateToExit() {
        viewModelScope.launch {
            _navigateToExit.emit(Unit)
        }
    }

    fun navigateToProjectDetail() {
        viewModelScope.launch {
            if (projectFeedDetail == null) {
                createProjectFeedUseCase(
                    CreateProjectFeedUseCase.Params(
                        title = projectTitle.value,
                        region = projectLocation.value?.key ?: "NONE",
                        isOnline = projectLocationType.value!! != ProjectWriteLocationType.TYPE_OFFLINE,
                        state = projectProgressState.value!!.value,
                        careerMin = projectMinCareer.value.key,
                        careerMax = projectMaxCareer.value.key,
                        leaderPart = selectedLeaderSubJobCategory.value!!.key,
                        category = projectSelectedFiled.value.map { it.category.key },
                        goal = projectPurposeType.value!!.value,
                        introduction = projectIntroduction.value,
                        recruits = recruitmentMembers.value.map(ProjectJobUiModel::asEntity),
                        skills = projectTechStacks.value,
                        bannerImageUrls = projectBannerUrls.value
                    )
                )
            } else {
                updateProjectFeedUseCase(
                    UpdateProjectFeedUseCase.Params(
                        projectId = projectFeedDetail?.id!!,
                        title = projectTitle.value,
                        region = projectLocation.value?.key ?: "NONE",
                        isOnline = projectLocationType.value!! == ProjectWriteLocationType.TYPE_ONLINE,
                        state = projectProgressState.value!!.value,
                        careerMin = projectMinCareer.value.key,
                        careerMax = projectMaxCareer.value.key,
                        leaderPart = selectedLeaderSubJobCategory.value!!.key,
                        category = projectSelectedFiled.value.map { it.category.key },
                        goal = projectPurposeType.value!!.value,
                        introduction = projectIntroduction.value,
                        recruits = recruitmentMembers.value.map(ProjectJobUiModel::asEntity),
                        skills = projectTechStacks.value,
                        bannerImageUrls = projectBannerUrls.value,
                        removeBannerImageUrls = projectRemoveBannerUrls.value
                    )
                )
            }.asResult().onEach { setLoading(it is ApiResult.Loading) }.collect {
                when (it) {
                    is ApiResult.Loading -> return@collect
                    is ApiResult.Success -> {
                        invalidateProjectFeedAction(ZonedDateTime.now())
                        _navigateToProjectDetail.emit(it.data)
                    }

                    is ApiResult.Error -> when (it.exception) {
                        is IOException -> setMessage(R.string.network_error)
                        is TeamOneException -> setMessage(it.exception.message.toString())

                        else -> setMessage(R.string.unknown_error)
                    }
                }
            }
        }
    }

    companion object {
        private const val KEY_PROJECT_FEED_DETAIL = "project_feed_detail"

        private const val KEY_PROJECT_WRITE_STEP = "project_write_step"

        private const val KEY_PROJECT_WRITE_TITLE = "project_write_title"
        private const val KEY_PROJECT_WRITE_TITLE_EDIT_TEXT_STATE = "project_write_title_edit_text_state"

        private const val KEY_PROJECT_WRITE_PERIOD_PROGRESS_STATE = "project_write_period_progress_state"
        private const val KEY_PROJECT_WRITE_LOCATION_TYPE = "project_write_location_type"
        private const val KEY_PROJECT_WRITE_LOCATION = "project_write_location"

        private const val KEY_PROJECT_WRITE_PURPOSE_TYPE = "project_write_purpose_type"
        private const val KEY_PROJECT_WRITE_CAREER_NO_LIMIT = "project_write_career_no_limit"
        private const val KEY_PROJECT_WRITE_MIN_CAREER = "project_write_min_career"
        private const val KEY_PROJECT_WRITE_MAX_CAREER = "project_write_max_career"

        private const val KEY_PROJECT_WRITE_SELECT_FIELD = "project_write_select_field"
        private const val PROJECT_WRITE_FIELD_MAX_SIZE = 3

        private const val KEY_PROJECT_WRITE_BANNER_URL = "project_write_banner_url"
        private const val KEY_PROJECT_WRITE_REMOVE_BANNER_URL = "project_write_remove_banner_url"

        private const val KEY_PROJECT_WRITE_INTRODUCTION = "project_write_introduction"
        private const val KEY_PROJECT_WRITE_INTRODUCTION_EDIT_TEXT_STATE = "project_write_introduction_edit_text_state"

        private const val KEY_PROJECT_WRITE_LEADER_MAIN_JOB = "project_write_leader_main_job"
        private const val KEY_PROJECT_WRITE_LEADER_SUB_JOB = "project_write_leader_sub_job"

        private const val KEY_PROJECT_WRITE_MEMBER_MAIN_JOB = "project_write_member_main_job"
        private const val KEY_PROJECT_WRITE_MEMBER_SUB_JOB = "project_write_member_sub_job"

        private const val KEY_PROJECT_WRITE_TECH_STACK = "project_write_tech_stack"

        private const val PROJECT_WRITE_BANNER_MAX_SIZE = 3
        private const val PROJECT_WRITE_MAXIMUM_MEMBER_COUNT = 29
    }
}