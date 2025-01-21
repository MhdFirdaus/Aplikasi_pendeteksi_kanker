package com.dicoding.asclepius.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.dicoding.asclepius.local.entity.CancerResult

@Dao
interface CancerResultDao {
    @Insert
    suspend fun insertResult(result: CancerResult)

    @Query("SELECT * FROM cancer_results ORDER BY id DESC")
    suspend fun getAllResults(): List<CancerResult>

    @Delete
    suspend fun deleteResult(result: CancerResult)
}