package com.example.android.dessertpusher.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DatabaseDao {
    @Insert
    fun insertPoints(oldData: DataClass)

    @Query("SELECT * FROM dataClass ORDER BY dessert_id DESC LIMIT 1")
    fun getKcal(): DataClass?

    @Query("DELETE FROM dataClass")
    fun clearDatabase()
}