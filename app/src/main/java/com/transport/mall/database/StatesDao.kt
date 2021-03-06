package com.transport.mall.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.transport.mall.model.StateModel

@Dao
interface StatesDao {
    @Query("SELECT * FROM states where isActive=1 AND isDeleted=0 ORDER BY LOWER(name_en) ASC")
    fun getAll(): LiveData<List<StateModel>>

    @Query("SELECT * FROM states where name_en=:name AND isActive=1 AND isDeleted=0 ORDER BY LOWER(name_en) ASC")
    fun getByName(name: String): LiveData<List<StateModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(model: List<StateModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(model: StateModel?)

    @Query("DELETE FROM states")
    fun deleteAll()
}