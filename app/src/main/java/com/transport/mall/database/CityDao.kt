package com.transport.mall.database

import androidx.room.Dao
import androidx.room.Query
import com.transport.mall.model.CityModel

@Dao
interface CityDao {
    @Query("SELECT * FROM cities")
    fun getAll(): List<CityModel>

    /* @Query("SELECT * FROM Cities")
     fun getAll(): List<CityModel?>?*/

    /*@Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(model: List<CityModel?>?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(model: CityModel?)

    @Query("DELETE FROM Cities")
    fun deleteAll()*/
}