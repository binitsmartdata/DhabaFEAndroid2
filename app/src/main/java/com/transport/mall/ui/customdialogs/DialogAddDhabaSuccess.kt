package com.transport.mall.ui.customdialogs

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.Animatable
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import com.transport.mall.R
import com.transport.mall.databinding.DialogAddDhabaSuccessBinding
import com.transport.mall.utils.common.GenericCallBack


class DialogAddDhabaSuccess constructor(
    context: Context, id: String, callBack: GenericCallBack<SELECTED_ACTION>
) : Dialog(context) {

    enum class SELECTED_ACTION {
        VIEW_DHABA,
        GO_HOME
    }

    var binding: DialogAddDhabaSuccessBinding

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context), R.layout.dialog_add_dhaba_success, null, false
        )
        setContentView(binding.root)
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        binding.dhabaId = id
        binding.btnGoHome.setOnClickListener {
            callBack.onResponse(SELECTED_ACTION.GO_HOME)
            dismiss()
        }
        binding.btnViewDhaba.setOnClickListener {
            callBack.onResponse(SELECTED_ACTION.VIEW_DHABA)
            dismiss()
        }
        binding.ivTick.visibility = View.VISIBLE
        Handler().postDelayed(
            Runnable { (binding.ivTick.getDrawable() as Animatable).start() },
            300
        )
    }
}