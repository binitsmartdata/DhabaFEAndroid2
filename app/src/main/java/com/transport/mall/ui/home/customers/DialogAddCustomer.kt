package com.transport.mall.ui.home.customers

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
import com.transport.mall.databinding.DialogAddCustomerBinding
import com.transport.mall.utils.common.GlobalUtils.showToastInCenter


class DialogAddCustomer constructor(context: Context) : Dialog(context) {

    var binding: DialogAddCustomerBinding

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context), R.layout.dialog_add_customer, null, false
        )
        setContentView(binding.root)
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window!!.getAttributes().windowAnimations = R.style.DialogAnimationBottom
        window!!.setGravity(Gravity.BOTTOM)

        binding.bSendInvitation.setOnClickListener {
            onButtonClick()
        }
    }

    private fun onButtonClick(){
        if(binding.etCustomerName.text?.trim().toString().isEmpty()){
            binding.etCustomerName.error = context.getString(R.string.please_enter_customer_name)
            return
        }
        if(binding.etCustomerContact.text?.trim().toString().isEmpty()){
            binding.etCustomerContact.error = context.getString(R.string.please_enter_customer_phone_number)
            return
        }

        dismiss()
        DialogSuccess(context, "An invitation link sent to " +
                binding.etCustomerName.text?.trim()).show()
    }
}