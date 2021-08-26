package com.transport.mall.ui.home.dhabalist

import android.content.Context
import android.view.View
import android.widget.ArrayAdapter
import com.transport.mall.R
import com.transport.mall.databinding.FragmentDhabaListBinding
import com.transport.mall.model.DhabaModel
import com.transport.mall.utils.base.BaseFragment
import com.transport.mall.utils.base.BaseVM
import com.transport.mall.utils.common.HomeActivityListener
import com.transport.mall.utils.common.recyclerviewbase.RecyclerBindingList
import com.transport.mall.utils.common.recyclerviewbase.RecyclerCallback


/**
 * Created by Vishal Sharma on 2020-01-24.
 */
class DhabaListFragment : BaseFragment<FragmentDhabaListBinding, BaseVM>(), RecyclerCallback {
    override val layoutId: Int
        get() = R.layout.fragment_dhaba_list
    override var viewModel: BaseVM
        get() = setUpVM(this, BaseVM(baseActivity.application))
        set(value) {}
    override var binding: FragmentDhabaListBinding
        get() = setUpBinding()
        set(value) {}

    private val bindList = RecyclerBindingList<DhabaModel>()
    var mActivityListener: HomeActivityListener? = null

    override fun bindData() {
        setupDhabaList()
        setHasOptionsMenu(true)
    }

    override fun initListeners() {
        var list: Array<String> = resources.getStringArray(R.array.cities_list_dummy)
        var adapter = ArrayAdapter<String>(
            activity as Context,
            android.R.layout.simple_dropdown_item_1line, list
        )

        binding.autoTextSearch.setThreshold(1)
        //Set adapter to AutoCompleteTextView
        binding.autoTextSearch.setAdapter(adapter)

    }

    private fun setupDhabaList() {
        val list = ArrayList<DhabaModel>()
        val menuArray = resources.getStringArray(R.array.dhaba_list)
        for (i in menuArray.indices) {
            list.add(DhabaModel(menuArray[i]))
        }
        bindList.itemsList = list
        binding.list = bindList
        binding.click = this
    }

    override fun onItemClick(view: View?, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onChildItemClick(view: View?, parentIndex: Int, childIndex: Int) {
        TODO("Not yet implemented")
    }

    override fun itemAction(type: String?, position: Int) {
        TODO("Not yet implemented")
    }
}