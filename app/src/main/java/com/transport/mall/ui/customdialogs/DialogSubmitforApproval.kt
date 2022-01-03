package com.transport.mall.ui.customdialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.widget.ListAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.transport.mall.R
import com.transport.mall.databinding.DialogDropDownOptionsBinding
import com.transport.mall.databinding.DialogSubmitForApprovalBinding
import com.transport.mall.model.DhabaModelMain
import com.transport.mall.model.SubmitForApprovalObservers
import com.transport.mall.ui.addnewdhaba.step1.BankDetailsVM
import com.transport.mall.utils.common.GenericCallBack


class DialogSubmitforApproval constructor(
    context: Context,
    dhabaModelMain: DhabaModelMain,
    progressObserver: SubmitForApprovalObservers
) : Dialog(context) {

    var binding: DialogSubmitForApprovalBinding

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context), R.layout.dialog_submit_for_approval, null, false
        )
        setContentView(binding.root)
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
//        window!!.getAttributes().windowAnimations = R.style.DialogAnimationBottom
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        window!!.setGravity(Gravity.BOTTOM)
        binding.context = context

        binding.dhabaModelMain = dhabaModelMain
        binding.observer = progressObserver
    }
}