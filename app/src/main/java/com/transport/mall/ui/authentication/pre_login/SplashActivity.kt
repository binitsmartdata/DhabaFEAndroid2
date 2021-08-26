package com.transport.mall.ui.authentication.pre_login

import android.content.Context
import android.content.Intent
import android.os.Handler
import com.transport.mall.R
import com.transport.mall.databinding.ActivitySplashBinding
import com.transport.mall.ui.authentication.AuthenticationActivity
import com.transport.mall.utils.base.BaseActivity
import com.transport.mall.utils.base.BaseVM


/**
 * Created by Vishal Sharma on 2019-12-06.
 */
class SplashActivity : BaseActivity<ActivitySplashBinding, BaseVM>() {
    override val binding: ActivitySplashBinding
        get() = setUpBinding()
    override val layoutId: Int
        get() = R.layout.activity_splash
    override var viewModel: BaseVM
        get() = setUpVM(
            this,
            BaseVM(application)
        )
        set(value) {}
    override val context: Context
        get() = this

    override fun bindData() {
        Handler().postDelayed(Runnable { startAuthActivity() }, 3000)
    }

    override fun initListeners() {

    }

    private fun startAuthActivity() {
        val intent = Intent(this, AuthenticationActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

}