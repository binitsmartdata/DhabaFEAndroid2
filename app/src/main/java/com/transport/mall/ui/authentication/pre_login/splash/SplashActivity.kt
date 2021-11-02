package com.transport.mall.ui.authentication.pre_login.splash

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.view.View
import com.transport.mall.R
import com.transport.mall.databinding.ActivitySplashBinding
import com.transport.mall.ui.authentication.AuthenticationActivity
import com.transport.mall.utils.base.BaseActivity
import com.transport.mall.utils.base.BaseVM
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.GlobalUtils
import com.transport.mall.utils.common.localstorage.SharedPrefsHelper


/**
 * Created by Parambir Singh on 2019-12-06.
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
        binding.lifecycleOwner = this
    }

    override fun initListeners() {
        viewModel.baseProgressOberver?.observe(this, {
            if (it) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        })

        viewModel.getLastSupportedVersion(mContext, GenericCallBack {
            it?.let {
                if (GlobalUtils.getNonNullString(it.lastSupportedVersion, "").isNotEmpty()) {
                    if (!GlobalUtils.isCurrentVersionSupported(this@SplashActivity, it.lastSupportedVersion!!)) {
                        GlobalUtils.showInfoDialog(this@SplashActivity, getString(R.string.force_update_message) + " " + getString(R.string.appName), false, GenericCallBack { isOk ->
                            it.reLoginRequired?.let {
                                if (it) {
                                    SharedPrefsHelper.getInstance(this@SplashActivity).clearData()
                                }
                            }

                            try {
//                                finish()
                                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
                            } catch (e: ActivityNotFoundException) {
                                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName")))
                            }
                        })
                    } else {
                        Handler().postDelayed(Runnable { startAuthActivity() }, 1000)
                    }
                }
            } ?: run {
                Handler().postDelayed(Runnable { startAuthActivity() }, 1000)
            }
        })
    }

    private fun startAuthActivity() {
        if (SharedPrefsHelper.getInstance(this).getUserData()._id.isNotEmpty()) {
            goToHomeScreen()
        } else {
            val intent = Intent(this, AuthenticationActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }

}