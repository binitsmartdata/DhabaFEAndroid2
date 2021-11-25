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
import com.transport.mall.R
import com.transport.mall.databinding.DialogDropDownOptionsBinding
import com.transport.mall.utils.common.GenericCallBack


class DialogDropdownOptions constructor(
    context: Context,
    title: String,
    adapter: ListAdapter, callBack: GenericCallBack<Int>
) : Dialog(context) {

    var binding: DialogDropDownOptionsBinding

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context), R.layout.dialog_drop_down_options, null, false
        )
        setContentView(binding.root)
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        window!!.getAttributes().windowAnimations = R.style.DialogAnimationBottom
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window!!.setGravity(Gravity.BOTTOM)
        binding.context = context
        binding.title = title

        binding.lvOptions.adapter = adapter
        binding.lvOptions.setOnItemClickListener { adapterView, view, i, l ->
            callBack.onResponse(i)
            dismiss()
        }
        binding.ivClose.setOnClickListener {
            dismiss()
        }
    }
}