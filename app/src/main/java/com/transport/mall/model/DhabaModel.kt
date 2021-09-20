package com.transport.mall.model

import com.transport.mall.utils.common.GenericCallBackTwoParams
import java.io.Serializable

/**
 * Created by Parambir Singh on 2020-01-24.
 */
class DhabaModel : Serializable {
    var _id: String = "6137443bb5828a682d08ecf1"
    var status: String = ""
    var owner_id: String = ""
    var name: String = ""
    var ownerName: String = ""
    var address: String = ""
    var landmark: String = ""
    var area: String = ""
    var highway: String = ""
    var state: String = ""
    var city: String = ""
    var pincode: String = ""
    var location: String = ""
    var mobile: String = ""
    var propertyStatus: String = ""
    var images: String = ""
    var videos: String = ""
    var createdBy: String = ""
    var updatedBy: String = ""
    var isDeleted: Boolean = false
    var isActive: Boolean = true
    var latitude: String = ""
    var longitude: String = ""

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