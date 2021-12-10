package com.transport.mall.ui.authentication

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import com.transport.mall.R
import com.transport.mall.databinding.ActivityAuthenticationBinding
import com.transport.mall.ui.authentication.pre_login.languageselection.LanguageSelectionFragment
import com.transport.mall.utils.base.BaseActivity
import com.transport.mall.utils.base.BaseVM


/**
 * Created by Parambir Singh on 2019-12-06.
 */
class AuthenticationActivity : BaseActivity<ActivityAuthenticationBinding, BaseVM>() {
    override val binding: ActivityAuthenticationBinding
        get() = setUpBinding()
    override val layoutId: Int
        get() = R.layout.activity_authentication
    override var viewModel: BaseVM
        get() = setUpVM(
            this,
            BaseVM(application)
        )
        set(value) {}
    override val context: Context
        get() = this

    override fun bindData() {
//        if (!SharedPrefsHelper.getInstance(this).getSelectedLanguage().isEmpty()) {
//            openFragmentReplaceNoAnim(R.id.authContainer, LoginFragment(), "LOGIN", true)
//        } else {
        openFragmentReplaceNoAnim(
            R.id.authContainer,
            LanguageSelectionFragment(),
            "LANGUAGE",
            true
        )
//        }
    }

    override fun initListeners() {
        /*if (resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK === Configuration.SCREENLAYOUT_SIZE_SMALL) {
            binding.frameMain.setBackgroundResource(R.drawable.bg_gradient)
            binding.ivDotLines.visibility = View.VISIBLE
        } else {
            binding.frameMain.setBackgroundResource(R.drawable.bg_splash)
            binding.ivDotLines.visibility = View.GONE
        }*/

        //Determine density
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        val density: Int = metrics.densityDpi

        if (density == DisplayMetrics.DENSITY_HIGH) {
            binding.frameMain.setBackgroundResource(R.drawable.bg_gradient)
            binding.ivDotLines.visibility = View.VISIBLE
        } else if (density == DisplayMetrics.DENSITY_MEDIUM) {
            binding.frameMain.setBackgroundResource(R.drawable.bg_gradient)
            binding.ivDotLines.visibility = View.VISIBLE
        } else if (density == DisplayMetrics.DENSITY_LOW) {
            binding.frameMain.setBackgroundResource(R.drawable.bg_splash)
            binding.ivDotLines.visibility = View.GONE
        } else {
            binding.frameMain.setBackgroundResource(R.drawable.bg_splash)
            binding.ivDotLines.visibility = View.GONE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        getWindow().requestFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

}