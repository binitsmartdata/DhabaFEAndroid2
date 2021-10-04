package com.transport.mall.model

import android.content.Context
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import com.google.gson.annotations.SerializedName
import com.transport.mall.R
import com.transport.mall.utils.common.GenericCallBackTwoParams
import com.transport.mall.utils.common.GlobalUtils
import java.io.Serializable

/**
 * Created by Parambir Singh on 2020-01-24.
 */
class DhabaOwnerModel : Serializable, BaseObservable() {

    @SerializedName(value = "_id")
    var _id: String = ""
        @Bindable get() = field
        set(_id) {
            field = _id
            notifyPropertyChanged(BR._id)
        }

    @SerializedName(value = "ownerName", alternate = ["fname", "name"])
    var ownerName: String = ""
        @Bindable get() = field
        set(ownerName) {
            field = ownerName
            notifyPropertyChanged(BR.ownerName)
        }

    @SerializedName(value = "mobilePrefix")
    var mobilePrefix: String = ""
        @Bindable get() = field
        set(mobilePrefix) {
            field = mobilePrefix
            notifyPropertyChanged(BR.mobilePrefix)
        }

    @SerializedName(value = "mobile")
    var mobile: String = ""
        @Bindable get() = field
        set(mobile) {
            field = mobile
            notifyPropertyChanged(BR.mobile)
        }

    @SerializedName(value = "email")
    var email: String = ""
        @Bindable get() = field
        set(email) {
            field = email
            notifyPropertyChanged(BR.email)
        }

    @SerializedName(value = "address")
    var address: String = ""
        @Bindable get() = field
        set(address) {
            field = address
            notifyPropertyChanged(BR.address)
        }

    @SerializedName(value = "alternativeContactperson", alternate = ["alternativeContactPerson"])
    var alternativeContactperson: String = ""
        @Bindable get() = field
        set(alternativeContactperson) {
            field = alternativeContactperson
            notifyPropertyChanged(BR.alternativeContactperson)
        }

    @SerializedName(value = "alternatePhone")
    var alternatePhone: String = ""
        @Bindable get() = field
        set(alternatePhone) {
            field = alternatePhone
            notifyPropertyChanged(BR.alternatePhone)
        }

    @SerializedName(value = "alternativeMobilePrefix")
    var alternativeMobilePrefix: String = ""
        @Bindable get() = field
        set(alternativeMobilePrefix) {
            field = alternativeMobilePrefix
            notifyPropertyChanged(BR.alternativeMobilePrefix)
        }

    @SerializedName(value = "alternateDesignation")
    var alternateDesignation: String = ""
        @Bindable get() = field
        set(alternateDesignation) {
            field = alternateDesignation
            notifyPropertyChanged(BR.alternateDesignation)
        }

    @SerializedName(value = "panNumber")
    var panNumber: String = ""
        @Bindable get() = field
        set(panNumber) {
            field = panNumber
            notifyPropertyChanged(BR.panNumber)
        }

    @SerializedName(value = "adharCard", alternate = ["aadharNumber"])
    var adharCard: String = ""
        @Bindable get() = field
        set(adharCard) {
            field = adharCard
            notifyPropertyChanged(BR.adharCard)
        }

    @SerializedName(value = "ownerPic", alternate = ["profileImage"])
    var ownerPic: String = ""
        @Bindable get() = field
        set(ownerPic) {
            field = ownerPic
            notifyPropertyChanged(BR.ownerPic)
        }

    @SerializedName(value = "idproofFront")
    var idproofFront: String = ""
        @Bindable get() = field
        set(idproofFront) {
            field = idproofFront
            notifyPropertyChanged(BR.idproofFront)
        }

    @SerializedName(value = "idproofBack")
    var idproofBack: String = ""
        @Bindable get() = field
        set(idproofBack) {
            field = idproofBack
            notifyPropertyChanged(BR.idproofBack)
        }

    @SerializedName(value = "latitude")
    var latitude: String? = ""
        @Bindable get() = field
        set(latitude) {
            field = latitude
            notifyPropertyChanged(BR.latitude)
        }

