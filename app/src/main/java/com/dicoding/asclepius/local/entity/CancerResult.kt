package com.dicoding.asclepius.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cancer_results")
data class CancerResult(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val label: String,
    val confidence: Float,
    val imageData: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CancerResult

        if (id != other.id) return false
        if (label != other.label) return false
        if (confidence != other.confidence) return false
        if (!imageData.contentEquals(other.imageData)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + label.hashCode()
        result = 31 * result + confidence.hashCode()
        result = 31 * result + imageData.contentHashCode()
        return result
    }
}