package com.transport.mall.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.transport.mall.model.BankNamesModel

@Dao
interface BanksDao {
    @Query("SELECT * FROM banks")
    fun getAll(): LiveData<List<BankNamesModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(model: List<BankNamesModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(model: BankNamesModel?)

    @Query("DELETE FROM states")
    fun deleteAll()
}