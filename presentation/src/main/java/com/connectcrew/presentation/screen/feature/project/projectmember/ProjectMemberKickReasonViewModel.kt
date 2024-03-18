package com.connectcrew.presentation.screen.feature.project.projectmember

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.connectcrew.domain.usecase.project.KickProjectMemberUseCase
import com.connectcrew.domain.util.ApiResult
import com.connectcrew.domain.util.TeamOneException
import com.connectcrew.domain.util.asResult
import com.connectcrew.presentation.R
import com.connectcrew.presentation.model.project.KickReason
import com.connectcrew.presentation.model.project.KickReasonType
import com.connectcrew.presentation.model.project.asEntity
import com.connectcrew.presentation.screen.base.BaseViewModel
import com.connectcrew.presentation.util.WhileViewSubscribed
import com.connectcrew.presentation.util.event.EventFlow
import com.connectcrew.presentation.util.event.MutableEventFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ProjectMemberKickReasonViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val kickProjectMemberUseCase: KickProjectMemberUseCase
) : BaseViewModel() {

    private val projectId: StateFlow<Long?> = savedStateHandle.getStateFlow(KEY_PROJECT_ID, null)

    private val memberId: StateFlow<Int?> = savedStateHandle.getStateFlow(KEY_PROJECT_MEMBER_ID, null)

    private val _kickReasonEtc: MutableStateFlow<String> = MutableStateFlow("")
    val kickReasonEtc: StateFlow<String> = _kickReasonEtc

    private val selectedKickReasons: MutableStateFlow<List<KickReasonType>> = MutableStateFlow(emptyList())
    val kickReason: StateFlow<List<KickReasonType>> = combine(selectedKickReasons, _kickReasonEtc, ::Pair)
        .onEach { it.first.find { it == KickReasonType.TYPE_ETC }?.let { it.detail = _kickReasonEtc.value } }
        .mapLatest { (reasons, _) -> reasons}
        .stateIn(viewModelScope, WhileViewSubscribed, emptyList())

    private val _navigateToProjectContainer = MutableEventFlow<Boolean>()
    val navigateToProjectContainer: EventFlow<Boolean> = _navigateToProjectContainer

    fun navigateToProjectContainer(isKicked: Boolean) {
        viewModelScope.launch {
            _navigateToProjectContainer.emit(isKicked)
        }
    }

    fun setKickReasonEtc(text: String) {
        _kickReasonEtc.value = text
    }

    fun onChangeCheckReason(reason: KickReasonType, isChecked: Boolean) {
        when (reason) {
            KickReasonType.TYPE_ABUSE,
            KickReasonType.TYPE_BAD_PARTICIPATION,
            KickReasonType.TYPE_DISSENSION,
            KickReasonType.TYPE_GIVEN_UP,
            KickReasonType.TYPE_OBSCENITY -> {
                if (isChecked) {
                    selectedKickReasons.value = selectedKickReasons.value.toMutableList().apply { add(reason) }
                } else {
                    selectedKickReasons.value = selectedKickReasons.value.toMutableList().apply { remove(reason) }
                }
            }

            KickReasonType.TYPE_ETC -> {
                if (isChecked) {
                    selectedKickReasons.value = selectedKickReasons.value.toMutableList().apply { add(reason) }
                } else {
                    selectedKickReasons.value = selectedKickReasons.value.toMutableList().apply { remove(reason) }
                }
            }
        }
    }

    fun kickProjectMember() {
        viewModelScope.launch {
            kickProjectMemberUseCase(
                KickProjectMemberUseCase.Params(
                    projectId = projectId.value!!,
                    userId = memberId.value!!,
                    reasons = kickReason.value.map(KickReasonType::toKickReason).map(KickReason::asEntity)
                )
            ).asResult().onEach {
                setLoading(it is ApiResult.Loading)
            }.collect {
                when (it) {
                    is ApiResult.Loading -> return@collect
                    is ApiResult.Success -> {
                        navigateToProjectContainer(true)
                    }

                    is ApiResult.Error -> {
                        when (it.exception) {
                            is IOException -> setMessage(R.string.network_error)
                            is TeamOneException -> setMessage(it.exception.message.toString())
                            else -> setMessage(R.string.unknown_error)
                        }
                        setMessage(R.string.unknown_error)
                    }
                }
            }
        }
    }

    companion object {
        private const val KEY_PROJECT_ID = "project_id"
        private const val KEY_PROJECT_MEMBER_ID = "member_id"
    }
}