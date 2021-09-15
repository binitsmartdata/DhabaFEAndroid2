package com.transport.mall.ui.home.dhabalist

import android.content.Context
import android.view.View
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.transport.mall.R
import com.transport.mall.callback.CommonActivityListener
import com.transport.mall.databinding.FragmentDhabaListBinding
import com.transport.mall.model.CityAndStateModel
import com.transport.mall.model.DhabaModel
import com.transport.mall.ui.customdialogs.DialogCitySelection
import com.transport.mall.utils.base.BaseFragment
import com.transport.mall.utils.common.GenericCallBack


/**
 * Created by Parambir Singh on 2020-01-24.
 */
class DhabaListFragment : BaseFragment<FragmentDhabaListBinding, DhabaListVM>() {
    override val layoutId: Int
        get() = R.layout.fragment_dhaba_list
    override var viewModel: DhabaListVM
        get() = setUpVM(this, DhabaListVM(baseActivity.application))
        set(value) {}
    override var binding: FragmentDhabaListBinding
        get() = setUpBinding()
        set(value) {}

    private val dhabaList = ArrayList<DhabaModel>()
    var cityAndStateList: ArrayList<CityAndStateModel> = ArrayList()
    var dhabaListAdapter: DhabaListAdapter? = null

    val limit = "20"
    var page = 1

    var mListener: CommonActivityListener? = null

    override fun bindData() {
        binding.lifecycleOwner = this
        mListener = activity as CommonActivityListener
        showOriginalList()
        setHasOptionsMenu(true)
        setupCitySelectionViews()
    }

    private fun initDhabaListAdapter(dhabaList: ArrayList<DhabaModel>) {
        dhabaListAdapter = DhabaListAdapter(activity as Context, dhabaList,
            GenericCallBack {

            })
        dhabaListAdapter?.setOnLoadMoreListener {
            page++
            refreshDhabaList()
        }
        binding.recyclerView.layoutManager =
            LinearLayoutManager(activity as Context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = dhabaListAdapter
    }

    private fun setupCitySelectionViews() {
        viewModel.getCitiesList(GenericCallBack {
            cityAndStateList = it

            var adapter = ArrayAdapter<CityAndStateModel>(
                activity as Context,
                android.R.layout.simple_list_item_1, cityAndStateList
            )
            binding.autoTextSearch.setAdapter(adapter)
            binding.autoTextSearch.setOnItemClickListener { adapterView, view, i, l ->
//                cityList[i].toString()
            }
        })

        binding.tvCitySelection.setOnClickListener {
            DialogCitySelection(activity as Context, cityAndStateList, GenericCallBack {
                var filteredCities: ArrayList<CityAndStateModel> = ArrayList()
                cityAndStateList.forEach {
                    if (it.isChecked) {
                        filteredCities.add(it)
                    }
                }
                if (filteredCities.isNotEmpty()) {
                    binding.viewCityIndicator.visibility = View.VISIBLE
                    showFilteredDhabas(filteredCities)
                } else {
                    binding.viewCityIndicator.visibility = View.GONE
                    showOriginalList()
                }
            }).show()
        }
    }

    private fun showOriginalList() {
        initDhabaListAdapter(dhabaList)
        refreshDhabaList()
    }

    override fun initListeners() {
        binding.btnAddDhaba.setOnClickListener {
            mListener?.openAddDhabaActivity()
        }
        viewModel.progressObserver.observe(this, Observer {
            binding.swipeRefreshLayout.isRefreshing = it
        })
        binding.swipeRefreshLayout.setOnRefreshListener {
            page = 1
            refreshDhabaList()
        }

        var list: Array<String> = resources.getStringArray(R.array.cities_list_dummy)
        var adapter = ArrayAdapter<String>(
            activity as Context,
            android.R.layout.simple_dropdown_item_1line, list
        )

        binding.autoTextSearch.setThreshold(1)
        //Set adapter to AutoCompleteTextView
        binding.autoTextSearch.setAdapter(adapter)

    }

    private fun refreshDhabaList() {
        viewModel.getAllDhabaList(limit, page.toString(), GenericCallBack {
            dhabaListAdapter?.removeLoadingView(dhabaList.size)
            if (it != null && it.isNotEmpty()) {
                if (page == 1) {
                    dhabaListAdapter?.setShouldLoadMore(true)
                    dhabaList.clear()
                    dhabaList.addAll(it)
                    initDhabaListAdapter(dhabaList)
                } else {
                    dhabaList.addAll(it)
                }
                dhabaListAdapter?.notifyDataSetChanged()
                refreshListAndNoDataView()
            } else {
                if (page == 1) {
                    refreshListAndNoDataView()
                } else {
                    dhabaListAdapter?.setShouldLoadMore(false)
                }
            }
        })
    }

    private fun showFilteredDhabas(filteredCities: ArrayList<CityAndStateModel>) {
        var filteredDhabaList = ArrayList<DhabaModel>()
        dhabaList.forEach { dhaba ->
            filteredCities.forEach { city ->
                if (dhaba.city.contains(city.name?.en.toString(), true)) {
                    filteredDhabaList.add(dhaba)
                }
            }
        }
        if (filteredDhabaList.isNotEmpty()) {
            initDhabaListAdapter(filteredDhabaList)
        }
    }

    fun refreshListAndNoDataView() {
        dhabaListAdapter?.dataList?.let {
            if (it.isNotEmpty()) {
                binding.llNoData.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
            } else {
                binding.llNoData.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            }
        }
    }
}