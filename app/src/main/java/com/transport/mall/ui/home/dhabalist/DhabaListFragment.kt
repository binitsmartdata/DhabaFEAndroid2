package com.transport.mall.ui.home.dhabalist

import android.content.Context
import android.view.View
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.transport.mall.R
import com.transport.mall.callback.CommonActivityListener
import com.transport.mall.database.AppDatabase
import com.transport.mall.databinding.FragmentDhabaListBinding
import com.transport.mall.model.CityModel
import com.transport.mall.model.DhabaModelMain
import com.transport.mall.ui.customdialogs.DialogCitySelection
import com.transport.mall.utils.base.BaseFragment
import com.transport.mall.utils.common.GenericCallBack


/**
 * Created by Parambir Singh on 2020-01-24.
 */
class DhabaListFragment(type: ListType) : BaseFragment<FragmentDhabaListBinding, DhabaListVM>() {
    override val layoutId: Int
        get() = R.layout.fragment_dhaba_list
    override var viewModel: DhabaListVM
        get() = setUpVM(this, DhabaListVM(baseActivity.application))
        set(value) {}
    override var binding: FragmentDhabaListBinding
        get() = setUpBinding()
        set(value) {}

    private val dhabaList = ArrayList<DhabaModelMain>()
    var cityList: ArrayList<CityModel> = ArrayList()
    var dhabaListAdapter: DhabaListAdapter? = null

    val limit = "20"
    var page = 1

    var mListener: CommonActivityListener? = null

    var listType: ListType

    init {
        listType = type
    }

    override fun bindData() {
        binding.lifecycleOwner = this
        mListener = activity as CommonActivityListener
        showOriginalList()
        setHasOptionsMenu(true)
        setupCitySelectionViews()
    }

    private fun initDhabaListAdapter(dhabaList: ArrayList<DhabaModelMain>) {
        dhabaListAdapter = DhabaListAdapter(activity as Context, dhabaList,
            GenericCallBack { position ->
/*
                viewModel.getDhabaById(dhabaList.get(position).dhabaModel?._id!!, GenericCallBack {
                    it.data?.let {
                        AddDhabaActivity.startForUpdate(activity as Context, it)
                    }
                })
*/
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
        AppDatabase.getInstance(getmContext())?.cityDao()
            ?.getAll()?.observe(this, Observer {
                cityList = it as ArrayList<CityModel>
                if (cityList.isNotEmpty()) {
                    setCitiesAdapter()
                }
            })

        binding.tvCitySelection.setOnClickListener {
            DialogCitySelection(activity as Context, cityList, GenericCallBack {
                var filteredCities: ArrayList<CityModel> = ArrayList()
                cityList.forEach {
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

    private fun setCitiesAdapter() {
        val adapter = ArrayAdapter<CityModel>(
            activity as Context,
            android.R.layout.simple_list_item_1, cityList
        )
        binding.autoTextSearch.setAdapter(adapter)
        binding.autoTextSearch.setOnItemClickListener { adapterView, view, i, l ->
            //                cityList[i].toString()
        }
    }

    private fun showOriginalList() {
        initDhabaListAdapter(dhabaList)
        refreshDhabaList()
    }

    override fun initListeners() {
        viewModel.dialogProgressObserver.observe(this, Observer {
            if (it) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        })
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
        if (listType == ListType.PENDING) {
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
        } else {
            refreshListAndNoDataView()
        }
    }

    private fun showFilteredDhabas(filteredCities: ArrayList<CityModel>) {
        var filteredDhabaList = ArrayList<DhabaModelMain>()
        dhabaList.forEach { dhaba ->
            filteredCities.forEach { city ->
                if (dhaba.dhabaModel?.city?.contains(city.name_en.toString(), true)!!) {
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