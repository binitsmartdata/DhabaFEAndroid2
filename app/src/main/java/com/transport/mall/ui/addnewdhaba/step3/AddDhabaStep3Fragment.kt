package com.transport.mall.ui.addnewdhaba.step1

import android.content.Context
import com.transport.mall.R
import com.transport.mall.databinding.FragmentAddDhabaStep3Binding
import com.transport.mall.ui.customdialogs.DialogAddDhabaSuccess
import com.transport.mall.utils.base.BaseFragment
import com.transport.mall.utils.common.GenericCallBack

/**
 * Created by Vishal Sharma on 2019-12-06.
 */
class AddDhabaStep3Fragment :
    BaseFragment<FragmentAddDhabaStep3Binding, AddDhabaStep2VM>() {
    override val layoutId: Int
        get() = R.layout.fragment_add_dhaba_step3
    override var viewModel: AddDhabaStep2VM
        get() = setUpVM(this, AddDhabaStep2VM(baseActivity.application))
        set(value) {}
    override var binding: FragmentAddDhabaStep3Binding
        get() = setUpBinding()
        set(value) {}

    override fun bindData() {
        binding.context = activity
    }

    override fun initListeners() {
        binding.btnSaveDhaba.setOnClickListener {
            DialogAddDhabaSuccess(
                activity as Context,
                "#LKJ534LK5J3L54J3L45",
                GenericCallBack {
                    when (it) {
                        DialogAddDhabaSuccess.SELECTED_ACTION.GO_HOME -> {
                            goToHomeScreen()
                        }
                        DialogAddDhabaSuccess.SELECTED_ACTION.VIEW_DHABA -> {

                        }
                    }
                }).show()
        }
    }
}