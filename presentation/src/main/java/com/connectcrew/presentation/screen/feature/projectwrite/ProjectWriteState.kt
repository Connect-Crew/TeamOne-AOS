package com.connectcrew.presentation.screen.feature.projectwrite

import com.connectcrew.presentation.R

enum class ProjectWriteProgressState(val value: String, val text: String) {
    STATE_IDLE("NOT_STARTED", "진행 전"),
    STATE_PROCEEDING("IN_PROGRESS", "진행 중"),
    STATE_PROGRESS_COMPLETED("COMPLETED", "진행 완료")
}

enum class ProjectWriteLocationType {
    TYPE_ONLINE,
    TYPE_OFFLINE,
    TYPE_ALL
}

enum class ProjectWritePurposeType(val value: String, val text:String) {
    TYPE_STARTUP("STARTUP", "스타트업"),
    TYPE_PORTFOLIO("PORTFOLIO", "포트폴리오")
}

enum class ProjectWriteCareerType(val index: Int, val key: String,  val value: String) {
    NONE(0,  "NONE","경력무관"),
    SEEKER(1, "SEEKER","준비생"),
    ENTRY(2, "ENTRY","신입"),
    YEAR_1(3, "YEAR_1","1년"),
    YEAR_2(4, "YEAR_2","2년"),
    YEAR_3(5, "YEAR_3","3년"),
    YEAR_4(6, "YEAR_4","4년"),
    YEAR_5(7, "YEAR_5","5년"),
    YEAR_6(8, "YEAR_6","6년"),
    YEAR_7(9, "YEAR_7","7년"),
    YEAR_8(10, "YEAR_8","8년"),
    YEAR_9(11, "YEAR_9","9년"),
    YEAR_10_PLUS(12, "YEAR_10_PLUS","10년+"),
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