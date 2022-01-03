package com.transport.mall.ui.customdialogs

import android.content.Context
import com.transport.mall.R
import com.transport.mall.databinding.RowTimingListBinding
import com.transport.mall.model.DhabaTimingModel
import com.transport.mall.utils.common.GenericCallBackTwoParams
import com.transport.mall.utils.common.infiniteadapter.InfiniteAdapter
import java.text.SimpleDateFormat
import java.util.*

class TimingListAdapter(
    val context: Context, val dataList: List<DhabaTimingModel>,val viewOnly: Boolean
) : InfiniteAdapter<RowTimingListBinding>() {

    init {
        setShouldLoadMore(false)
    }

    override fun bindData(position: Int, myViewHolderG: MyViewHolderG?) {
        myViewHolderG?.binding?.context = context
        myViewHolderG?.binding?.model = dataList.get(position)

        if (!viewOnly) {
            myViewHolderG?.binding?.tvStartTime?.setOnClickListener {
                setOpeningPickerListener(position)
            }

            myViewHolderG?.binding?.tvEndTime?.setOnClickListener {
                setClosingPickerListener(position)
            }

            myViewHolderG?.binding?.flChecked?.setOnClickListener {
                dataList.get(position).isEnabled = !dataList.get(position).isEnabled
                notifyDataSetChanged()
            }
        }

        myViewHolderG?.binding?.executePendingBindings()

        val sdf = SimpleDateFormat("EEE")
        myViewHolderG?.binding?.today = sdf.format(Date())
    }

    private fun setClosingPickerListener(position: Int) {
        DailogTimePicker(context, GenericCallBackTwoParams { time, applyToAll ->
            dataList.get(position).closing = time
            if (applyToAll) {
                dataList.forEach {
                    it.closing = time
                    it.isEnabled = true
                }
            }
            notifyDataSetChanged()
        }).selectedTime(dataList.get(position).closing)
            .type(DailogTimePicker.Companion.Type.END_TIME)
            .startTime(dataList.get(position).opening)
            .show()
    }

    private fun setOpeningPickerListener(position: Int) {
        DailogTimePicker(context, GenericCallBackTwoParams { time, applyToAll ->
            dataList.get(position).opening = time
            if (applyToAll) {
                dataList.forEach {
                    it.opening = time
                    it.isEnabled = true
                }
            }
            notifyDataSetChanged()
        }).selectedTime(dataList.get(position).opening)
            .type(DailogTimePicker.Companion.Type.START_TIME)
            .endTime(dataList.get(position).closing)
            .show()
    }

    override fun getCount(): Int {
        return dataList.size
    }

    override fun getInflateLayout(type: Int): Int {
        return R.layout.row_timing_list
    }
}