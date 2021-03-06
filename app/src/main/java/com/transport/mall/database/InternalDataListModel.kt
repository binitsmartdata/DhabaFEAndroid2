package com.transport.mall.database

import androidx.room.Entity
import androidx.room.Index
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class InternalDataListModel<T>(
	@SerializedName("data") var data: T
) : Serializable