    @SerializedName(value = "longitude")
    var longitude: String? = ""
        @Bindable get() = field
        set(longitude) {
            field = longitude
            notifyPropertyChanged(BR.longitude)
        }

    fun hasEverything(context: Context, callback: GenericCallBackTwoParams<Boolean, String>) {
        if (ownerName.trim().isEmpty()) {
            callback.onResponse(false, context.getString(R.string.enter_owner_name))
        } else if (mobile.trim().isEmpty() || mobile.trim().length < 10) {
            callback.onResponse(false, context.getString(R.string.enter_valid_mobile))
        } else if (email.trim().isEmpty()) {
            callback.onResponse(false, context.getString(R.string.enter_email))
        } else if (!GlobalUtils.isValidEmail(email)) {
            callback.onResponse(false, context.getString(R.string.enter_valid_email))
        } else if (panNumber.trim()
                .isNotEmpty() && panNumber.trim().length < 10
        ) { // NOT MANDATORY BUT IF HAS THEN IT SHOULD BE VALID
            callback.onResponse(false, context.getString(R.string.enter_valid_pan_number))
        } else if (adharCard.trim()
                .isNotEmpty() && adharCard.trim().length < 12
        ) { // NOT MANDATORY BUT IF HAS THEN IT SHOULD BE VALID
            callback.onResponse(false, context.getString(R.string.enter_valid_aadhar_number))
        } else if (alternatePhone.trim()
                .isNotEmpty() && alternatePhone.trim().length < 10
        ) { // NOT MANDATORY BUT IF HAS THEN IT SHOULD BE VALID
            callback.onResponse(false, context.getString(R.string.enter_valid_alt_number))
        } /*else if (alternateContactPerson.trim().isNotEmpty() && alternateContactPerson.trim().length < 12) {
            callback.onResponse(false, context.getString(R.string.enter_contact_person))
        } else if (alternatePhone.trim().isNotEmpty() && alternatePhone.trim().length < 12) {
            callback.onResponse(false, context.getString(R.string.enter_alternative_number))
        } else if (alternateDesignation.trim().isNotEmpty() && alternateDesignation.trim().length < 12) {
            callback.onResponse(false, context.getString(R.string.enter_alternative_contact_designation))
        } */ else {
            callback.onResponse(true, "")
        }
    }

    fun getMissingParameters(context: Context): String {
        var missingParams = ""
        if (ownerName.trim().isEmpty()) {
            missingParams = "Owner's Name"
        }
        if (mobile.trim().isEmpty() || mobile.trim().length < 10) {
            val param = "Owner's Mobile Number"
            missingParams = if (missingParams.isEmpty()) param else missingParams + ", " + param
        }
        if (email.trim().isEmpty() || !GlobalUtils.isValidEmail(email)) {
            val param = "Owner's Email ID"
            missingParams = if (missingParams.isEmpty()) param else missingParams + ", " + param
        }
        if (panNumber.trim().isNotEmpty() || panNumber.trim().length < 10) {
            val param = "Owner's Pan Number"
            missingParams = if (missingParams.isEmpty()) param else missingParams + ", " + param
        }
        if (adharCard.trim().isNotEmpty() || adharCard.trim().length < 12) {
            val param = "Owner's Aadhaar Number"
            missingParams = if (missingParams.isEmpty()) param else missingParams + ", " + param
        }
        if (ownerPic.trim().isEmpty()) {
            val param = "Owner's Picture"
            missingParams = if (missingParams.isEmpty()) param else missingParams + ", " + param
        }
        if (idproofFront.trim().isEmpty()) {
            val param = "Owner's ID Proof(Front)"
            missingParams = if (missingParams.isEmpty()) param else missingParams + ", " + param
        }
        if (idproofBack.trim().isEmpty()) {
            val param = "Owner's ID Proof(Back)"
            missingParams = if (missingParams.isEmpty()) param else missingParams + ", " + param
        }
        return missingParams
    }
}