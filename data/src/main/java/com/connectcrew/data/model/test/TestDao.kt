package com.connectcrew.data.model.test

import androidx.room.Dao
import androidx.room.Query

@Dao
internal interface TestDao { // TODO:: 더미 파일입니다. 추후 제거 예정

    @Query("SELECT * FROM TestTable")
    fun getTestData(): TestModel
}