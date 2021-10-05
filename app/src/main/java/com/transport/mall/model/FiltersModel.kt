package com.transport.mall.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import java.io.Serializable

class FiltersModel : Serializable, BaseObservable() {
    var cities = ""
        @Bindable get() = field
        set(cities) {
            field = cities
            notifyPropertyChanged(BR.cities)
        }

    var states = ""
        @Bindable get() = field
        set(states) {
            field = states
            notifyPropertyChanged(BR.states)
        }

    var highway = ""
        @Bindable get() = field
        set(highway) {
            field = highway
            notifyPropertyChanged(BR.highway)
        }

    var pincode = ""
        @Bindable get() = field
        set(pincode) {
            field = pincode
            notifyPropertyChanged(BR.pincode)
        }

    fun isHaveAnyFilter(): Boolean {
        if (cities.trim().isNotEmpty()) {
            return true
        }
        if (states.trim().isNotEmpty()) {
            return true
        }
        if (pincode.trim().isNotEmpty()) {
            return true
        }
        if (highway.trim().isNotEmpty()) {
            return true
        }
        return false
    }
}