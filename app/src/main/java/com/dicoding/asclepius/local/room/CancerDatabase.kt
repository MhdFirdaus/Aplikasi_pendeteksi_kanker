package com.dicoding.asclepius.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.asclepius.local.entity.CancerResult

@Database(entities = [CancerResult::class], version = 1)
abstract class CancerDatabase : RoomDatabase() {
    abstract fun cancerResultDao(): CancerResultDao

    companion object {
        @Volatile
        private var INSTANCE: CancerDatabase? = null

        fun getDatabase(context: Context): CancerDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CancerDatabase::class.java,
                    "cancer_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}