package com.transport.mall.ui.addnewdhaba.step1

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.github.dhaval2404.imagepicker.ImagePicker
import com.transport.mall.R
import com.transport.mall.callback.AddDhabaListener
import com.transport.mall.databinding.FragmentStep4BankDetailsBinding
import com.transport.mall.ui.customdialogs.DialogAddDhabaSuccess
import com.transport.mall.utils.base.BaseFragment
import com.transport.mall.utils.base.BaseVM
import com.transport.mall.utils.common.GenericCallBack

/**
 * Created by Parambir Singh on 2019-12-06.
 */
class Step4BankDetailsFragment :
    BaseFragment<FragmentStep4BankDetailsBinding, BaseVM>() {
    override val layoutId: Int
        get() = R.layout.fragment_step4_bank_details
    override var viewModel: BaseVM
        get() = setUpVM(this, BaseVM(baseActivity.application))
        set(value) {}
    override var binding: FragmentStep4BankDetailsBinding
        get() = setUpBinding()
        set(value) {}

    var mListener: AddDhabaListener? = null

    private val PICKER_OWNER_IMAGE = 1
    private val PICKER_ID_FRONT = 2
    private val PICKER_ID_BACK = 3
    private var INTENT_TYPE = 0

    override fun bindData() {
        binding.context = activity
        binding.lifecycleOwner = this

        mListener = activity as AddDhabaListener
    }

    override fun initListeners() {
        binding.btnNext.setOnClickListener {
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

    private fun launchImagePicker() {
        ImagePicker.with(this)
            .crop()
            .compress(1024)            //Final image size will be less than 1 MB(Optional)
            .maxResultSize(
                1080,
                1080
            )    //Final image resolution will be less than 1080 x 1080(Optional)
            .start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
//            val uri: Uri = data?.data!!
//            binding.ivOwnerImage.setImageURI(uri)
//            viewModel.ownerPic.set(if (uri.isAbsolute) uri.path else getRealPathFromURI(uri))
        }
    }
}