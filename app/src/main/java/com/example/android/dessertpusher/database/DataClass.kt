package com.example.android.dessertpusher.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dataClass")
data class DataClass(
        @PrimaryKey(autoGenerate = true)
        val dessert_id: Long = 0L,

        @ColumnInfo
        val dataRevenue: Int,

        @ColumnInfo
        val dataAmountSold: Int
)