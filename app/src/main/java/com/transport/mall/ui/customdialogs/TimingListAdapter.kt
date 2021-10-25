package com.transport.mall.ui.customdialogs

import android.app.TimePickerDialog
import android.content.Context
import com.transport.mall.R
import com.transport.mall.databinding.RowTimingListBinding
import com.transport.mall.model.DhabaTimingModel
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.GlobalUtils
import com.transport.mall.utils.common.infiniteadapter.InfiniteAdapter
import java.text.SimpleDateFormat
import java.util.*

class TimingListAdapter(
    val context: Context, val dataList: List<DhabaTimingModel>
) : InfiniteAdapter<RowTimingListBinding>() {

    init {
        setShouldLoadMore(false)
    }

    override fun bindData(position: Int, myViewHolderG: MyViewHolderG?) {
        myViewHolderG?.binding?.context = context
        myViewHolderG?.binding?.model = dataList.get(position)

        myViewHolderG?.binding?.tvStartTime?.setOnClickListener {
            setOpeningPickerListener(position)
        }

        myViewHolderG?.binding?.tvEndTime?.setOnClickListener {
            setClosingPickerListener(position)
        }

        myViewHolderG?.binding?.executePendingBindings()
    }

    private fun setClosingPickerListener(position: Int) {
        pickEndTime(context, dataList.get(position).opening, GenericCallBack {
            if (it.isNotEmpty()) {
                dataList.get(position).closing = it
                notifyDataSetChanged()
            } else {
                setClosingPickerListener(position)
            }
        })
    }

    private fun setOpeningPickerListener(position: Int) {
        pickStartTime(context, dataList.get(position).closing, GenericCallBack {
            if (it.isNotEmpty()) {
                dataList.get(position).opening = it
                notifyDataSetChanged()
            } else {
                setOpeningPickerListener(position)
            }
        })
    }

    override fun getCount(): Int {
        return dataList.size
    }

    override fun getInflateLayout(type: Int): Int {
        return R.layout.row_timing_list
    }

    fun pickStartTime(context: Context, endTime: String, callBack: GenericCallBack<String>) {
        val cal = Calendar.getInstance()
        val sdf = SimpleDateFormat("h:mm a")
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)

            if (endTime.isNotEmpty()) {
                val date1 = sdf.parse(sdf.format(cal.time))
                val date2 = sdf.parse(endTime)

                if (date1.before(date2)) {
                    callBack.onResponse(sdf.format(cal.time))
                } else {
                    GlobalUtils.showToastInCenter(context, context.getString(R.string.opening_time_validation))
                    callBack.onResponse("")
                }
            } else {
                callBack.onResponse(sdf.format(cal.time))
            }
        }

        TimePickerDialog(
            context, timeSetListener, cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE), false
        ).show()
    }

    fun pickEndTime(context: Context, startTime: String, callBack: GenericCallBack<String>) {
        val cal = Calendar.getInstance()
        val sdf = SimpleDateFormat("h:mm a")
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)

            if (startTime.isNotEmpty()) {
                val date1 = sdf.parse(startTime)
                val date2 = sdf.parse(sdf.format(cal.time))

                if (date1.before(date2)) {
                    callBack.onResponse(sdf.format(cal.time))
                } else {
                    GlobalUtils.showToastInCenter(context, context.getString(R.string.closing_time_validation))
                    callBack.onResponse("")
                }
            } else {
                callBack.onResponse(sdf.format(cal.time))
            }
        }

        TimePickerDialog(
            context, timeSetListener, cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE), false
        ).show()
    }
}