package com.transport.mall.ui.customdialogs

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import com.transport.mall.R
import com.transport.mall.databinding.FragmentLanguageSelectionBinding
import com.transport.mall.ui.authentication.pre_login.splash.SplashActivity
import com.transport.mall.ui.home.settings.SettingsFragment
import com.transport.mall.utils.common.localstorage.SharedPrefsHelper


class DialogLanguageSelection constructor(
    context: Context,
    fragment: SettingsFragment
) : Dialog(context) {

    var binding: FragmentLanguageSelectionBinding

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context), R.layout.fragment_language_selection, null, false
        )
        setContentView(binding.root)
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
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

        binding.flEnglish.setOnClickListener {
            binding.english = true
            binding.hindi = false
            binding.punjabi = false
            fragment.setLanguageToEnglish()
            restartApp(fragment)
        }
        binding.flHindi.setOnClickListener {
            binding.english = false
            binding.hindi = true
            binding.punjabi = false
            fragment.setLanguageToHindi()
            restartApp(fragment)
        }
        binding.flPunjabi.setOnClickListener {
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