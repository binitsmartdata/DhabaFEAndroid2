package com.transport.mall.ui.addnewdhaba

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import com.transport.mall.R
import com.transport.mall.databinding.FragmentDhabaSuccessBinding
import com.transport.mall.model.UserModel
import com.transport.mall.utils.base.BaseFragment
import com.transport.mall.utils.base.BaseVM
import com.transport.mall.utils.common.GlobalUtils
import com.transport.mall.utils.common.localstorage.SharedPrefsHelper


/**
 * Created by Parambir Singh on 2020-01-24.
 */
class DhabaSuccessMessageFragment(val id: String) : BaseFragment<FragmentDhabaSuccessBinding, BaseVM>() {
    override val layoutId: Int
        get() = R.layout.fragment_dhaba_success
    override var viewModel: BaseVM
        get() = setUpVM(this, BaseVM(baseActivity.application))
        set(value) {}
    override var binding: FragmentDhabaSuccessBinding
        get() = setUpBinding()
        set(value) {}

    var userModel: UserModel? = UserModel()

    override fun bindData() {
        binding.lifecycleOwner = this
        setHasOptionsMenu(true)
    }

    override fun initListeners() {
        SharedPrefsHelper.getInstance(getmContext()).deleteDraftDhaba()
        if (id.length > 6) {
            binding.dhabaId = "************" + id.substring(id.length - 6)
        } else {
            binding.dhabaId = id
        }
        binding.btnViewDhaba.setOnClickListener {
            goToHomeScreen()
        }

        binding.tvDhabaId.setOnClickListener {
            val clipboard: ClipboardManager? =
                getmContext().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?
            val clip = ClipData.newPlainText("DHABA_ID", id)
            clipboard?.setPrimaryClip(clip)

            GlobalUtils.showToastInCenter(getmContext(), getmContext().getString(R.string.copied_to_clipboard))
        }
    }
}