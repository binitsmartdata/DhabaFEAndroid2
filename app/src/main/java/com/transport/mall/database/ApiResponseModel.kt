package com.transport.mall.database

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ApiResponseModel<T>(
    @SerializedName("status")
    var status: Int,
    @SerializedName("message")
    var message: String,
    @SerializedName("data")
    var data: T?
) : Serializable