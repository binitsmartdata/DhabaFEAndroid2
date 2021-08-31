package com.transport.mall.model.user

import com.google.gson.annotations.SerializedName

data class UserModel(
    @SerializedName("_id") val id: String,
    @SerializedName("fname") val fname: LangModel,
    @SerializedName("lname") val lname: LangModel,
    @SerializedName("email") val email: String,
    @SerializedName("lastLogin") val lastLogin: String,
    @SerializedName("accessToken") val accessToken: String
)