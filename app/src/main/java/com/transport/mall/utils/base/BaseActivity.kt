package com.transport.mall.utils.base

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.androidadvance.topsnackbar.TSnackbar
import com.transport.mall.R
import com.transport.mall.ui.home.HomeActivity
import com.transport.mall.utils.common.localstorage.SharedPrefsHelper
import java.util.*


/**
 * Created by Vishal on 10/10/19.
 */

abstract class BaseActivity<myBinding : ViewDataBinding, V : ViewModel> : AppCompatActivity() {
    // since its going to be common for all the activities
    var mViewModel: ViewModel? = null
    lateinit var mContext: Context

    abstract val binding: myBinding

    lateinit var mViewBinding: ViewDataBinding

    /**
     * @return toolbar resource id
     */
    @get:LayoutRes
    abstract val layoutId: Int

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    abstract var viewModel: V

    /**
     *
     * @return context
     */
    protected abstract val context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = Color.WHITE
        }*/
        this.mViewBinding = DataBindingUtil.setContentView(this, layoutId)
        this.mContext = context
        this.mViewModel = viewModel
        bindData()
        initListeners()
    }

    override fun onStart() {
        super.onStart()
        refreshLanguagePreference()
    }

    override fun onRestart() {
        super.onRestart()
        refreshLanguagePreference()
    }

    abstract fun bindData()

    abstract fun initListeners()

    fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    /**
     * Common function for Set Up Binding ...
     */
    fun setUpBinding(): myBinding {
        return mViewBinding as myBinding
    }

    /**
     * Common function for Set Up ViewModel...
     * if no ViewModel Available then use BaseModel ...
     * You can also send parameters in constructor ...
     */
    fun setUpVM(activity: AppCompatActivity, obj: ViewModel): V {
        val provider = AppVMProvider()
        provider.createParams(obj)
        return ViewModelProvider(
            activity, provider
        ).get(obj::class.java) as V
    }


    /*fun performDataBinding(): ViewDataBinding {
        mViewDataBinding = DataBindingUtil.setContentView(this, layoutId)
        // this.mViewModel = if (mViewModel == null) getViewModel() else mViewModel
        // mViewDataBinding.setVariable(getBindingVariable(), mViewModel)
        //  mViewDataBinding.executePendingBindings()
        return mViewDataBinding
    }*/

    /* Method for showing SnackBar Alert ...
    *
    * @param view
    * @param message
    * @param isError
    */
    fun showSnackBar(view: View, message: String, isError: Boolean) {
        val snackBar = TSnackbar.make(view, message, TSnackbar.LENGTH_LONG)
        snackBar.setActionTextColor(Color.WHITE)
        val snackBarView = snackBar.view
        when (isError) {
            true -> snackBarView.setBackgroundColor(Color.BLACK)
            false -> snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    R.color.black
                )
            )
        }

//        val font = Typeface.createFromAsset(
//            assets,
//            "fonts/montserrat_regular.otf"
//        )

        val textView = snackBarView.findViewById<TextView>(R.id.snackbar_text)
        textView.setTextColor(Color.WHITE)
        textView.setPadding(
            0,
            (getStatusBarHeight() + resources.getDimension(R.dimen._30sdp).toInt()),
            0,
            0
        )
        textView.gravity = Gravity.CENTER
        //textView.typeface = font
        textView.textSize = resources.getDimension(R.dimen._5sdp)
        snackBar.show()
    }

    /**
     * Get Status Bar Height for the device ...
     * @return
     */
    fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    fun openFragmentReplace(id: Int, fragment: Fragment, tag: String, addToBack: Boolean) {
        when (addToBack) {
            true -> supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(
                    R.anim.enter_from_right,
                    R.anim.exit_to_left,
                    R.anim.enter_from_left,
                    R.anim.exit_to_right
                )
                .replace(id, fragment, tag)
                .addToBackStack(null)
                .commit()

            false -> supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(
                    R.anim.enter_from_right,
                    R.anim.exit_to_left,
                    R.anim.enter_from_left,
                    R.anim.exit_to_right
                )
                .replace(id, fragment, tag)
                .commit()
        }
    }

    fun openFragmentReplaceNoAnim(
        id: Int,
        fragment: Fragment,
        tag: String,
        addToBack: Boolean
    ) {
        when (addToBack) {
            true -> supportFragmentManager
                .beginTransaction()
                .replace(id, fragment, tag)
                .addToBackStack(null)
                .commit()

            false -> supportFragmentManager
                .beginTransaction()
                .replace(id, fragment, tag)
                .commit()
        }
    }

    open fun setLocale(languageCode: String?) {
        val config = resources.configuration
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            config.setLocale(locale)
        else
            config.locale = locale

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            createConfigurationContext(config)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    fun refreshLanguagePreference() {
        setLocale(SharedPrefsHelper.getInstance(this).getSelectedLanguage())
    }

    fun setLanguageToPunjabi() {
        SharedPrefsHelper.getInstance(this).setLanguageToPunjabi()
    }

    fun setLanguageToEnglish() {
        SharedPrefsHelper.getInstance(this).setLanguageToEnglish()
    }

    fun setLanguageToHindi() {
        SharedPrefsHelper.getInstance(this).setLanguageToHindi()
    }

    fun showToastInCenter(msg: String) {
        try {
            val toast = Toast.makeText(
                context,
                msg,
                Toast.LENGTH_LONG
            )
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        } catch (e: Exception) {
            val toast = Toast.makeText(context, msg.replace("\"".toRegex(), ""), Toast.LENGTH_LONG)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }
    }

    fun goToHomeScreen() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    /*Avoid showing blank activity when pressing back button.
    Take care while implementing onbackpressed in child activities*/
    override fun onBackPressed() {
        val fragments = supportFragmentManager.backStackEntryCount
        if (fragments == 1) {
            finish()
        } else if (supportFragmentManager.backStackEntryCount > 1) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

    private var mDialog: Dialog? = null
    fun showProgressDialog() {
        try {
            if (mDialog == null) {
                mDialog = Dialog(this, android.R.style.Theme_Translucent_NoTitleBar)
                var loaderView =
                    LayoutInflater.from(this)
                        .inflate(com.transport.mall.R.layout.loader, null)
                mDialog!!.setContentView(loaderView)
                mDialog!!.setCancelable(false)
                var tvProgressMessage =
                    loaderView.findViewById(com.transport.mall.R.id.tvProgressMessage) as TextView
                tvProgressMessage.visibility = View.GONE
                mDialog!!.show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun showProgressDialog(message: String?) {
        try {
            if (mDialog == null) {
                mDialog = Dialog(this, android.R.style.Theme_Translucent_NoTitleBar)
                var loaderView: View = LayoutInflater.from(this)
                    .inflate(com.transport.mall.R.layout.loader, null, false)
                mDialog!!.setContentView(loaderView)
                mDialog!!.setCancelable(false)

                var tvProgressMessage =
                    loaderView.findViewById(com.transport.mall.R.id.tvProgressMessage) as TextView
                tvProgressMessage.text = message
                tvProgressMessage.visibility = View.VISIBLE

                mDialog!!.show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun hideProgressDialog() {
        try {
            if (mDialog != null && mDialog!!.isShowing) {
                mDialog!!.dismiss()
                mDialog = null
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

