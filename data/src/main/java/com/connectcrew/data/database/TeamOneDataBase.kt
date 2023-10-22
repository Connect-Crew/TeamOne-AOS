package com.connectcrew.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.connectcrew.data.model.test.TestDao
import com.connectcrew.data.model.test.TestModel

@Database(
    entities = [TestModel::class], // TODO:: 더미 파일입니다. 추후 제거 예정
    version = 1,
    exportSchema = false
)
internal abstract class TeamOneDataBase : RoomDatabase() {

    abstract fun testDao(): TestDao // TODO:: 더미 파일입니다. 추후 제거 예정

    companion object {
        private const val databaseName = "team-one-db"

        fun buildDatabase(context: Context): TeamOneDataBase {
            return Room
                .databaseBuilder(context, TeamOneDataBase::class.java, databaseName)
                .enableMultiInstanceInvalidation()
                .build()
        }
    }
}