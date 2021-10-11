package com.getdishout.trxpay.ui.termsofservice

import android.app.Dialog
import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.databinding.DataBindingUtil
import com.transport.mall.R
import com.transport.mall.databinding.DialogTermsAndConditionsBinding

class DialogTermsAndConditions(context: Context, title: String, html: String) : Dialog(context) {

    var binding: DialogTermsAndConditionsBinding

    init {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_terms_and_conditions, null, false)
        setContentView(binding.root)
        setCancelable(false)
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        binding.txtvModifTitle.text = title

        /*val mimeType = "text/html"
        val encoding = "UTF-8"
        binding.tvTerms.loadDataWithBaseURL("", html, mimeType, encoding, "")*/
        binding.ivClose.setOnClickListener { dismiss() }
        binding.tvText.text = Html.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY)
        /*binding.tvTerms.post(Runnable {
            val childHeight: Int = binding.tvTerms.getHeight()
            val isScrollable: Boolean =
                binding.scrollView.getHeight() < childHeight + binding.scrollView.getPaddingTop() + binding.scrollView.getPaddingBottom()
            if (!isScrollable) { //Urrah! is scrollable
//                enableButtons(context, true)
            }
        })*/
    }
}