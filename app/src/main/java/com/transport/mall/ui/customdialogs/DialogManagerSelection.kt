package com.transport.mall.ui.customdialogs

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.transport.mall.R
import com.transport.mall.databinding.DialogOwnerSelectionBinding
import com.transport.mall.model.UserModel
import com.transport.mall.repository.networkoperator.ApiResult
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.GlobalUtils
import com.transport.mall.utils.common.infiniteadapter.InfiniteAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class DialogManagerSelection constructor(
    context: Context,
    val ownerId: String?,
    callBack: GenericCallBack<UserModel>
) : BaseDialog(context), SwipeRefreshLayout.OnRefreshListener, InfiniteAdapter.OnLoadMoreListener {

    var binding: DialogOwnerSelectionBinding
    var page = 1
    var searchString = ""
    var adapter: UserListAdapter? = null
    var dataList: ArrayList<UserModel> = ArrayList()

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context), R.layout.dialog_owner_selection, null, false
        )
        setContentView(binding.root)
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        window!!.setGravity(Gravity.BOTTOM)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.isHavingData = true
        setCancelable(false)

        binding.recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = UserListAdapter(context, dataList, GenericCallBack {
            callBack.onResponse(it)
            dismiss()
        })
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

        if (ownerId != null) {
            binding.edSearch.visibility = View.GONE
        }
    }

    private fun getOwnerList(callBack: GenericCallBack<List<UserModel>>) {
        if (page == 1) {
            binding.swipeRefreshLayout.isRefreshing = true
        }
        GlobalScope.launch(Dispatchers.Main) {
            try {
                executeApi(getApiService()?.getUserByRole(limit, page.toString(), "owner", searchString)).collect {
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

    private fun getManagersList(callBack: GenericCallBack<List<UserModel>>) {
        binding.swipeRefreshLayout.isRefreshing = true
        GlobalScope.launch(Dispatchers.Main) {
            try {
                executeApi(getApiService()?.getManagersByOwnerId(ownerId!!)).collect {
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
                            callBack.onResponse(it.data?.data)
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("GET MANAGERS :", e.toString())
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    fun refreshList() {
        if (ownerId != null) {
            adapter?.setShouldLoadMore(false)
            getManagersList(GenericCallBack {
                showData(it)
            })
        } else {
            getOwnerList(GenericCallBack {
                showData(it)
            })
        }
    }

    private fun showData(it: List<UserModel>) {
        if (it.isNotEmpty()) {
            if (page == 1) {
                if (ownerId==null) {
                    adapter?.setShouldLoadMore(true)
                }
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
        if (ownerId==null) {
            adapter?.removeLoadingView(dataList.size)
        }
        binding.isHavingData = dataList.isNotEmpty()
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