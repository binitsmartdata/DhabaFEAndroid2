package com.transport.mall.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.transport.mall.model.CityModel

@Dao
interface CityDao {
    @Query("SELECT * FROM cities")
    fun getAll(): LiveData<List<CityModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(model: List<CityModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(model: CityModel?)

    @Query("DELETE FROM Cities")
    fun deleteAll()
}