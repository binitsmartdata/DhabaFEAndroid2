package com.transport.mall.database

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class InternalItemsListModel<T>(
    @SerializedName("count") var count: String?,
    @SerializedName("items") var data: T
) : Serializable