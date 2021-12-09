package com.transport.mall.ui.home.profile.owner

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.transport.mall.R
import com.transport.mall.callback.CommonActivityListener
import com.transport.mall.databinding.FragmentOwnerEditProfileBinding
import com.transport.mall.model.UserModel
import com.transport.mall.ui.home.dhabalist.HomeViewPagerAdapter
import com.transport.mall.ui.home.profile.owner.address.AddressFragment
import com.transport.mall.ui.home.profile.owner.basicdetails.BasicDetailsFragment
import com.transport.mall.utils.base.BaseFragment

/**
 * Created by Parambir Singh on 2020-01-24.
 */
class OwnerEditProfileFragment : BaseFragment<FragmentOwnerEditProfileBinding, OwnerProfileVM>() {
    override val layoutId: Int
        get() = R.layout.fragment_owner_edit_profile
    override var viewModel: OwnerProfileVM
        get() = setUpVM(this, OwnerProfileVM(baseActivity.application))
        set(value) {}
    override var binding: FragmentOwnerEditProfileBinding
        get() = setUpBinding()
        set(value) {}

    var userData: UserModel? = null
    var mListener: CommonActivityListener? = null

    override fun bindData() {
        setHasOptionsMenu(true)
        mListener = activity as CommonActivityListener
        binding.lifecycleOwner = this
        setupViewPager()

    }

    override fun initListeners() {
    }

    private fun setupViewPager() {
        binding.viewPager.offscreenPageLimit = 3
        binding.tabLayout.setupWithViewPager(binding.viewPager)
        val adapter = HomeViewPagerAdapter(
            childFragmentManager
        )
        adapter.addFrag(BasicDetailsFragment(), getString(R.string.basic_details))
        adapter.addFrag(AddressFragment(), getString(R.string.address))
//        adapter.addFrag(OwnerBankDetailsFragment(), getString(R.string.bank_details))

        // set adapter on viewpager
        binding.viewPager.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_notification, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.actionNotification -> {
                mListener?.openNotificationFragment()
                return true
            }
        }
        return false
    }
}