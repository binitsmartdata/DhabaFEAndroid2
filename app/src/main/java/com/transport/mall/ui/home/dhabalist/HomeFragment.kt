package com.transport.mall.ui.home.dhabalist

import android.app.Activity
import android.content.Intent
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.transport.mall.R
import com.transport.mall.databinding.FragmentHomeBinding
import com.transport.mall.ui.addnewdhaba.NewDhabaActivity
import com.transport.mall.ui.home.HomeActivity
import com.transport.mall.utils.base.BaseFragment
import com.transport.mall.utils.base.BaseVM
import com.transport.mall.utils.common.HomeActivityListener

/**
 * Created by Vishal Sharma on 2020-01-24.
 */
class HomeFragment : BaseFragment<FragmentHomeBinding, BaseVM>() {
    override val layoutId: Int
        get() = R.layout.fragment_home
    override var viewModel: BaseVM
        get() = setUpVM(this, BaseVM(baseActivity.application))
        set(value) {}
    override var binding: FragmentHomeBinding
        get() = setUpBinding()
        set(value) {}

    var mActivityListener: HomeActivityListener? = null

    override fun bindData() {
        setupViewPager()
        setHasOptionsMenu(true)
    }

    private fun setupViewPager() {
        binding.tabLayout.setupWithViewPager(binding.viewPager)
        val adapter = HomeViewPagerAdapter(childFragmentManager)

        // add your fragments
        adapter.addFrag(DhabaListFragment(), getString(R.string.pending))
        adapter.addFrag(DhabaListFragment(), getString(R.string.in_progress))
        adapter.addFrag(DhabaListFragment(), getString(R.string.active))
        adapter.addFrag(DhabaListFragment(), getString(R.string.inactive))

        // set adapter on viewpager
        binding.viewPager.adapter = adapter
    }

    override fun initListeners() {
        binding.tvAddNew.setOnClickListener {
            startAddDhabaActivity()
        }
    }

    private fun startAddDhabaActivity() {
        val intent = Intent(activity, NewDhabaActivity::class.java)
        activity?.startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_notification, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.actionNotification -> {
                mActivityListener?.openNotificationFragment()
                return true
            }
        }
        return false
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        try {
            mActivityListener = activity as HomeActivityListener
        } catch (e: ClassCastException) {
            throw ClassCastException(
                activity.toString()
                        + " must implement TransactionDataListener"
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
        mActivityListener = null
    }
}