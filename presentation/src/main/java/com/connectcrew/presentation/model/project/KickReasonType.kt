package com.connectcrew.presentation.model.project

enum class KickReasonType(val kickType: String, var detail: String) {
    TYPE_ABUSE("ABUSE", "욕설/비하발언"),
    TYPE_BAD_PARTICIPATION("BAD_PARTICIPATION", "참여율 저조"),
    TYPE_DISSENSION("DISSENSION", "팀원과의 불화"),
    TYPE_GIVEN_UP("GIVEN_UP", "자진 중도 포기"),
    TYPE_OBSCENITY("OBSCENITY", "19+ 음란성, 만남 유도"),
    TYPE_ETC("ETC", "");

    fun toKickReason(): KickReason = KickReason(type = kickType, reason = detail)
}