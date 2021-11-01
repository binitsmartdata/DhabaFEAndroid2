package com.transport.mall.database

import android.content.Context
import com.google.gson.annotations.SerializedName
import com.transport.mall.model.DhabaTimingModel
import java.io.Serializable

data class DhabaTimingModelParent(
    @SerializedName("dhabaId")
    var dhabaId: String,
    @SerializedName("timingArray")
    var timingArray: ArrayList<DhabaTimingModel>?
) : Serializable {
    fun validationMsg(context: Context): String {
        /*timingArray?.let {
            if (it.isNotEmpty()) {
                for (item in it) {
                    if (item.opening.trim().isEmpty() || item.closing.trim().isEmpty()) {
                        return context.getString(R.string.select_times_for_all_days)
                    }
                }
            } else {
                return context.getString(R.string.select_dhaba_timings)
            }
        }*/
        return ""
    }

    fun setAllTo24Hours() {
        timingArray?.clear()
        timingArray?.add(initTimingModel("12:00 AM", "11:59 PM", "monday", true))
        timingArray?.add(initTimingModel("12:00 AM", "11:59 PM", "tuesday", true))
        timingArray?.add(initTimingModel("12:00 AM", "11:59 PM", "wednesday", true))
        timingArray?.add(initTimingModel("12:00 AM", "11:59 PM", "thursday", true))
        timingArray?.add(initTimingModel("12:00 AM", "11:59 PM", "friday", true))
        timingArray?.add(initTimingModel("12:00 AM", "11:59 PM", "saturday", true))
        timingArray?.add(initTimingModel("12:00 AM", "11:59 PM", "sunday", true))
    }

    private fun initTimingModel(opening: String, closing: String, day: String, enabled: Boolean): DhabaTimingModel {
        val model = DhabaTimingModel()
        model.opening = opening
        model.closing = closing
        model.day = day
        model.isEnabled = enabled
        return model
    }
}