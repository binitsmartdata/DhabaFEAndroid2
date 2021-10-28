package com.transport.mall.database

import android.content.Context
import com.google.gson.annotations.SerializedName
import com.transport.mall.R
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
}