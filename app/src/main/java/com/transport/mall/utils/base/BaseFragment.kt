package com.transport.mall.utils.base

import android.Manifest
import android.R
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.transport.mall.ui.home.HomeActivity
import com.transport.mall.utils.common.FilePath
import com.transport.mall.utils.common.localstorage.SharedPrefsHelper

abstract class BaseFragment<dataBinding : ViewDataBinding, viewModel : ViewModel> : Fragment() {
    // since its going to be common for all the activities
    private var mViewModel: ViewModel? = null
    lateinit var baseActivity: BaseActivity<dataBinding, viewModel>

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
    abstract var viewModel: viewModel

    abstract var binding: dataBinding

    lateinit var mViewDataBinding: ViewDataBinding

    private var mDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseActivity = activity as BaseActivity<dataBinding, viewModel>
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.mViewDataBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        this.mViewModel = viewModel
        bindData()
        initListeners()
        return mViewDataBinding.root
    }


    override fun onStart() {
        super.onStart()
    }

    protected abstract fun bindData()

    protected abstract fun initListeners()

    fun setUpBinding(): dataBinding {
        return mViewDataBinding as dataBinding
    }

    /**
     * Common function for Set Up ViewModel...
     * if no ViewModel Available then use BaseModel ...
     * You can also send parameters in constructor ...
     */
    fun setUpVM(fragment: Fragment, obj: ViewModel): viewModel {
        val provider = AppVMProvider()
        provider.createParams(obj)
        return ViewModelProvider(
            fragment, provider
        ).get(obj::class.java) as viewModel
    }

    fun setUpVM(activity: AppCompatActivity, obj: ViewModel): viewModel {
        val provider = AppVMProvider()
        provider.createParams(obj)
        return ViewModelProvider(
            activity, provider
        ).get(obj::class.java) as viewModel
    }

    fun showSnackBar(view: View, message: String, isError: Boolean) {
        baseActivity.showSnackBar(view, message, isError)
    }

    fun openFragmentReplace(id: Int, fragment: Fragment, tag: String, addToBack: Boolean) {
        baseActivity.openFragmentReplace(id, fragment, tag, addToBack)
    }

    fun openFragmentReplaceNoAnim(id: Int, fragment: Fragment, tag: String, addToBack: Boolean) {
        baseActivity.openFragmentReplaceNoAnim(id, fragment, tag, addToBack)
    }

    fun onBackPressed() {
        baseActivity.onBackPressed()
//        mContext.supportFragmentManager.popBackStack()
    }

    fun showProgressDialog() {
        try {
            if (mDialog == null) {
                mDialog = Dialog(activity as Context, R.style.Theme_Translucent_NoTitleBar)
                var loaderView =
                    LayoutInflater.from(activity as Context)
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
                mDialog = Dialog(activity as Context, R.style.Theme_Translucent_NoTitleBar)
                var loaderView: View = LayoutInflater.from(activity as Context)
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

    fun setLanguageToPunjabi() {
        SharedPrefsHelper.getInstance(activity as Context).setLanguageToPunjabi()
        if (activity is BaseActivity<*, *>)
            (activity as BaseActivity<*, *>).refreshLanguagePreference()
    }

    fun setLanguageToEnglish() {
        SharedPrefsHelper.getInstance(activity as Context).setLanguageToEnglish()
        if (activity is BaseActivity<*, *>)
            (activity as BaseActivity<*, *>).refreshLanguagePreference()
    }

    fun setLanguageToHindi() {
        SharedPrefsHelper.getInstance(activity as Context).setLanguageToHindi()
        if (activity is BaseActivity<*, *>)
            (activity as BaseActivity<*, *>).refreshLanguagePreference()
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
        try {
            HomeActivity.start(getmContext())
            activity?.finish()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    var INTENT_VIDEO_CAMERA = 111
    var INTENT_VIDEO_GALLERY = 222
    fun captureVideo(fragment: Fragment) {
        val takePictureIntent =
            Intent(MediaStore.ACTION_VIDEO_CAPTURE).putExtra(MediaStore.EXTRA_DURATION_LIMIT, 1000)
        try {
            startActivityForResult(takePictureIntent, INTENT_VIDEO_CAMERA)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }
    }

    fun pickVideoFromGallery(fragment: Fragment) {
        /*val item = VideoPickerItem().apply {
            showIcon = true
            sizeLimit = 100 * 1024 * 1024       // max. size in Bytes
            selectionMode = SelectionMode.Single  //Other modes are Single & Custom(limit:Int)
            gridDecoration = Triple(2, 20, true)    //(spanCount,spacing,includeEdge)
            showDuration = true
            selectionStyle = SelectionStyle.Large
        }
        EasyVideoPicker().startPickerForResult(this, item, INTENT_VIDEO_GALLERY)*/

        val mVideoUri = activity?.getContentResolver()?.insert(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            ContentValues()
        )
        val intent = Intent(Intent.ACTION_PICK)
        intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 150)
        intent.type = "video/*"
        fragment.startActivityForResult(Intent.createChooser(intent, "Select Video"), INTENT_VIDEO_GALLERY)
    }

    open fun getRealPathFromURI(contentUri: Uri?): String {
        if (contentUri?.isAbsolute == true) {
            return contentUri.path!!
        } else {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            val cursor: Cursor = activity?.managedQuery(contentUri, proj, null, null, null)!!
            val column_index: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            return cursor.getString(column_index)
        }
    }

    open fun getRealVideoPathFromURI(context: Context, contentUri: Uri): String? {
        return FilePath.getPath(context, contentUri)
    }

    fun getmContext(): Context {
        return activity as Context
    }

    fun requestLocationUpdates() {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity)
        val mLocationRequest: LocationRequest = LocationRequest.create()
        mLocationRequest.setInterval(60000)
        mLocationRequest.setFastestInterval(5000)
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        val mLocationCallback: LocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult == null) {
                    return
                }
                for (location in locationResult.getLocations()) {
                    if (location != null) {
                        fusedLocationProviderClient.removeLocationUpdates(this)
                    }
                }
            }
        }
        if (ActivityCompat.checkSelfPermission(getmContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                getmContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            return
        }
        fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null)
    }
}