package com.transport.mall.ui.home

import android.app.Activity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.viewpager.widget.ViewPager
import com.transport.mall.R
import com.transport.mall.callback.CommonActivityListener
import com.transport.mall.databinding.FragmentHomeBinding
import com.transport.mall.model.DhabaModel
import com.transport.mall.model.UserModel
import com.transport.mall.ui.home.dhabalist.DhabaListFragment
import com.transport.mall.ui.home.dhabalist.HomeViewPagerAdapter
import com.transport.mall.utils.base.BaseFragment
import com.transport.mall.utils.common.localstorage.SharedPrefsHelper


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
    var userModel: UserModel? = UserModel()

    override fun bindData() {
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        userModel = SharedPrefsHelper.getInstance(getmContext()).getUserData()
        binding.userModel = userModel
        setupViewPager()
        setHasOptionsMenu(true)
    }

    private fun setupViewPager() {
        binding.viewPager.offscreenPageLimit = 4
        binding.tabLayout.setupWithViewPager(binding.viewPager)
        val adapter = HomeViewPagerAdapter(
            childFragmentManager
        )

        val fragment1 = DhabaListFragment.newInstance(DhabaModel.STATUS_PENDING)
        val fragment2 = DhabaListFragment.newInstance(DhabaModel.STATUS_INPROGRESS)
        val fragment3 = DhabaListFragment.newInstance(DhabaModel.STATUS_ACTIVE)
        val fragment4 = DhabaListFragment.newInstance(DhabaModel.STATUS_INACTIVE)

        if (userModel?.isOwner()!! || userModel?.isManager()!!) {
            adapter.addFrag(DhabaListFragment.newInstance(null), getString(R.string.pending))
        } else {
            adapter.addFrag(fragment1, getString(R.string.pending))
            adapter.addFrag(fragment2, getString(R.string.in_review))
            adapter.addFrag(fragment3, getString(R.string.active))
            adapter.addFrag(fragment4, getString(R.string.inactive))
        }

        // set adapter on viewpager
        binding.viewPager.adapter = adapter
        if (!userModel?.isOwner()!! && !userModel?.isManager()!!) {
            binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {
                }

                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    when (position) {
                        0 -> fragment1.onFocused()
                        1 -> fragment2.onFocused()
                        2 -> fragment3.onFocused()
                        3 -> fragment4.onFocused()
                    }
                }

                override fun onPageSelected(position: Int) {
                }
            })
        }
    }

    override fun initListeners() {
        binding.tvAddNew.setOnClickListener {
            mCommonActivityListener?.openAddDhabaActivity()
        }
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