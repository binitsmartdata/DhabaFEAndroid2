package com.transport.mall.ui.customdialogs

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.transport.mall.R
import com.transport.mall.databinding.DialogDhabaSelectionBinding
import com.transport.mall.model.DhabaModelMain
import com.transport.mall.repository.networkoperator.ApiResult
import com.transport.mall.ui.home.dhabalist.DhabaListAdapterForDhabaSelection
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.GlobalUtils
import com.transport.mall.utils.common.infiniteadapter.InfiniteAdapter
import com.transport.mall.utils.common.localstorage.SharedPrefsHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class DialogDhabaSelection constructor(
    context: Context,
    callBack: GenericCallBack<DhabaModelMain>
) : BaseDialog(context), SwipeRefreshLayout.OnRefreshListener, InfiniteAdapter.OnLoadMoreListener {

    var binding: DialogDhabaSelectionBinding
    var page = 1
    var searchString = ""
    var adapter: DhabaListAdapterForDhabaSelection? = null
    var dataList: ArrayList<DhabaModelMain> = ArrayList()

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context), R.layout.dialog_dhaba_selection, null, false
        )
        setContentView(binding.root)
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        window!!.setGravity(Gravity.BOTTOM)
        binding.isHavingData = true
        setCancelable(false)

        binding.recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = DhabaListAdapterForDhabaSelection(context, dataList, null, {}, {}, {
            callBack.onResponse(dataList[it])
            dismiss()
        }, {})
        binding.recyclerView.adapter = adapter
        adapter?.setOnLoadMoreListener(this)

        binding.ivClose.setOnClickListener { dismiss() }
        binding.swipeRefreshLayout.setOnRefreshListener(this)

        binding.edSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchString = p0.toString().trim()
                if (searchString.isEmpty()) {
                    onRefresh()
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        binding.edSearch.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                GlobalUtils.hideKeyboard(context, binding.edSearch)
                if (binding.edSearch.text.toString().trim().isNotEmpty()) {
                    onRefresh()
                }
                return@OnEditorActionListener true
            }
            false
        })

        onRefresh()
    }

    private fun getOwnerList(callBack: GenericCallBack<ArrayList<DhabaModelMain>>) {
        if (page == 1) {
            binding.swipeRefreshLayout.isRefreshing = true
        }
        GlobalScope.launch(Dispatchers.Main) {
            try {
                executeApi(
                    getApiService()?.getAllOwnerDhabaList(
                        SharedPrefsHelper.getInstance(context).getUserData().accessToken,
                        limit,
                        page.toString(),
                        null,
                        null,
                        null,
                        null,
                        null,
                        searchString,
                        null
                    )
                ).collect {
                    when (it.status) {
                        ApiResult.Status.LOADING -> {
                            if (page == 1) {
                                binding.swipeRefreshLayout.isRefreshing = true
                            }
                        }
                        ApiResult.Status.ERROR -> {
                            binding.swipeRefreshLayout.isRefreshing = false
                            callBack.onResponse(ArrayList())
                        }
                        ApiResult.Status.SUCCESS -> {
                            binding.swipeRefreshLayout.isRefreshing = false
                            callBack.onResponse(it.data?.data?.data)
                        }
                    }
                }
            } catch (e: Exception) {
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    fun refreshList() {
        getOwnerList(GenericCallBack {
            if (it != null && it.isNotEmpty()) {
                if (page == 1) {
                    adapter?.setShouldLoadMore(true)
                    dataList.clear()
                    dataList.addAll(it)
                } else {
                    dataList.addAll(it)
                }
                adapter?.notifyDataSetChanged()
            } else {
                if (page == 1) {
                    dataList.clear()
//                    adapter?.notifyDataSetChanged()
                } else {
                    adapter?.setShouldLoadMore(false)
                }
            }
            adapter?.removeLoadingView(dataList.size)
            binding.isHavingData = dataList.isNotEmpty()
        })
    }

    override fun onRefresh() {
        page = 1
        refreshList()
    }

    override fun onLoadMore() {
        page += 1
        refreshList()
    }
}