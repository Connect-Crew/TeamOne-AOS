package com.connectcrew.presentation.screen.feature.projectwrite

import com.connectcrew.presentation.R

enum class ProjectWriteProgressState {
    STATE_IDLE,
    STATE_PROCEEDING,
    STATE_PROGRESS_COMPLETED
}

enum class ProjectWriteLocationType {
    TYPE_ONLINE,
    TYPE_OFFLINE,
    TYPE_ALL
}

enum class ProjectWritePurposeType(val value: String) {
    TYPE_STARTUP("STARTUP"),
    TYPE_PORTFOLIO("PORTFOLIO")
}

enum class ProjectWriteCareerType(val index: Int, val value: String) {
    NONE(0, "none"),
    SEEKER(1, "준비생"),
    ENTRY(2, "신입"),
    YEAR_1(3, "1년"),
    YEAR_2(4, "2년"),
    YEAR_3(5, "3년"),
    YEAR_4(6, "4년"),
    YEAR_5(7, "5년"),
    YEAR_6(8, "6년"),
    YEAR_7(9, "7년"),
    YEAR_8(10, "8년"),
    YEAR_9(11, "9년"),
    YEAR_10_PLUS(12, "10년+"),
}

enum class ProjectWriteFieldType(val key: String, val value: String) {
    TYPE_IT("IT", "IT"),
    TYPE_APP("APP", "앱서비스"),
    TYPE_TRAVEL("TRAVEL", "여행"),
    TYPE_ECOMMERCE("ECOMMERCE", "쇼핑·이커머스"),
    TYPE_COMMUNITY("COMMUNITY", "커뮤니티"),
    TYPE_EDUCATION("EDUCATION", "교육"),
    TYPE_HEALTH_LIFE("HEALTH_LIFE", "건강·생활"),
    TYPE_BABY_PET("BABY_PET", "육아·반려동물"),
    TYPE_LOVE("LOVE", "연애"),
    TYPE_GAME("GAME", "게임"),
    TYPE_FOOD("FOOD", "요식업"),
    TYPE_FINANCE("FINANCE", "금융"),
    TYPE_HOUSE("HOUSE", "부동산·숙박"),
    TYPE_AI("AI", "인공지능"),
    TYPE_ETC("ETC", "기타")
}

fun ProjectWriteFieldType.getFiledIcon(): Int? {
    return when (this) {
        ProjectWriteFieldType.TYPE_IT -> R.drawable.ic_project_field_it
        ProjectWriteFieldType.TYPE_APP -> R.drawable.ic_project_field_app_service
        ProjectWriteFieldType.TYPE_TRAVEL -> R.drawable.ic_project_field_travel
        ProjectWriteFieldType.TYPE_ECOMMERCE -> R.drawable.ic_project_field_shopping
        ProjectWriteFieldType.TYPE_COMMUNITY -> R.drawable.ic_project_field_community
        ProjectWriteFieldType.TYPE_EDUCATION -> R.drawable.ic_project_filed_education
        ProjectWriteFieldType.TYPE_HEALTH_LIFE -> R.drawable.ic_project_field_health
        ProjectWriteFieldType.TYPE_BABY_PET -> R.drawable.ic_project_field_pet
        ProjectWriteFieldType.TYPE_LOVE -> R.drawable.ic_project_field_love
        ProjectWriteFieldType.TYPE_GAME -> R.drawable.ic_project_field_game
        ProjectWriteFieldType.TYPE_FOOD -> R.drawable.ic_project_field_food
        ProjectWriteFieldType.TYPE_FINANCE -> R.drawable.ic_project_field_finance
        ProjectWriteFieldType.TYPE_HOUSE -> R.drawable.ic_project_field_real_estate
        ProjectWriteFieldType.TYPE_AI -> R.drawable.ic_project_field_ai
        ProjectWriteFieldType.TYPE_ETC -> null
    }
}