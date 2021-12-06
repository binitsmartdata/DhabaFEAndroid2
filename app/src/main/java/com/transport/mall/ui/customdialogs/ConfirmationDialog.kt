package com.transport.mall.ui.customdialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import com.transport.mall.R
import com.transport.mall.databinding.DialogConfirmationBinding
import com.transport.mall.utils.common.GenericCallBack


class ConfirmationDialog constructor(
    context: Context,
    message: String,
    callBack: GenericCallBack<Boolean>
) : Dialog(context) {

    var binding: DialogConfirmationBinding

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context), R.layout.dialog_confirmation, null, false
        )
        setContentView(binding.root)
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        window!!.getAttributes().windowAnimations = R.style.DialogAnimationBottom
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window!!.setGravity(Gravity.BOTTOM)
        binding.context = context
        binding.message = message
        binding.btnYes.setOnClickListener {
            callBack.onResponse(true)
            dismiss()
        }
        binding.btnNo.setOnClickListener {
            callBack.onResponse(false)
            dismiss()
        }
    }
}