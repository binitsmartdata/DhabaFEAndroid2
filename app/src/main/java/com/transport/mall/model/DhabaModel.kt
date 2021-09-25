package com.transport.mall.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import com.google.gson.annotations.SerializedName
import com.transport.mall.utils.common.GenericCallBackTwoParams
import java.io.Serializable

/**
 * Created by Parambir Singh on 2020-01-24.
 */
class DhabaModel : Serializable, BaseObservable() {

    var _id: String = "6137443bb5828a682d08ecf1"
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


    fun hasEverything(callback: GenericCallBackTwoParams<Boolean, String>) {
        if (name.isEmpty()) {
            callback.onResponse(false, "Please Enter Dhaba Name")
        } else if (address.isEmpty()) {
            callback.onResponse(false, "Please Enter Address")
        } else if (landmark.isEmpty()) {
            callback.onResponse(false, "Please Enter Landmark")
        } else if (area.isEmpty()) {
            callback.onResponse(false, "Please Enter Area Name")
        } else if (highway.isEmpty()) {
            callback.onResponse(false, "Please Enter Highway Name")
        } else if (pincode.isEmpty()) {
            callback.onResponse(false, "Please Enter Pincode")
        } else if (propertyStatus.isEmpty()) {
            callback.onResponse(false, "Please Select Property Status")
        } else if (images.isEmpty()) {
            callback.onResponse(false, "Please Select Dhaba Image")
        } /*else if (videos.isEmpty()) {
            callback.onResponse(false, "Please Select Dhaba Video")
        } */ else {
            callback.onResponse(true, "")
        }
    }

    companion object {
        val STATUS_PENDING = "Pending"
        val STATUS_INPROGRESS = "InProgress"
        val STATUS_ACTIVE = "Active"
        val STATUS_INACTIVE = "InActive"
    }
}