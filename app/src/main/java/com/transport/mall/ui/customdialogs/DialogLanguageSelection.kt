package com.transport.mall.ui.customdialogs

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import com.transport.mall.R
import com.transport.mall.databinding.PopupShowChangeLanguageBinding
import com.transport.mall.ui.authentication.pre_login.splash.SplashActivity
import com.transport.mall.ui.home.settings.SettingsFragment
import com.transport.mall.utils.common.localstorage.SharedPrefsHelper


class DialogLanguageSelection constructor(
    context: Context,
    fragment: SettingsFragment
) : Dialog(context) {

    var binding: PopupShowChangeLanguageBinding

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context), R.layout.popup_show_change_language, null, false
        )
        setContentView(binding.root)
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        window!!.getAttributes().windowAnimations = R.style.DialogAnimationBottom
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window!!.setGravity(Gravity.BOTTOM)
        binding.context = context

        //setting already selected language
        when (SharedPrefsHelper.getInstance(context).getSelectedLanguage()) {
            "en" -> {
                binding.english = true
                binding.punjabi = false
                binding.hindi = false
            }
            "hi" -> {
                binding.english = false
                binding.hindi = true
                binding.punjabi = false
            }
            "pa" -> {
                binding.english = false
                binding.hindi = false
                binding.punjabi = true
            }
        }

        binding.tvEnglish.setOnClickListener {
            binding.english = true
            binding.hindi = false
            binding.punjabi = false
            fragment.setLanguageToEnglish()
            restartApp(fragment)
        }
        binding.tvHindi.setOnClickListener {
            binding.english = false
            binding.hindi = true
            binding.punjabi = false
            fragment.setLanguageToHindi()
            restartApp(fragment)
        }
        binding.tvPunjabi.setOnClickListener {
            binding.english = false
            binding.hindi = false
            binding.punjabi = true
            fragment.setLanguageToPunjabi()
            restartApp(fragment)
        }
    }

    private fun restartApp(fragment: SettingsFragment) {
        Handler().postDelayed(Runnable {
            val intent = Intent(context, SplashActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
            fragment.activity?.finish()
        }, 500)
    }
}