package com.connectcrew.data.model.test

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TestTable")
internal data class TestModel(
    @PrimaryKey
    val testName: String,
    @ColumnInfo
    val testAge: Int
)