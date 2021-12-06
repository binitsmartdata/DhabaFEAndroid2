package com.transport.mall.ui.customdialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
import com.transport.mall.model.CityModel
import com.transport.mall.utils.common.GenericCallBack
import java.util.*
import kotlin.collections.ArrayList


class DialogCitySelection constructor(
    context: Context,
    dataList: ArrayList<CityModel>,
    selectedCities: String,
    callBack: GenericCallBack<ArrayList<CityModel>>
) : Dialog(context, R.style.PauseDialog) {

    var binding: DialogCitySelectionBinding
    var filterDataList: ArrayList<CityModel> = ArrayList()

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context), R.layout.dialog_city_selection, null, false
        )
        setContentView(binding.root)
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        window!!.setGravity(Gravity.BOTTOM)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.isHavingData = dataList.isNotEmpty()
        setCancelable(false)

        //CHECK ALREADY SELECTED DATA
        val items: List<String> = selectedCities.split(",").map { it.trim() }
        for (item in items) {
            for (cityModel in dataList) {
                if (cityModel.name_en?.trim().equals(item.trim(), true)!!) {
                    cityModel.isChecked = true
                    break
                }
            }
        }

        binding.viewSpace.setOnClickListener {
            dismiss()
        }

        binding.recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = CityListAdapter(context, dataList)
        binding.btnContinue.setOnClickListener {
            val selectedCities = ArrayList<CityModel>()
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
                    binding.recyclerView.adapter = CityListAdapter(context, filterDataList)

                    binding.isHavingData = filterDataList.isNotEmpty()
                } else {
                    filterDataList.clear()
                    binding.recyclerView.adapter = CityListAdapter(context, dataList)
                    binding.isHavingData = dataList.isNotEmpty()
                }
            }
        })
    }
}