package com.transport.mall.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class DhabaTimingModel : Serializable, BaseObservable() {
    @SerializedName("_id")
    var _id: String = ""
        @Bindable get() = field
        set(_id) {
            field = _id
            notifyPropertyChanged(BR._id)
        }

    @SerializedName("enabled")
    var enabled: Boolean = true
        @Bindable get() = field
        set(enabled) {
            field = enabled
            notifyPropertyChanged(BR.enabled)
        }

    @SerializedName("opening")
    var opening: String = ""
        @Bindable get() = field
        set(opening) {
            field = opening
            notifyPropertyChanged(BR.opening)
        }

    @SerializedName("closing")
    var closing: String = ""
        @Bindable get() = field
        set(closing) {
            field = closing
            notifyPropertyChanged(BR.closing)
        }

    @SerializedName("day")
    var day: String = ""
        @Bindable get() = field
        set(day) {
            field = day
            notifyPropertyChanged(BR.day)
        }


    override fun toString(): String {
        return day
    }
}