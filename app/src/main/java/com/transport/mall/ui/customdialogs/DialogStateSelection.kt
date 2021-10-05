package com.transport.mall.ui.customdialogs

import android.app.Dialog
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.transport.mall.R
import com.transport.mall.databinding.DialogCitySelectionBinding
import com.transport.mall.model.StateModel
import com.transport.mall.utils.common.GenericCallBack
import java.util.*
import kotlin.collections.ArrayList


class DialogStateSelection constructor(
    context: Context,
    dataList: ArrayList<StateModel>,
    selectecStates: String,
    callBack: GenericCallBack<ArrayList<StateModel>>
) : Dialog(context) {

    var binding: DialogCitySelectionBinding
    var filterDataList: ArrayList<StateModel> = ArrayList()

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context), R.layout.dialog_city_selection, null, false
        )
        setContentView(binding.root)
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        window!!.setGravity(Gravity.BOTTOM)
        binding.isHavingData = dataList.isNotEmpty()
        binding.edSearch.setHint(context.getString(R.string.searchState))
        setCancelable(false)

        //CHECK ALREADY SELECTED DATA
        val items: List<String> = selectecStates.split(",").map { it.trim() }
        for (item in items) {
            for (stateModel in dataList) {
                if (stateModel.name_en?.trim().equals(item.trim(), true)!!) {
                    stateModel.isChecked = true
                    break
                }
            }
        }

        binding.recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = StatesListAdapter(context, dataList)
        binding.btnContinue.setOnClickListener {
            val selectedCities = ArrayList<StateModel>()
            for (model in if (filterDataList.isNotEmpty()) filterDataList else dataList) {
                if (model.isChecked) {
                    selectedCities.add(model)
                }
            }
            callBack.onResponse(selectedCities)
            dismiss()
        }
        binding.edSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                filterDataList.clear()
                if (p0.toString().isNotEmpty()) {
                    for (model in dataList) {
                        if (model.name_en?.toLowerCase(Locale.getDefault())
                                ?.contains(p0.toString().toLowerCase(Locale.getDefault()))!!
                        ) {
                            filterDataList.add(model)
                        }
                    }
                    binding.recyclerView.adapter = StatesListAdapter(context, filterDataList)

                    binding.isHavingData = filterDataList.isNotEmpty()
                } else {
                    filterDataList.clear()
                    binding.recyclerView.adapter = StatesListAdapter(context, dataList)
                    binding.isHavingData = dataList.isNotEmpty()
                }
            }
        })
    }
}