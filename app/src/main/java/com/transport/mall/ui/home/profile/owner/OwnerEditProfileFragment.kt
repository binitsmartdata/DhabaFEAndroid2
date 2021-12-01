package com.transport.mall.ui.home.profile.owner

import com.transport.mall.R
import com.transport.mall.databinding.FragmentOwnerEditProfileBinding
import com.transport.mall.model.UserModel
import com.transport.mall.ui.home.dhabalist.HomeViewPagerAdapter
import com.transport.mall.ui.home.profile.owner.address.AddressFragment
import com.transport.mall.ui.home.profile.owner.bankdetails.OwnerBankDetailsFragment
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

    override fun bindData() {
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
}