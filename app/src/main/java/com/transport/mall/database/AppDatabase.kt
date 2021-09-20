package com.transport.mall.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.transport.mall.model.CityModel
import com.transport.mall.model.HighwayModel
import com.transport.mall.model.StateModel

/**
 * Created by parambir.singh on 1/09/21.
 */
@Database(entities = [CityModel::class, StateModel::class, HighwayModel::class], version = 8)
abstract class AppDatabase : RoomDatabase() {

    abstract fun cityDao(): CityDao
    abstract fun statesDao(): StatesDao
    abstract fun highwayDao(): HighwayDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java, "transport_mall.db"
                        )
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build()
                    }
                }
            }
            return INSTANCE
        }
    }
}