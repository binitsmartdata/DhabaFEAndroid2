package com.transport.mall.ui.customdialogs

import android.app.Dialog
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.transport.mall.R
import com.transport.mall.databinding.DialogCitySelectionBinding
import com.transport.mall.model.HighwayModel
import com.transport.mall.utils.common.GenericCallBack
import java.util.*
import kotlin.collections.ArrayList

class DialogHighwaySelection constructor(
    context: Context,
    dataList: ArrayList<HighwayModel>,
    val callBack: GenericCallBack<HighwayModel>
) : Dialog(context), GenericCallBack<HighwayModel> {

    var binding: DialogCitySelectionBinding
    var filterDataList: ArrayList<HighwayModel> = ArrayList()

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context), R.layout.dialog_city_selection, null, false
        )
        setContentView(binding.root)
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        binding.recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        binding.recyclerView.adapter = HighwayListAdapter(context, dataList, this@DialogHighwaySelection)
        binding.btnContinue.visibility = View.GONE
        binding.edSearch.hint = context.getString(R.string.search_highway)
        binding.isHavingData = dataList.isNotEmpty()
        binding.tvNoData.text = context.getString(R.string.no_highway_found)

        binding.edSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                filterDataList.clear()
                if (p0.toString().isNotEmpty()) {
                    for (model in dataList) {
                        if (model.highwayNumber?.toLowerCase(Locale.getDefault())?.contains(p0.toString().toLowerCase(Locale.getDefault()))!!) {
                            filterDataList.add(model)
                        }
                    }
                    binding.recyclerView.adapter = HighwayListAdapter(context, filterDataList, this@DialogHighwaySelection)
                    binding.isHavingData = filterDataList.isNotEmpty()
                } else {
                    filterDataList.clear()
                    binding.recyclerView.adapter = HighwayListAdapter(context, dataList, this@DialogHighwaySelection)
                    binding.isHavingData = dataList.isNotEmpty()
                }
            }
        })
    }

    override fun onResponse(output: HighwayModel?) {
        callBack.onResponse(output)
        dismiss()
    }
}