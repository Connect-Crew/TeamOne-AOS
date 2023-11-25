package com.connectcrew.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.connectcrew.data.datasource.searchhistory.local.SearchHistoryDao
import com.connectcrew.data.model.searchhistory.SearchKeyword

@Database(
    entities = [SearchKeyword::class],
    version = 1,
    exportSchema = false
)
internal abstract class TeamOneDataBase : RoomDatabase() {

    abstract fun searchHistoryDao(): SearchHistoryDao

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