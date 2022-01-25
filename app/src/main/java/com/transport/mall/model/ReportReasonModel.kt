package com.transport.mall.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ReportReasonModel : Serializable, BaseObservable() {
    @SerializedName(value = "_id")
    var _id: String = ""
        @Bindable get() = field
        set(_id) {
            field = _id
            notifyPropertyChanged(BR._id)
        }

    @SerializedName(value = "name")
    var name: String = ""
        @Bindable get() = field
        set(name) {
            field = name
            notifyPropertyChanged(BR.name)
        }

    override fun toString(): String {
        return name
    }


}