package com.transport.mall.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import java.io.Serializable

/**
 * Created by Parambir Singh on 2020-01-24.
 */
class SubmitForApprovalObservers : Serializable, BaseObservable() {
    companion object {
        val Status_SUCCESS = 1
        val Status_ERROR = -1
        val Status_LOADING = 0
    }

    var progressObserverOwner = Status_LOADING
        @Bindable get() = field
        set(progressObserverOwner) {
            field = progressObserverOwner
            notifyPropertyChanged(BR.progressObserverOwner)
        }

    var progressObserverDhaba = Status_LOADING
        @Bindable get() = field
        set(progressObserverDhaba) {
            field = progressObserverDhaba
            notifyPropertyChanged(BR.progressObserverDhaba)
        }

    var progressObserverDhabaTiming = Status_LOADING
        @Bindable get() = field
        set(progressObserverDhabaTiming) {
            field = progressObserverDhabaTiming
            notifyPropertyChanged(BR.progressObserverDhabaTiming)
        }

    var progressObserverFood = Status_LOADING
        @Bindable get() = field
        set(progressObserverFood) {
            field = progressObserverFood
            notifyPropertyChanged(BR.progressObserverFood)
        }

    var progressObserverParking = Status_LOADING
        @Bindable get() = field
        set(progressObserverParking) {
            field = progressObserverParking
            notifyPropertyChanged(BR.progressObserverParking)
        }

    var progressObserverSleeping = Status_LOADING
        @Bindable get() = field
        set(progressObserverSleeping) {
            field = progressObserverSleeping
            notifyPropertyChanged(BR.progressObserverSleeping)
        }

    var progressObserverWashroom = Status_LOADING
        @Bindable get() = field
        set(progressObserverWashroom) {
            field = progressObserverWashroom
            notifyPropertyChanged(BR.progressObserverWashroom)
        }

    var progressObserverSecurity = Status_LOADING
        @Bindable get() = field
        set(progressObserverSecurity) {
            field = progressObserverSecurity
            notifyPropertyChanged(BR.progressObserverSecurity)
        }

    var progressObserverLight = Status_LOADING
        @Bindable get() = field
        set(progressObserverLight) {
            field = progressObserverLight
            notifyPropertyChanged(BR.progressObserverLight)
        }

    var progressObserverOther = Status_LOADING
        @Bindable get() = field
        set(progressObserverOther) {
            field = progressObserverOther
            notifyPropertyChanged(BR.progressObserverOther)
        }

    var progressObserverBank = Status_LOADING
        @Bindable get() = field
        set(progressObserverBank) {
            field = progressObserverBank
            notifyPropertyChanged(BR.progressObserverBank)
        }
}