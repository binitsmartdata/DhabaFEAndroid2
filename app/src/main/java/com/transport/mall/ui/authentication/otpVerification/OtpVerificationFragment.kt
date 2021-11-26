package com.transport.mall.ui.authentication.otpVerification

import android.app.Activity
import android.content.Context
import android.content.IntentFilter
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.auth.api.phone.SmsRetrieverClient
import com.google.android.gms.tasks.Task
import com.transport.mall.R
import com.transport.mall.databinding.FragmentOtpVerificationBinding
import com.transport.mall.model.UserModel
import com.transport.mall.utils.MySMSBroadcastReceiver
import com.transport.mall.utils.MySMSBroadcastReceiver.OTPReceiveListener
import com.transport.mall.utils.base.BaseFragment
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.localstorage.SharedPrefsHelper


/**
 * Created by Parambir Singh on 2019-12-06.
 */
class OtpVerificationFragment(val userModel: UserModel) : BaseFragment<FragmentOtpVerificationBinding, OtpVerificationVM>() {
    private val REQ_USER_CONSENT: Int = 123
    override val layoutId: Int
        get() = R.layout.fragment_otp_verification
    override var viewModel: OtpVerificationVM
        get() = setUpVM(this, OtpVerificationVM(baseActivity.application))
        set(value) {}
    override var binding: FragmentOtpVerificationBinding
        get() = setUpBinding()
        set(value) {}

    val minutesToWait: Long = (60000 * 3).toLong() // 3 minutes
    var otp = ""
    var mySMSBroadcastReceiver: MySMSBroadcastReceiver? = null

    override fun bindData() {
        binding.vm = viewModel
        viewModel.userModel = userModel
        var prefix = if (userModel.mobilePrefix.isNotEmpty()) "+" + userModel.mobilePrefix else ""
        binding.tvOtpSentTo.text = "$prefix ${userModel.mobile}"
        binding.btnLoginOwner.setOnClickListener {
            if (otp.isNotEmpty() && otp.length == 4) {
                viewModel.otp = otp
                viewModel.checkOtp(GenericCallBack {
                    it.data?.let {
                        SharedPrefsHelper.getInstance(getmContext()).setUserData(it)
                        goToHomeScreen()
                        activity?.finish()
                    } ?: kotlin.run {
                        showToastInCenter(it.message.toString())
                    }
                })
            } else {
                showToastInCenter(getString(R.string.enter_valid_otp))
            }
        }

        binding.btnResentOtp.setOnClickListener {
            viewModel.resendOtp(GenericCallBack { response ->
                response.data?.let {
                    showToastInCenter(getString(R.string.otp_sent))
                    miliseconds = 0
                    countDown()
                } ?: kotlin.run {
                    showToastInCenter(response.message.toString())
                }
            })
        }
    }

    override fun initListeners() {
        binding.otpView.setOtpCompletionListener {
            otp = it
            binding.otp = it
        }

        viewModel.progressObserverCityStates?.observe(this, Observer {
            if (it) {
                showProgressDialog(getString(R.string.fetching_states_cities_highways))
            } else {
                hideProgressDialog()
            }
        })
        viewModel.observerProgress()?.observe(this, Observer {
            if (it) {
                showProgressDialog(getString(R.string.verifying))
            } else {
                hideProgressDialog()
            }
        })
        viewModel.resendOtpObserver?.observe(this, Observer {
            if (it) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        })

        countDown()
        initSmsReceiver()

    }

    private fun initSmsReceiver() {
        startSMSRetrieverClient()

        mySMSBroadcastReceiver = MySMSBroadcastReceiver()
        activity?.registerReceiver(mySMSBroadcastReceiver, IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION))
        mySMSBroadcastReceiver?.init(object : OTPReceiveListener {
            override fun onOTPReceived(otp: String?) {
                Log.e("OTP RECEIVED :", otp.toString())
                binding.otpView.setText(otp)
                binding.btnLoginOwner.performClick()
            }

            override fun onOTPTimeOut() {
                Log.e("OTP TIMEOUT :", "TIMEOUT")
            }
        })
    }

    var miliseconds = 0
    private fun countDown() {
        if (miliseconds < minutesToWait) {
            if (activity != null) {
//                binding.btnResentOtp.isEnabled = true
//                binding.btnResentOtp.setTextColor(ContextCompat.getColor(getmContext(), R.color.black))
                Handler(Looper.getMainLooper()).postDelayed(Runnable {
                    activity?.let {
                        miliseconds += 1000
//                    var seconds = ((minutesToWait - miliseconds) / 1000).toString()
//                    binding.countDownTime = activity?.getString(R.string.resend_in) + " 00:${if (seconds.length > 1) seconds else "0" + seconds}"
                        val minutes = ((minutesToWait - miliseconds) / 1000 / 60).toString()
                        val seconds = (((minutesToWait - miliseconds) / 1000 % 60)).toString()

                        val minutesModified = if (minutes.length > 1) minutes else "0$minutes"
                        val secondsModified = if (seconds.length > 1) seconds else "0$seconds"
                        val countDownText = getString(R.string.otp_expires_in) + " " + minutesModified + ":" + secondsModified

                        binding.countDownTime = countDownText
                        binding.tvResendIn.visibility = View.VISIBLE
                        countDown()
                    }
                }, 1000)
            }
        } else {
            if (activity != null) {
//                binding.btnResentOtp.setTextColor(ContextCompat.getColor(getmContext(), R.color.black))
//                binding.btnResentOtp.isEnabled = true
                binding.tvResendIn.visibility = View.GONE
            }
        }
    }

    fun Context.showKeyboard(view: View?) {
        val imm = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInputFromWindow(view?.windowToken, 0, 0)
        view?.clearFocus()
    }

    private fun startSMSRetrieverClient() {
        val client: SmsRetrieverClient = SmsRetriever.getClient(activity)
        val task: Task<Void> = client.startSmsRetriever()
        task.addOnSuccessListener { aVoid -> Log.e("OTP SMS SUCCESS ::", "$aVoid") }
        task.addOnFailureListener { e -> Log.e("OTP SMS SUCCESS ::", "$e") }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mySMSBroadcastReceiver != null) activity?.unregisterReceiver(mySMSBroadcastReceiver)
    }
}