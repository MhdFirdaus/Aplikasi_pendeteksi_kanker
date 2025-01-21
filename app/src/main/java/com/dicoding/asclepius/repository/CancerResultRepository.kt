package com.dicoding.asclepius.repository

import com.dicoding.asclepius.local.entity.CancerResult
import com.dicoding.asclepius.local.room.CancerResultDao


class CancerResultRepository(private val dao: CancerResultDao) {
    suspend fun insertResult(result: CancerResult) = dao.insertResult(result)

    suspend fun getAllResults(): List<CancerResult> = dao.getAllResults()

    suspend fun deleteResult(result: CancerResult) = dao.deleteResult(result)
}