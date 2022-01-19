package com.transport.mall.database

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class InternalDocsListModel<T>(
    @SerializedName("count") var count: String?,
    @SerializedName("docs") var data: T
) : Serializable