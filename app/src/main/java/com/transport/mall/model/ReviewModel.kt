package com.transport.mall.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ReviewModel(
    @SerializedName("_id") var _id: String?,
    @SerializedName("dhaba_id") var dhaba_id: String?,
    @SerializedName("review") var review: String?,
    @SerializedName("rating") var rating: String?,
    @SerializedName("createdAt") var createdAt: String?,
    @SerializedName("updatedAt") var updatedAt: String?,
    @SerializedName("user_id") var user_id: UserModel?
) : Serializable {
    override fun toString(): String {
        return review.toString()
    }
}