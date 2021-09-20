package com.transport.mall.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.transport.mall.model.HighwayModel

@Dao
interface HighwayDao {
    @Query("SELECT * FROM highways")
    fun getAll(): LiveData<List<HighwayModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(model: List<HighwayModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(model: HighwayModel?)

    @Query("DELETE FROM highways")
    fun deleteAll()
}