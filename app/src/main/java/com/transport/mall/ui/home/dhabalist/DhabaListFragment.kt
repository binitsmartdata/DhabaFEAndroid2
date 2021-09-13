package com.transport.mall.ui.home.dhabalist

import android.content.Context
import android.view.View
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.transport.mall.R
import com.transport.mall.databinding.FragmentDhabaListBinding
import com.transport.mall.model.CityAndStateModel
import com.transport.mall.model.DhabaModel
import com.transport.mall.ui.customdialogs.DialogCitySelection
import com.transport.mall.utils.base.BaseFragment
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.recyclerviewbase.RecyclerCallback


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

    val limit = "5"
    var page = 1

    override fun bindData() {
        binding.lifecycleOwner = this
        initDhabaListAdapter()
        refreshDhabaList()
        setHasOptionsMenu(true)
        setupCitySelectionViews()
    }

    private fun initDhabaListAdapter() {
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
                var selectedNames: String = ""
                cityAndStateList.forEach {
                    selectedNames =
                        if (selectedNames.isEmpty()) it.name?.en!! else selectedNames + ", " + it.name?.en
                }
                binding.autoTextSearch.setText(selectedNames)
            }).show()
        }
    }

    override fun initListeners() {
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
                binding.tvNoData.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
                if (page == 1) {
                    dhabaList.clear()
                }
                dhabaList.addAll(it)
                dhabaListAdapter?.notifyDataSetChanged()
            } else {
                if (page == 1) {
                    binding.tvNoData.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.GONE
                }
            }
        })
    }
}