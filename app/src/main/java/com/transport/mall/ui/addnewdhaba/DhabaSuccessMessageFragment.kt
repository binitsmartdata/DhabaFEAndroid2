package com.transport.mall.ui.addnewdhaba

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.transport.mall.R
import com.transport.mall.databinding.FragmentDhabaSuccessBinding
import com.transport.mall.model.UserModel
import com.transport.mall.utils.base.BaseFragment
import com.transport.mall.utils.base.BaseVM
import com.transport.mall.utils.common.GenericCallBack
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
    var dialogProgressObserver: MutableLiveData<Boolean> = MutableLiveData()

    override fun bindData() {
        binding.lifecycleOwner = this
        setHasOptionsMenu(true)

        dialogProgressObserver.observe(this, Observer {
            if (it) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        })

    }

    override fun initListeners() {
        SharedPrefsHelper.getInstance(getmContext()).deleteDraftDhaba()
        if (id.length > 6) {
            binding.dhabaId = "************" + id.substring(id.length - 6)
        } else {
            binding.dhabaId = id
        }
        binding.btnViewDhaba.setOnClickListener {
            dialogProgressObserver.value = true
            viewModel.getDhabaById(id, GenericCallBack {
                dialogProgressObserver.value = false
                if (it.data != null) {
                    context?.let { context ->
                        AddDhabaActivity.startForUpdate(context, it.data!!)
                        activity?.finish()
                    }
                } else {
                    showToastInCenter(it.message)
                }
            })
        }
        binding.btnGoHome.setOnClickListener {
            goToHomeScreen()
            activity?.finish()
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