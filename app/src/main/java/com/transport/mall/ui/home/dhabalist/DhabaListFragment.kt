package com.transport.mall.ui.home.dhabalist

import android.content.Context
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.TextView.OnEditorActionListener
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.transport.mall.R
import com.transport.mall.callback.CommonActivityListener
import com.transport.mall.database.AppDatabase
import com.transport.mall.databinding.FragmentDhabaListBinding
import com.transport.mall.model.CityModel
import com.transport.mall.model.DhabaModel
import com.transport.mall.model.DhabaModelMain
import com.transport.mall.ui.addnewdhaba.AddDhabaActivity
import com.transport.mall.ui.customdialogs.DialogCitySelection
import com.transport.mall.utils.base.BaseFragment
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.GlobalUtils
import com.transport.mall.utils.common.localstorage.SharedPrefsHelper


/**
 * Created by Parambir Singh on 2020-01-24.
 */
class DhabaListFragment(val status: String) : BaseFragment<FragmentDhabaListBinding, DhabaListVM>(), SwipeRefreshLayout.OnRefreshListener {
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

    val limit = "10"
    var page = 1
    var selectedCities = ""

    var mListener: CommonActivityListener? = null

    init {
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
                viewModel.dialogProgressObserver.value = true
                viewModel.getDhabaById(dhabaList.get(position).dhabaModel?._id!!, GenericCallBack {
                    viewModel.dialogProgressObserver.value = false
                    if (it.data != null) {
                        when (status) {
                            DhabaModel.STATUS_PENDING -> AddDhabaActivity.startForUpdate(activity as Context, it.data!!)
                            DhabaModel.STATUS_INPROGRESS -> AddDhabaActivity.startForUpdate(activity as Context, it.data!!)
                            DhabaModel.STATUS_ACTIVE -> AddDhabaActivity.startViewOnly(activity as Context, it.data!!)
                            DhabaModel.STATUS_INACTIVE -> AddDhabaActivity.startViewOnly(activity as Context, it.data!!)
                        }
                    } else {
                        showToastInCenter(it.message)
                    }
                })
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
            })

        binding.tvCitySelection.setOnClickListener {
            DialogCitySelection(activity as Context, cityList, GenericCallBack {
                selectedCities = ""
                var filteredCities: ArrayList<CityModel> = ArrayList()
                cityList.forEach {
                    if (it.isChecked) {
                        filteredCities.add(it)
                        selectedCities = if (selectedCities.isEmpty()) it.name_en!! else selectedCities + "," + it.name_en
                    }
                }
                if (filteredCities.isNotEmpty() && selectedCities.isNotEmpty()) {
                    binding.viewCityIndicator.visibility = View.VISIBLE
//                    showFilteredDhabas(filteredCities)
                    binding.edSearch.setText("")
                    onRefresh()
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
        binding.edSearch.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                GlobalUtils.hideKeyboard(getmContext(), binding.edSearch)
                if (binding.edSearch.text.toString().trim().isNotEmpty()) {
                    cityList.forEach { it.isChecked = false }
                    binding.viewCityIndicator.visibility = View.GONE
                    onRefresh()
                }
                return@OnEditorActionListener true
            }
            false
        })

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
        binding.swipeRefreshLayout.setOnRefreshListener(this)

        var list: Array<String> = resources.getStringArray(R.array.cities_list_dummy)
        var adapter = ArrayAdapter<String>(
            activity as Context,
            android.R.layout.simple_dropdown_item_1line, list
        )
    }

    private fun refreshDhabaList() {
        viewModel.getAllDhabaList(
            SharedPrefsHelper.getInstance(getmContext()).getUserData().accessToken,
            limit,
            page.toString(),
            selectedCities,
            binding.edSearch.text.toString(),
            status,
            GenericCallBack {
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
                        dhabaList.clear()
                        refreshListAndNoDataView()
                    } else {
                        dhabaListAdapter?.setShouldLoadMore(false)
                    }
                }
            })
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

    override fun onRefresh() {
        page = 1
        refreshDhabaList()
    }

    override fun onResume() {
        super.onResume()
        GlobalUtils.hideKeyboard(getmContext(), binding.edSearch)
    }
}