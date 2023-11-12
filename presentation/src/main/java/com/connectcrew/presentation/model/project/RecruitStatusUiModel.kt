package com.connectcrew.presentation.model.project

sealed class RecruitStatusUiModel {

    data class TotalMemberRecruitStatusUiModel(
        val currentCount: Int,
        val maxCount: Int
    ) : RecruitStatusUiModel()

    data class PartMembersRecruitStatusUiModel(
        val recruitStatus: RecruitStatus
    ) : RecruitStatusUiModel()
}