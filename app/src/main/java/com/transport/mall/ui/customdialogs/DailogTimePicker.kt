package com.transport.mall.ui.customdialogs

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import com.transport.mall.R
import com.transport.mall.databinding.DialogTimePickerBinding
import com.transport.mall.utils.common.GenericCallBackTwoParams
import com.transport.mall.utils.common.GlobalUtils
import java.text.SimpleDateFormat
import java.util.*


class DailogTimePicker constructor(context: Context, callBack: GenericCallBackTwoParams<String, Boolean>) : BaseDialog(context) {

    var binding: DialogTimePickerBinding
    var mobilePrefix = ""

    val cal = Calendar.getInstance()
    val sdf = SimpleDateFormat("h:mm a")

    var mtype: Type? = Type.NONE
    private lateinit var mStartTime: String
    private lateinit var mEndTime: String
    private lateinit var mSelectedTime: String

    companion object {
        enum class Type {
            START_TIME,
            END_TIME,
            NONE
        }
    }

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context), R.layout.dialog_time_picker, null, false
        )
        setContentView(binding.root)
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        window!!.getAttributes().windowAnimations = R.style.DialogAnimation;
        window!!.setGravity(Gravity.BOTTOM)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.timePicker.setOnTimeChangedListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
        }

        binding.btnSave.setOnClickListener {
            when (mtype) {
                Type.START_TIME -> {
                    if (mEndTime.isNotEmpty()) {
                        val date1 = sdf.parse(sdf.format(cal.time))
                        val date2 = sdf.parse(mEndTime)

                        if (date1.before(date2)) {
                            callBack.onResponse(sdf.format(cal.time), binding.chkAllDays.isChecked)
                            dismiss()
                        } else {
                            GlobalUtils.showToastInCenter(context, context.getString(R.string.opening_time_validation))
                        }
                    } else {
                        callBack.onResponse(sdf.format(cal.time), binding.chkAllDays.isChecked)
                        dismiss()
                    }
                }
                Type.END_TIME -> {
                    if (mStartTime.isNotEmpty()) {
                        val date1 = sdf.parse(mStartTime)
                        val date2 = sdf.parse(sdf.format(cal.time))

                        if (date1.before(date2)) {
                            callBack.onResponse(sdf.format(cal.time), binding.chkAllDays.isChecked)
                            dismiss()
                        } else {
                            GlobalUtils.showToastInCenter(context, context.getString(R.string.closing_time_validation))
                        }
                    } else {
                        callBack.onResponse(sdf.format(cal.time), binding.chkAllDays.isChecked)
                        dismiss()
                    }
                }
                Type.NONE -> {
                    callBack.onResponse(sdf.format(cal.time), binding.chkAllDays.isChecked)
                    dismiss()
                }
            }
        }
    }

    fun type(type: Type): DailogTimePicker {
        mtype = type
        return this
    }

    fun startTime(startTime: String): DailogTimePicker {
        mStartTime = startTime
        return this
    }

    fun endTime(endTime: String): DailogTimePicker {
        mEndTime = endTime
        return this
    }

    fun selectedTime(selectedTime: String): DailogTimePicker {
        mSelectedTime = selectedTime
        try {
            cal.time = sdf.parse(mSelectedTime)
            if (Build.VERSION.SDK_INT >= 23) {
                binding.timePicker.hour = cal.get(Calendar.HOUR_OF_DAY)
                binding.timePicker.minute = cal.get(Calendar.MINUTE)
            } else {
                binding.timePicker.currentHour = cal.get(Calendar.HOUR_OF_DAY)
                binding.timePicker.currentMinute = cal.get(Calendar.MINUTE)
            }
        } catch (e: Exception) {
            mSelectedTime = ""
        }

        return this
    }
}