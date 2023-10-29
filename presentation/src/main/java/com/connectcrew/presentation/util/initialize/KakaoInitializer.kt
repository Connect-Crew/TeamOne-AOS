package com.connectcrew.presentation.util.initialize

import android.content.Context
import androidx.startup.Initializer
import com.connectcrew.presentation.R
import com.kakao.sdk.common.KakaoSdk

class KakaoInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        KakaoSdk.init(context, context.getString(R.string.KAKAO_API_KEY))
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()

}