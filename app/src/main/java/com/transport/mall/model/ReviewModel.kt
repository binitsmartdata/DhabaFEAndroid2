package com.transport.mall.model

import android.content.Context
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import com.google.gson.annotations.SerializedName
import com.transport.mall.R
import com.transport.mall.utils.common.GlobalUtils
import java.io.Serializable

class ReviewModel : Serializable, BaseObservable() {
    @SerializedName(value = "dhaba_id")
    var dhaba_id: String = ""
        @Bindable get() = field
        set(dhaba_id) {
            field = dhaba_id
            notifyPropertyChanged(BR.dhaba_id)
        }

    @SerializedName(value = "user_id")
    var user_id: String = ""
        @Bindable get() = field
        set(user_id) {
            field = user_id
            notifyPropertyChanged(BR.user_id)
        }

    @SerializedName(value = "review")
    var review: String = ""
        @Bindable get() = field
        set(review) {
            field = review
            notifyPropertyChanged(BR.review)
        }

    @SerializedName(value = "rating")
    var rating: Float = 0f
        @Bindable get() = field
        set(rating) {
            field = rating
            notifyPropertyChanged(BR.rating)
        }

    @SerializedName(value = "is_Approved")
    var is_Approved: Boolean = false

    @SerializedName(value = "isDeleted")
    var isDeleted: Boolean = false

    @SerializedName(value = "isActive")
    var isActive: Boolean = false

    @SerializedName(value = "createdAt")
    var createdAt: String = ""
        @Bindable get() = field
        set(createdAt) {
            field = createdAt
            notifyPropertyChanged(BR.createdAt)
        }

    @SerializedName(value = "updatedAt")
    var updatedAt: String = ""
        @Bindable get() = field
        set(updatedAt) {
            field = updatedAt
            notifyPropertyChanged(BR.updatedAt)
        }

    @SerializedName("imageList")
    var imagesList = ArrayList<PhotosModel>()

    fun hasEverything(context: Context): Boolean {
        if (rating.toFloat() < 1) {
            GlobalUtils.showToastInCenter(context, context.getString(R.string.rating_validation))
            return false
        } else if (GlobalUtils.getNonNullString(review, "").trim().isEmpty()) {
            GlobalUtils.showToastInCenter(context, context.getString(R.string.review_validation))
            return false
        } else {
            return true
        }
    }
}