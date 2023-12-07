package com.connectcrew.presentation.screen.feature.projectwrite

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