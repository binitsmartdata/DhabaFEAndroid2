package com.transport.mall.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.transport.mall.model.CityAndStateModel

@Dao
interface CityDao {
    @Query("SELECT * FROM cities")
    fun getAll(): LiveData<List<CityAndStateModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(andStateModel: List<CityAndStateModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(andStateModel: CityAndStateModel?)

    @Query("DELETE FROM Cities")
    fun deleteAll()
}