package com.example.fitbit.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyEntryDao {
    @Insert
    suspend fun insert(dailyEntry: DailyEntry)

    @Query("SELECT * FROM daily_entry ORDER BY date DESC")
    fun getAll(): Flow<List<DailyEntry>>

    @Delete
    suspend fun delete(dailyEntry: DailyEntry)
}