package com.transport.mall.database

import androidx.room.Entity
import androidx.room.Index
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(
	tableName = "cities",
	indices = [Index(
		value = ["_id"],
		unique = true
	)]
)
data class InternalDataListModel<T>(
	@SerializedName("data") var data: T
) : Serializable