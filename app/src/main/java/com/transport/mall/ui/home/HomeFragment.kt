package com.transport.mall.ui.home

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.lifecycle.Observer
import com.transport.mall.R
import com.transport.mall.callback.CommonActivityListener
import com.transport.mall.database.AppDatabase
import com.transport.mall.databinding.FragmentHomeBinding
import com.transport.mall.model.CityAndStateModel
import com.transport.mall.ui.addnewdhaba.AddDhabaActivity
import com.transport.mall.ui.home.dhabalist.DhabaListFragment
import com.transport.mall.ui.home.dhabalist.HomeViewPagerAdapter
import com.transport.mall.utils.base.BaseFragment


/**
 * Created by Parambir Singh on 2020-01-24.
 */
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeVM>() {
    override val layoutId: Int
        get() = R.layout.fragment_home
    override var viewModel: HomeVM
        get() = setUpVM(this, HomeVM(baseActivity.application))
        set(value) {}
    override var binding: FragmentHomeBinding
        get() = setUpBinding()
        set(value) {}

    var mCommonActivityListener: CommonActivityListener? = null

    override fun bindData() {
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
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
        AddDhabaActivity.start(activity as Context)
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