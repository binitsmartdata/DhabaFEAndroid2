package com.transport.mall.model

import com.google.gson.annotations.SerializedName

data class UserModelMain(
    @SerializedName("executive") val executive: UserModel,
    @SerializedName("manager") val manager: UserModel,
    @SerializedName("areaHead") val areaHead: UserModel
)