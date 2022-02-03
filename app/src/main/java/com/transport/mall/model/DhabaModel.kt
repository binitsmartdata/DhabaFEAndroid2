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
    var dhabaUniqueId: String = ""
        @Bindable get() = field
        set(dhabaUniqueId) {
            field = dhabaUniqueId
            notifyPropertyChanged(BR.dhabaUniqueId)
        }

    fun getUniqueId(): String {
        return if (dhabaUniqueId.trim().isNotEmpty()) (dhabaUniqueId + " ") else ""
    }

    var overall_rating: Float = 0f
        @Bindable get() = field
        set(overall_rating) {
            field = overall_rating
            notifyPropertyChanged(BR.overall_rating)
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

    var manager_id: String = ""
        @Bindable get() = field
        set(manager_id) {
            field = manager_id
            notifyPropertyChanged(BR.manager_id)
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

    var informations: String = ""
        @Bindable get() = field
        set(informations) {
            field = informations
            notifyPropertyChanged(BR.informations)
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

    var propertyStatus: String = ""
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

    @SerializedName("imageList", alternate = ["holdingImg"])
    var imageList: ArrayList<PhotosModel> = ArrayList()
        @Bindable get() = field
        set(imageList) {
            field = imageList
            notifyPropertyChanged(BR.imageList)
        }

    var videos: String = ""
        @Bindable get() = field
        set(videos) {
            field = videos
            notifyPropertyChanged(BR.videos)
        }

    var executive_id: String = ""
        @Bindable get() = field
        set(executive_id) {
            field = executive_id
            notifyPropertyChanged(BR.executive_id)
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

    @SerializedName("isActive")
    var active: String? = "true"
        @Bindable get() = field
        set(active) {
            field = active
            notifyPropertyChanged(BR.active)
        }

    @SerializedName("isVerified")
    var verified: String? = "false"
        @Bindable get() = field
        set(verified) {
            field = verified
            notifyPropertyChanged(BR.verified)
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

    @SerializedName("isOpen24_7")
    var open247: Boolean? = true
        @Bindable get() = field
        set(open247) {
            field = open247
            notifyPropertyChanged(BR.open247)
        }

    var approval_for: String = ""
        @Bindable get() = field
        set(approval_for) {
            field = approval_for
            notifyPropertyChanged(BR.approval_for)
        }
    var approval_by: String = ""
        @Bindable get() = field
        set(approval_by) {
            field = approval_by
            notifyPropertyChanged(BR.approval_by)
        }
    var draft_by: String = ""
        @Bindable get() = field
        set(draft_by) {
            field = draft_by
            notifyPropertyChanged(BR.draft_by)
        }

    var dhabaObj: DhabaModel? = null

    var isDraft: String = ""

    fun isActiveDhaba(): Boolean {
        return getNonNullString(active, "true").toBoolean()
//        return false
    }

    fun hasEverything(context: Context, callback: GenericCallBackTwoParams<Boolean, String>) {
        if (name.trim().isEmpty()) {
            callback.onResponse(false, context.getString(R.string.enter_dhaba_name))
        }
        //this is mandatory while submit for approval
        /*else if ((latitude.trim().isEmpty() || longitude.trim().isEmpty())
            || (getNonNullString(latitude.trim(), "0").toDouble() == 0.toDouble()
                    || getNonNullString(longitude.trim(), "0").trim().toDouble() == 0.toDouble())
        ) {
            callback.onResponse(false, context.getString(R.string.choose_dhaba_location))
        } else if (address.trim().isEmpty()) {
            callback.onResponse(false, context.getString(R.string.enter_address))
        }*/
        /* else if (landmark.trim().isEmpty()) {
            callback.onResponse(false, context.getString(R.string.enter_landmark))
        } else if (area.trim().isEmpty()) {
            callback.onResponse(false, context.getString(R.string.enter_area))
        } else if (highway.trim().isEmpty()) {
            callback.onResponse(false, context.getString(R.string.enter_highway))
        } else if (pincode.trim().isEmpty()) {
            callback.onResponse(false, context.getString(R.string.enter_pincode))
        } else if (propertyStatus.trim().isEmpty()) {
            callback.onResponse(false, context.getString(R.string.select_property_status))
        } */
        //this is mandatory while submit for approval
        /*else if (images.isEmpty()) {
            callback.onResponse(false, context.getString(R.string.upload_dhaba_hoarding_pic))
        }*/ else {
            callback.onResponse(true, "")
        }
    }

    fun getMissingParameters(context: Context): String {
        var missingParams = ""
        if (name.trim().isEmpty()) {
            missingParams = context.getString(R.string._dhaba_name)
        }
        if ((latitude.trim().isEmpty() || longitude.trim().isEmpty())
            || (getNonNullString(latitude.trim(), "0").toDouble() == 0.toDouble()
                    || getNonNullString(longitude.trim(), "0").trim().toDouble() == 0.toDouble())
        ) {
            val param = context.getString(R.string.dhaba_location)
            missingParams = if (missingParams.isEmpty()) param else missingParams + "\n" + param
        }
        if (address.trim().isEmpty()) {
            val param = context.getString(R.string.dhaba_address)
            missingParams = if (missingParams.isEmpty()) param else missingParams + "\n" + param
        }
        /*if (landmark.trim().isEmpty()) {
            val param = context.getString(R.string.dhaba_address_landmark)
            missingParams = if (missingParams.isEmpty()) param else missingParams + "\n" + param
        }
        if (area.trim().isEmpty()) {
            val param = context.getString(R.string.dhaba_address_area)
            missingParams = if (missingParams.isEmpty()) param else missingParams + "\n" + param
        }
        if (highway.trim().isEmpty()) {
            val param = context.getString(R.string.highway)
            missingParams = if (missingParams.isEmpty()) param else missingParams + "\n" + param
        }
        if (state.trim().isEmpty()) {
            val param = context.getString(R.string.dhaba_state)
            missingParams = if (missingParams.isEmpty()) param else missingParams + "\n" + param
        }
        if (city.trim().isEmpty()) {
            val param = context.getString(R.string.dhaba_city)
            missingParams = if (missingParams.isEmpty()) param else missingParams + "\n" + param
        }
        if (pincode.trim().isEmpty()) {
            val param = context.getString(R.string.dhaba_pincode)
            missingParams = if (missingParams.isEmpty()) param else missingParams + "\n" + param
        }*/
        if (images.isEmpty()) {
            val param = context.getString(R.string.dhaba_hoarding_pic)
            missingParams = if (missingParams.isEmpty()) param else missingParams + "\n" + param
        }
/*
        if (videos.trim().isEmpty()) {
            val param = context.getString(R.string.dhaba_video)
            missingParams = if (missingParams.isEmpty()) param else missingParams + "\n" + param
        }
*/
        return missingParams
    }

    fun getPrimaryImage(): String {
        if (imageList != null && imageList.isNotEmpty()) {
            imageList.forEach {
                if (it.isActive && it.isPrimary) {
                    return it.path
                }
            }
            return images
        } else {
            return images
        }
    }

    companion object {
        public val STATUS_PENDING = "Pending"
        public val STATUS_INPROGRESS = "InProgess"
        public val STATUS_ACTIVE = "Active"
        public val STATUS_INACTIVE = "Inactive"
    }
}