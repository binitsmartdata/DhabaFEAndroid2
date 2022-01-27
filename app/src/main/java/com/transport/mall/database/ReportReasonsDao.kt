package com.transport.mall.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.transport.mall.model.ReportReasonModel

@Dao
interface ReportReasonsDao {
    @Query("SELECT * FROM reportReasons")
    fun getAll(): LiveData<List<ReportReasonModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(model: List<ReportReasonModel>)

    @Query("DELETE FROM reportReasons")
    fun deleteAll()
}