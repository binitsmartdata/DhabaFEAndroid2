package com.transport.mall.ui.authentication.pre_login.splash

import android.app.ActivityOptions
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.Pair
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import com.transport.mall.R
import com.transport.mall.databinding.ActivitySplashBinding
import com.transport.mall.ui.authentication.AuthenticationActivity
import com.transport.mall.ui.home.HomeActivity
import com.transport.mall.utils.AppSignatureHelper
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

        val appSignatureHelper = AppSignatureHelper(this)
        Log.e("OTP SMS SIGNATURE ::", appSignatureHelper.appSignatures.toString())
    }

    override fun initListeners() {
        viewModel.getLastSupportedVersion(mContext, GenericCallBack {
            it?.let {
                if (GlobalUtils.getNonNullString(it.lastSupportedVersion, "").isNotEmpty()) {
                    if (!GlobalUtils.isCurrentVersionSupported(this@SplashActivity, it.lastSupportedVersion!!)) {
                        GlobalUtils.showCustomConfirmationDialog(
                            this@SplashActivity,
                            getString(R.string.force_update_message),
                            getString(R.string.update),
                            getString(R.string.cancel),
                            GenericCallBack { isYes ->
                                if (isYes) {
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
                                } else {
                                    finish()
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
//            goToHomeScreen()
            openActivityWithLogoAnimation(HomeActivity::class.java)
        } else {
            openActivityWithLogoAnimation(AuthenticationActivity::class.java)
        }
    }

    private fun openActivityWithLogoAnimation(target: Class<*>) {
        val intent = Intent(this, target)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        val options = ActivityOptions.makeSceneTransitionAnimation(
            this,
            Pair(binding.ivSplashLogo, "appLogo")
        )

        startActivity(intent, options.toBundle())
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            finish()
        }, 1500)
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