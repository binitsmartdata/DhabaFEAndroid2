package com.transport.mall.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "banks",
    indices = [Index(
        value = ["_id"],
        unique = true
    )]
)
data class BankNamesModel(
    @ColumnInfo(name = "uid")
    @PrimaryKey(autoGenerate = true) val autoId: Int? = null,

    @ColumnInfo(name = "_id")
    val _id: String? = "",

    @ColumnInfo(name = "name")
    val name: String? = "",

    @ColumnInfo(name = "discription")
    val discription: String? = "",

    @ColumnInfo(name = "createdAt")
    val createdAt: String? = "",

    @ColumnInfo(name = "updatedAt")
    val updatedAt: String? = ""
) : Serializable {
    override fun toString(): String {
        return name!!
    }
}