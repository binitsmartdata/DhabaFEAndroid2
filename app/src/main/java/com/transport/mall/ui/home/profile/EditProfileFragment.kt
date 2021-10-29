package com.transport.mall.ui.home.helpline

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.Observer
import com.github.dhaval2404.imagepicker.ImagePicker
import com.transport.mall.R
import com.transport.mall.databinding.FragmentEditProfileBinding
import com.transport.mall.model.UserModel
import com.transport.mall.ui.home.profile.ProfileVM
import com.transport.mall.utils.base.BaseFragment
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.GlobalUtils
import com.transport.mall.utils.common.localstorage.SharedPrefsHelper

/**
 * Created by Parambir Singh on 2020-01-24.
 */
class EditProfileFragment : BaseFragment<FragmentEditProfileBinding, ProfileVM>() {
    override val layoutId: Int
        get() = R.layout.fragment_edit_profile
    override var viewModel: ProfileVM
        get() = setUpVM(this, ProfileVM(baseActivity.application))
        set(value) {}
    override var binding: FragmentEditProfileBinding
        get() = setUpBinding()
        set(value) {}

    var userData: UserModel? = null

    override fun bindData() {
        binding.lifecycleOwner = this
        setUserData()
    }

    private fun setUserData() {
        userData = SharedPrefsHelper.getInstance(activity as Context).getUserData()
        userData?.let {
            viewModel.userModel = it
            binding.userModel = viewModel.userModel
        }
        userData?.mobilePrefix?.let {
            if (it.isNotEmpty()) {
                try {
                    binding.ccpCountryCode.setCountryForPhoneCode(it.toInt())
                } catch (e: Exception) {
                    binding.ccpCountryCode.setCountryForPhoneCode(91)
                }
            }
        }
    }

    override fun initListeners() {
        binding.ccpCountryCode.isClickable = false
        binding.ccpCountryCode.setOnCountryChangeListener {
            viewModel.userModel.mobilePrefix = binding.ccpCountryCode.selectedCountryCode
        }

        viewModel.progressObserverUpdate.observe(this, Observer {
            if (it) {
                showProgressDialog(getString(R.string.updating_profile))
            } else {
                hideProgressDialog()
            }
        })

        binding.ivProfileImg.setOnClickListener {
            ImagePicker.with(this)
                .cropSquare()//Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                    1080,
                    1080
                )    //Final image resolution will be less than 1080 x 1080(Optional)
                .start()
        }

        binding.btnUpdateProfile.setOnClickListener {
            viewModel.userModel = binding.userModel as UserModel
            if (viewModel.userModel.ownerName.trim().isEmpty()) {
                showToastInCenter(getString(R.string.please_enter_name))
            } else if (viewModel.userModel.email.trim().isEmpty() || !GlobalUtils.isValidEmail(viewModel.userModel.email.trim())) {
                showToastInCenter(getString(R.string.enter_email_id))
            } else if (viewModel.userModel.mobile.trim().isEmpty()) {
                showToastInCenter(getString(R.string.enter_mobile_number))
            } else if (viewModel.userModel.mobile.trim().length < 10) {
                showToastInCenter(getString(R.string.enter_valid_mobile))
            } else {
                viewModel.userModel.mobilePrefix = binding.ccpCountryCode.selectedCountryCodeAsInt.toString()
                viewModel.updateUserProfile(GenericCallBack {
                    if (it.data != null) {
                        viewModel.userModel = it.data!!

                        SharedPrefsHelper.getInstance(getmContext()).setUserData(it.data!!)
                        showToastInCenter(getString(R.string.profile_updated))
                        goToHomeScreen()
                    } else {
                        showToastInCenter(it.message)
                    }
                })
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val uri: Uri = data?.data!!
            binding.ivProfileImg.setImageURI(uri)
            viewModel.userModel.ownerPic = getRealPathFromURI(uri)
        }
    }
}