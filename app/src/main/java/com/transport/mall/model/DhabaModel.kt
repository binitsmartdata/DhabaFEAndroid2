package com.transport.mall.model

import android.content.Context
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import com.google.gson.annotations.SerializedName
import com.transport.mall.R
import com.transport.mall.utils.common.GenericCallBackTwoParams
import com.transport.mall.utils.common.GlobalUtils.getNonNullString
import java.io.Serializable

/**
 * Created by Parambir Singh on 2020-01-24.
 */
class DhabaModel : Serializable, BaseObservable() {

    val CATEGORY_GOLD = "Gold"
    val CATEGORY_BRONZE = "Bronze"
    val CATEGORY_SILVER = "Silver"

    var _id: String = ""
        @Bindable get() = field
        set(_id) {
            field = _id
            notifyPropertyChanged(BR._id)
        }

    var status: String = ""
        @Bindable get() = field
        set(status) {
            field = status
            notifyPropertyChanged(BR.status)
        }

    var owner_id: String = ""
        @Bindable get() = field
        set(owner_id) {
            field = owner_id
            notifyPropertyChanged(BR.owner_id)
        }

    var name: String = ""
        @Bindable get() = field
        set(name) {
            field = name
            notifyPropertyChanged(BR.name)
        }

    var ownerName: String = ""
        @Bindable get() = field
        set(ownerName) {
            field = ownerName
            notifyPropertyChanged(BR.ownerName)
        }

    var address: String = ""
        @Bindable get() = field
        set(address) {
            field = address
            notifyPropertyChanged(BR.address)
        }

    var landmark: String = ""
        @Bindable get() = field
        set(landmark) {
            field = landmark
            notifyPropertyChanged(BR.landmark)
        }

    var area: String = ""
        @Bindable get() = field
        set(area) {
            field = area
            notifyPropertyChanged(BR.area)
        }

    var dhabaCategory: String = ""
        @Bindable get() = field
        set(dhabaCategory) {
            field = dhabaCategory
            notifyPropertyChanged(BR.dhabaCategory)
        }

    var highway: String = ""
        @Bindable get() = field
        set(highway) {
            field = highway
            notifyPropertyChanged(BR.highway)
        }

    var state: String = ""
        @Bindable get() = field
        set(state) {
            field = state
            notifyPropertyChanged(BR.state)
        }

    var city: String = ""
        @Bindable get() = field
        set(city) {
            field = city
            notifyPropertyChanged(BR.city)
        }

    var pincode: String = ""
        @Bindable get() = field
        set(pincode) {
            field = pincode
            notifyPropertyChanged(BR.pincode)
        }

/*
    var location: String = ""
        @Bindable get() = field
        set(location) {
            field = location
            notifyPropertyChanged(BR.location)
        }
*/

    var mobile: String = ""
        @Bindable get() = field
        set(mobile) {
            field = mobile
            notifyPropertyChanged(BR.mobile)
        }

    var propertyStatus: String = "other"
        @Bindable get() = field
        set(propertyStatus) {
            field = propertyStatus
            notifyPropertyChanged(BR.propertyStatus)
        }

    var images: String = ""
        @Bindable get() = field
        set(images) {
            field = images
            notifyPropertyChanged(BR.images)
        }

    var videos: String = ""
        @Bindable get() = field
        set(videos) {
            field = videos
            notifyPropertyChanged(BR.videos)
        }

    var createdBy: String = ""
        @Bindable get() = field
        set(createdBy) {
            field = createdBy
            notifyPropertyChanged(BR.createdBy)
        }

    var updatedBy: String = ""
        @Bindable get() = field
        set(updatedBy) {
            field = updatedBy
            notifyPropertyChanged(BR.updatedBy)
        }
/*
    var isDeleted: String = "false"
        @Bindable get() = field
        set(isDeleted) {
            field = isDeleted
            notifyPropertyChanged(BR.isDeleted)
        }*/

    @SerializedName("isActive")
    var active: String? = "true"
        @Bindable get() = field
        set(active) {
            field = active
            notifyPropertyChanged(BR.active)
        }

    var latitude: String = ""
        @Bindable get() = field
        set(latitude) {
            field = latitude
            notifyPropertyChanged(BR.latitude)
        }

    var longitude: String = ""
        @Bindable get() = field
        set(longitude) {
            field = longitude
            notifyPropertyChanged(BR.longitude)
        }

    var blockDay: Int = 0
        @Bindable get() = field
        set(blockDay) {
            field = blockDay
            notifyPropertyChanged(BR.blockDay)
        }

    var blockMonth: Int = 0
        @Bindable get() = field
        set(blockMonth) {
            field = blockMonth
            notifyPropertyChanged(BR.blockMonth)
        }

    var isDraft: String = ""


    fun hasEverything(context: Context, callback: GenericCallBackTwoParams<Boolean, String>) {
        if (name.trim().isEmpty()) {
            callback.onResponse(false, context.getString(R.string.enter_dhaba_name))
        } else if ((latitude.trim().isEmpty() || longitude.trim().isEmpty())
            || (getNonNullString(latitude.trim(), "0").toDouble() == 0.toDouble()
                    || getNonNullString(longitude.trim(), "0").trim().toDouble() == 0.toDouble())
        ) {
            callback.onResponse(false, context.getString(R.string.choose_dhaba_location))
        } else if (address.trim().isEmpty()) {
            callback.onResponse(false, context.getString(R.string.enter_address))
        } else if (landmark.trim().isEmpty()) {
            callback.onResponse(false, context.getString(R.string.enter_landmark))
        } else if (area.trim().isEmpty()) {
            callback.onResponse(false, context.getString(R.string.enter_area))
        } else if (highway.trim().isEmpty()) {
            callback.onResponse(false, context.getString(R.string.enter_highway))
        } else if (pincode.trim().isEmpty()) {
            callback.onResponse(false, context.getString(R.string.enter_pincode))
        } else if (propertyStatus.trim().isEmpty()) {
            callback.onResponse(false, context.getString(R.string.select_property_status))
        } else if (images.trim().isEmpty()) {
            callback.onResponse(false, context.getString(R.string.select_dhaba_image))
        } else {
            callback.onResponse(true, "")
        }
    }

    companion object {
        val STATUS_PENDING = "Pending"
        val STATUS_INPROGRESS = "InProgess"
        val STATUS_ACTIVE = "Active"
        val STATUS_INACTIVE = "Inactive"
    }
}