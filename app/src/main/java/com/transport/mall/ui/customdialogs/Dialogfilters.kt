package com.transport.mall.ui.customdialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
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
import com.transport.mall.utils.common.GlobalUtils


class Dialogfilters constructor(
    fragment: Fragment,
    filterModel: FiltersModel,
    callBack: GenericCallBack<FiltersModel>
) : Dialog(fragment.activity as Context, R.style.PauseDialog) {

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
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        window!!.setGravity(Gravity.BOTTOM)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCancelable(false)
        binding.filterModel = filterModel

        AppDatabase.getInstance(context)?.statesDao()
            ?.getAll()?.observe(fragment, Observer {
                statesList = it as ArrayList<StateModel>

                //MARK CHECKED PREVIOUSLY SELECTED DATA
                if (filterModel.states.trim().isNotEmpty()) {
                    val filteredCities: ArrayList<StateModel> = ArrayList()
                    //CHECK ALREADY SELECTED DATA
                    val items: List<String> = filterModel.states.split(",").map { it.trim() }
                    for (item in items) {
                        for (stateModel in statesList) {
                            if (stateModel.name_en?.trim().equals(item.trim(), true)!!) {
                                stateModel.isChecked = true
                                filteredCities.add(stateModel)
                                break
                            }
                        }
                    }

                    if (filteredCities.isNotEmpty()) {
                        populateCitiesByStates(fragment, filteredCities)
                    }
                }
            })

        binding.edState.setOnClickListener {
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
                    Log.e("SELECTED STATES :", selectedStates)
                    binding.filterModel!!.states = selectedStates
                    binding.filterModel!!.cities = ""
                    populateCitiesByStates(fragment, filteredCities)
                } else {
                    binding.filterModel!!.states = ""
                    binding.filterModel!!.cities = ""
                }
            }).show()
        }

        binding.edCity.setOnClickListener {
            if (binding.filterModel!!.states.trim().isNotEmpty()) {
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
                        Log.e("SELECTED CITIES :", selectedCities)
                        binding.filterModel!!.cities = selectedCities
                    } else {
                        binding.filterModel!!.cities = ""
                    }
                }).show()
            } else {
                GlobalUtils.showToastInCenter(context, context.getString(R.string.please_select_state_first))
            }
        }

        binding.edHighway.setOnClickListener {
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
        binding.viewSpace.setOnClickListener {
            dismiss()
        }

        binding.btnContinue.setOnClickListener {
            if (binding.filterModel!!.isHaveAnyFilter()) {
                callBack.onResponse(binding.filterModel!!)
                dismiss()
            } else {
                GlobalUtils.showToastInCenter(context,context.getString(R.string.filters_validation))
            }
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

    private fun populateCitiesByStates(fragment: Fragment, filteredCities: ArrayList<StateModel>) {
        AppDatabase.getInstance(context)?.cityDao()
            ?.getAll()?.observe(fragment, Observer {
                val allCities = it as ArrayList<CityModel>
                val citiesByStates = ArrayList<CityModel>()
                for (cityModel in allCities) {
                    for (states in filteredCities) {
                        if (states.stateCode.equals(cityModel.stateCode)) {
                            citiesByStates.add(cityModel)
                        }
                    }
                }
                if (citiesByStates.isNotEmpty()) {
                    cityList.clear()
                    cityList.addAll(citiesByStates)
                }
            })
    }
}