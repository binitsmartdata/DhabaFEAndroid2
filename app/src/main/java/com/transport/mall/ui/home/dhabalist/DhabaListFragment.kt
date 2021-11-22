package com.transport.mall.ui.home.dhabalist

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.transport.mall.R
import com.transport.mall.callback.CommonActivityListener
import com.transport.mall.databinding.FragmentDhabaListBinding
import com.transport.mall.model.DhabaModel
import com.transport.mall.model.DhabaModelMain
import com.transport.mall.model.FiltersModel
import com.transport.mall.ui.addnewdhaba.AddDhabaActivity
import com.transport.mall.ui.customdialogs.Dialogfilters
import com.transport.mall.ui.viewdhaba.ViewDhabaActivity
import com.transport.mall.utils.RxBus
import com.transport.mall.utils.base.BaseFragment
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.GlobalUtils
import com.transport.mall.utils.common.localstorage.SharedPrefsHelper


/**
 * Created by Parambir Singh on 2020-01-24.
 */
class DhabaListFragment(val status: String?) : BaseFragment<FragmentDhabaListBinding, DhabaListVM>(), SwipeRefreshLayout.OnRefreshListener {
    override val layoutId: Int
        get() = R.layout.fragment_dhaba_list
    override var viewModel: DhabaListVM
        get() = setUpVM(this, DhabaListVM(baseActivity.application))
        set(value) {}
    override var binding: FragmentDhabaListBinding
        get() = setUpBinding()
        set(value) {}

    private val dhabaList = ArrayList<DhabaModelMain>()
    var dhabaListAdapter: DhabaListAdapter? = null

    val limit = "10"
    var page = 1

    var mListener: CommonActivityListener? = null
    var filterModel = FiltersModel()

    init {
    }

    override fun bindData() {
        binding.lifecycleOwner = this
        binding.user = SharedPrefsHelper.getInstance(getmContext()).getUserData()
        mListener = activity as CommonActivityListener
        showOriginalList()
        setHasOptionsMenu(true)
        setupCitySelectionViews()
    }

    private fun initDhabaListAdapter(dhabaList: ArrayList<DhabaModelMain>) {
        dhabaListAdapter = DhabaListAdapter(activity as Context, dhabaList, { deletedDhaba -> // dhaba deletion listener
            viewModel.updateDhabaStatus(
                deletedDhaba.isDraft.toBoolean(),
                deletedDhaba,
                DhabaModel.STATUS_INACTIVE,
                viewModel.dialogProgressObserver,
                GenericCallBack {
                    onRefresh()
                })
        }, { editPosition -> // DHABA CLICKED LISTENER
            getSingleDhaba(dhabaList, editPosition) {
                AddDhabaActivity.startForUpdate(activity as Context, it)
            }
        }, { viewPosition -> // DHABA CLICKED LISTENER
            getSingleDhaba(dhabaList, viewPosition) {
                ViewDhabaActivity.start(activity as Context, it)
            }
        }, { sendForApprovalPosition -> // DHABA CLICKED LISTENER
            sendForApproval(dhabaList, sendForApprovalPosition)
        })
        dhabaListAdapter?.setOnLoadMoreListener {
            page++
            refreshDhabaList()
        }
        binding.recyclerView.layoutManager =
            LinearLayoutManager(activity as Context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = dhabaListAdapter
    }

    private fun sendForApproval(dhabaModelMain: ArrayList<DhabaModelMain>, position: Int) {
        val ownerMissingParams = dhabaModelMain[position].ownerModel?.getMissingParameters(getmContext()).toString()
        val dhabaMissingParams = dhabaModelMain[position].dhabaModel?.getMissingParameters(getmContext()).toString()
        if (ownerMissingParams.isNotEmpty() || dhabaMissingParams.isNotEmpty()) {
            // IF ALL THE PARAMETERS ARE NOT THERE THEN ASK USER TO FILL THEM
            GlobalUtils.showInfoDialog(getmContext(), getString(R.string.details_missing_validation),
                ownerMissingParams + "\n" + dhabaMissingParams,
                GenericCallBack {
                    getSingleDhaba(dhabaList, position) {
                        AddDhabaActivity.startForUpdate(activity as Context, it)
                    }
                })
        } else {
            // UPDATE DHABA STATUS TO IN PROGRESS(IN REVIEW)
            viewModel.updateDhabaStatus(false, dhabaModelMain[position].dhabaModel!!, DhabaModel.STATUS_INPROGRESS, viewModel.dialogProgressObserver, GenericCallBack {
                it.data?.let {
                    GlobalUtils.showToastInCenter(getmContext(), getString(R.string.sent_for_spproval))
                    dhabaModelMain[position].dhabaModel = it
                    dhabaListAdapter?.notifyDataSetChanged()
                }
            })
        }
    }

    private fun getSingleDhaba(dhabaList: ArrayList<DhabaModelMain>, editPosition: Int, callBack: GenericCallBack<DhabaModelMain>) {
        viewModel.dialogProgressObserver.value = true
        viewModel.getDhabaById(dhabaList.get(editPosition).dhabaModel?._id!!, GenericCallBack {
            viewModel.dialogProgressObserver.value = false
            if (it.data != null) {
                callBack.onResponse(it.data)
            } else {
                showToastInCenter(it.message)
            }
        })
    }

    private fun setupCitySelectionViews() {
        binding.tvCitySelection.setOnClickListener {
            Dialogfilters(this@DhabaListFragment, filterModel, GenericCallBack {
                filterModel = it
                onRefresh()
                if (filterModel.isHaveAnyFilter()) {
                    binding.viewCityIndicator.visibility = View.VISIBLE
                } else {
                    binding.viewCityIndicator.visibility = View.GONE
                }
            }).show()
        }
    }

    private fun showOriginalList() {
        initDhabaListAdapter(dhabaList)
        refreshDhabaList()
    }

    override fun initListeners() {
        //LISTENER TO LISTEN WHEN TO REFRESH THE DHABA LISTS.
        RxBus.listen(DhabaModel::class.java).subscribe {
            onRefresh()
        }

        setupSearchFieldListener()

        viewModel.dialogProgressObserver.observe(this, Observer {
            if (it) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        })
/*
        binding.btnAddDhaba.setOnClickListener {
            mListener?.openAddDhabaActivity()
        }
*/
        viewModel.progressObserver.observe(this, Observer {
            binding.swipeRefreshLayout.isRefreshing = it
        })
        binding.swipeRefreshLayout.setOnRefreshListener(this)
    }

    private fun setupSearchFieldListener() {
        binding.edSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().trim().isEmpty()) {
                    onRefresh()
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        binding.edSearch.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                GlobalUtils.hideKeyboard(getmContext(), binding.edSearch)
                if (binding.edSearch.text.toString().trim().isNotEmpty()) {
                    onRefresh()
                }
                return@OnEditorActionListener true
            }
            false
        })
    }

    private fun refreshDhabaList() {
        viewModel.getAllDhabaList(
            SharedPrefsHelper.getInstance(getmContext()).getUserData().accessToken,
            limit,
            page.toString(),
            filterModel,
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
        if (activity != null) {
            page = 1
            refreshDhabaList()
        }
    }

    override fun onResume() {
        super.onResume()
        Handler(Looper.getMainLooper()).postDelayed(Runnable { GlobalUtils.hideKeyboard(getmContext(), binding.edSearch) }, 100)
    }
}