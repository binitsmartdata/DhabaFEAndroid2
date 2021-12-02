package com.transport.mall.ui.authentication

import android.content.Context
import android.os.Build
import android.os.Bundle
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