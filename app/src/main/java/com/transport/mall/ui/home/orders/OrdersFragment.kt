package com.transport.mall.ui.home.orders

import android.app.Activity
import android.view.*
import com.transport.mall.R
import com.transport.mall.callback.CommonActivityListener
import com.transport.mall.databinding.FragmentOrdersBinding
import com.transport.mall.utils.base.BaseFragment
import com.transport.mall.utils.base.BaseVM
import com.transport.mall.utils.common.recyclerviewbase.RecyclerCallback

class OrdersFragment : BaseFragment<FragmentOrdersBinding, BaseVM>(),
    RecyclerCallback {
    override val layoutId: Int
        get() = R.layout.fragment_orders
    override var viewModel: BaseVM
        get() = setUpVM(this, BaseVM(baseActivity.application))
        set(value) {}
    override var binding: FragmentOrdersBinding
        get() = setUpBinding()
        set(value) {}

    var mCommonActivityListener: CommonActivityListener? = null

    override fun bindData() {
        setUpViewPager()
        setHasOptionsMenu(true)
    }

    private fun setUpViewPager(){
        binding.viewPager.offscreenPageLimit = 2
        val adapter = OrderViewPagerAdapter(requireFragmentManager())

        val fragment1 = NewOrdersFragment()
        val fragment2 = PastOrdersFragment()

        adapter.addFrag(fragment1, getString(R.string.new_orders))
        adapter.addFrag(fragment2, getString(R.string.past_orders))

        // set adapter on viewpager
        binding.viewPager.adapter = adapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }

    override fun initListeners() {
    }

    override fun onItemClick(view: View?, position: Int) {
    }

    override fun onChildItemClick(view: View?, parentIndex: Int, childIndex: Int) {
    }

    override fun itemAction(type: String?, position: Int) {
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_notification, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.actionNotification -> {
                mCommonActivityListener?.openNotificationFragment()
                return true
            }
        }
        return false
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        try {
            mCommonActivityListener = activity as CommonActivityListener
        } catch (e: ClassCastException) {
            throw ClassCastException(
                activity.toString()
                        + " must implement TransactionDataListener"
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
        mCommonActivityListener = null
    }

}