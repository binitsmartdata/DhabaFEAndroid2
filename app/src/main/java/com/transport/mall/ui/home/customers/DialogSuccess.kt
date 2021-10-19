package com.transport.mall.ui.home.customers

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import com.transport.mall.R
import com.transport.mall.databinding.DialogAddCustomerBinding
import com.transport.mall.databinding.DialogSuccessBinding
import com.transport.mall.utils.common.GlobalUtils.showToastInCenter


class DialogSuccess constructor(context: Context, message: String) : Dialog(context) {

    var binding: DialogSuccessBinding

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context), R.layout.dialog_success, null, false
        )
        setContentView(binding.root)
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.tvMessage.text = message
        binding.bOk.setOnClickListener {
            dismiss()
        }
    }
}