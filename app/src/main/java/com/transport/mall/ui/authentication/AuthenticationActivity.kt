package com.transport.mall.ui.authentication

import android.content.Context
import com.afollestad.assent.Permission
import com.afollestad.assent.askForPermissions
import com.afollestad.assent.isAllGranted
import com.transport.mall.R
import com.transport.mall.databinding.ActivityAuthenticationBinding
import com.transport.mall.ui.authentication.pre_login.LanguageSelectionFragment
import com.transport.mall.utils.base.BaseActivity
import com.transport.mall.utils.base.BaseVM


/**
 * Created by Vishal Sharma on 2019-12-06.
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
        if (!isAllGranted(Permission.CAMERA, Permission.WRITE_EXTERNAL_STORAGE)) {
            askForPermissions(Permission.CAMERA, Permission.WRITE_EXTERNAL_STORAGE) { result ->
                // Check the result, see the Using Results section
                if (result.isAllGranted(Permission.CAMERA, Permission.WRITE_EXTERNAL_STORAGE)) {

                } else {

                }
            }
        }

        openFragmentReplaceNoAnim(R.id.authContainer, LanguageSelectionFragment(), "", true)
    }

    override fun initListeners() {


    }
}