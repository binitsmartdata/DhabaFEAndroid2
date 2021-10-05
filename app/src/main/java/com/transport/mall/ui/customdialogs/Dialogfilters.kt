package com.transport.mall.ui.customdialogs

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.transport.mall.R
import com.transport.mall.database.AppDatabase
import com.transport.mall.databinding.DialogFiltersBinding
import com.transport.mall.model.CityModel
import com.transport.mall.model.FiltersModel
import com.transport.mall.model.HighwayModel
import com.transport.mall.model.StateModel
import com.transport.mall.utils.common.GenericCallBack


class Dialogfilters constructor(
    fragment: Fragment,
    filterModel: FiltersModel,
    callBack: GenericCallBack<FiltersModel>
) : Dialog(fragment.activity as Context) {

    var binding: DialogFiltersBinding
    var cityList: ArrayList<CityModel> = ArrayList()
    var statesList: ArrayList<StateModel> = ArrayList()
    var selectedCities = ""
    var selectedStates = ""

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context), R.layout.dialog_filters, null, false
        )
        setContentView(binding.root)
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        window!!.setGravity(Gravity.BOTTOM)
        setCancelable(false)
        binding.filterModel = filterModel

        AppDatabase.getInstance(context)?.cityDao()
            ?.getAll()?.observe(fragment, Observer {
                cityList = it as ArrayList<CityModel>
            })
        AppDatabase.getInstance(context)?.statesDao()
            ?.getAll()?.observe(fragment, Observer {
                statesList = it as ArrayList<StateModel>
            })

        binding.tvCity.setOnClickListener {
            DialogCitySelection(context, cityList, filterModel.cities, GenericCallBack {
                selectedCities = ""
                val filteredCities: ArrayList<CityModel> = ArrayList()
                cityList.forEach {
                    if (it.isChecked) {
                        filteredCities.add(it)
                        selectedCities = if (selectedCities.isEmpty()) it.name_en!! else selectedCities + "," + it.name_en
                    }
                }
                if (filteredCities.isNotEmpty() && selectedCities.isNotEmpty()) {
                    binding.filterModel!!.cities = selectedCities
                } else {
                    binding.filterModel!!.cities = ""
                }
            }).show()
        }

        binding.tvState.setOnClickListener {
            DialogStateSelection(context, statesList, filterModel.states, GenericCallBack {
                selectedStates = ""
                val filteredCities: ArrayList<StateModel> = ArrayList()
                statesList.forEach {
                    if (it.isChecked) {
                        filteredCities.add(it)
                        selectedStates = if (selectedStates.isEmpty()) it.name_en!! else selectedStates + "," + it.name_en
                    }
                }
                if (filteredCities.isNotEmpty() && selectedStates.isNotEmpty()) {
                    binding.filterModel!!.states = selectedStates
                } else {
                    binding.filterModel!!.states = ""
                }
            }).show()
        }

        binding.tvHighway.setOnClickListener {
            AppDatabase.getInstance(context)?.highwayDao()?.getAll()
                ?.observe(fragment, {
                    DialogHighwaySelection(
                        context,
                        false,
                        it as ArrayList<HighwayModel>,
                        GenericCallBack {
                            binding.filterModel!!.highway = it.highwayNumber!!
                        }).show()
                })
        }

        binding.btnContinue.setOnClickListener {
            callBack.onResponse(filterModel)
            dismiss()
        }
        binding.btnReset.setOnClickListener {
            callBack.onResponse(FiltersModel())
            dismiss()
        }
        binding.ivClose.setOnClickListener {
            callBack.onResponse(filterModel)
            dismiss()
        }
    }
}