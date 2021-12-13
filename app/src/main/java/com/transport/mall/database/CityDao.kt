package com.transport.mall.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.transport.mall.model.CityModel

@Dao
interface CityDao {
    @Query("SELECT * FROM cities where isActive=1 AND isDeleted=0 ORDER BY LOWER(name_en) ASC")
    fun getAll(): LiveData<List<CityModel>>

    @Query("SELECT * FROM cities where stateCode=:stateCode AND isActive=1 AND isDeleted=0 ORDER BY LOWER(name_en) ASC")
    fun getAllByState(stateCode: String): LiveData<List<CityModel>>

    @Query("SELECT * FROM cities where stateCode IN (:stateCodes) AND isActive=1 AND isDeleted=0")
    fun getAllByMultipleStates(stateCodes: String): LiveData<List<CityModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(model: List<CityModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(model: CityModel?)

    @Query("DELETE FROM cities")
    fun deleteAll()
}