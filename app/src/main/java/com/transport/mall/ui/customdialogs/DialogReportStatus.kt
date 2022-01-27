package com.transport.mall.ui.customdialogs

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import com.transport.mall.R
import com.transport.mall.databinding.DialogReportStatusBinding


class DialogReportStatus constructor(
    context: Context,
    status: String
) : BaseDialog(context) {

    var binding: DialogReportStatusBinding
    var mobilePrefix = ""

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context), R.layout.dialog_report_status, null, false
        )
        setContentView(binding.root)
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        window!!.getAttributes().windowAnimations = R.style.DialogAnimationBottom
        window!!.setGravity(Gravity.BOTTOM)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val message =
            when (status) {
                "InReview" -> {
                    binding.ivIcon.setImageResource(R.drawable.ic_warning)
                    "Your report has been submitted you will be notified after we finished reviewing."
                }
                "Resolved" -> {
                    binding.ivIcon.setImageResource(R.drawable.ic_tick_step)
                    "Your report has been Resolved."
                }
                "Declined" -> {
                    binding.ivIcon.setImageResource(R.drawable.ic_cross)
                    "Your report has been Declined."
                }
                else -> {
                    binding.ivIcon.setImageResource(R.drawable.ic_warning)
                    "Your report has been submitted you will be notified after we finished reviewing."
                }
            }
        binding.tvStatusText.setText(message)
    }
}