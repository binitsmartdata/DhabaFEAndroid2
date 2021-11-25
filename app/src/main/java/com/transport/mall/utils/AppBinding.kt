package com.transport.mall.utils

import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Typeface
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import com.afollestad.assent.Permission
import com.afollestad.assent.askForPermissions
import com.afollestad.assent.isAllGranted
import com.afollestad.assent.runWithPermissions
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import com.squareup.picasso.Picasso
import com.transport.mall.R
import com.transport.mall.callback.PermissionCallback
import com.transport.mall.utils.base.BaseActivity
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import java.io.File


/**
 * Picasso for image loading ...
 */
@BindingAdapter("loadImage", "placeholder")
fun xloadImages(
    view: ImageView?,
    image: String?,
    placeHolder: Int
) {
    val isSpecifiedPlaceholder = when (placeHolder) {
        R.drawable.ic_profile_pic_placeholder -> true
        R.drawable.ic_id_front -> true
        R.drawable.ic_id_back -> true
        else -> false
    }

    image?.let {
        if (it.contains("http")) {
            Picasso.get()
                .load(it)
                .error(if (isSpecifiedPlaceholder) placeHolder else R.drawable.ic_launcher_background)
                .placeholder(placeHolder)
                .into(view)
        } else {
            val file = File(it)
            Picasso.get()
                .load(file)
                .error(placeHolder)
                .placeholder(placeHolder)
                .into(view)
        }
    } ?: run {
        Picasso.get()
            .load(R.drawable.ic_image_placeholder)
            .error(if (isSpecifiedPlaceholder) placeHolder else R.drawable.ic_launcher_background)
            .placeholder(R.drawable.ic_image_placeholder)
            .into(view)
    }
}

/**
 * Picasso for image loading ...
 */

@BindingAdapter("loadImageRoundCorners", "placeholder")
fun loadImageRoundCorners(view: ImageView?, image: String?, placeHolder: Int) {
    image?.let {
        if (it.contains("http")) {
            Picasso.get()
                .load(it)
                .error(if (placeHolder == R.drawable.ic_profile_pic_placeholder) placeHolder else R.drawable.ic_launcher_background)
                .placeholder(placeHolder)
                .transform(RoundedCornersTransformation(50, 50, RoundedCornersTransformation.CornerType.ALL))
                .into(view)
        } else {
            val file = File(it)
            Picasso.get()
                .load(file)
                .error(placeHolder)
                .placeholder(placeHolder)
                .transform(RoundedCornersTransformation(50, 50, RoundedCornersTransformation.CornerType.ALL))
                .into(view)
        }
    } ?: run {
        Picasso.get()
            .load(R.drawable.ic_image_placeholder)
            .error(if (placeHolder == R.drawable.ic_profile_pic_placeholder) placeHolder else R.drawable.ic_launcher_background)
            .placeholder(R.drawable.ic_image_placeholder)
            .transform(RoundedCornersTransformation(50, 50, RoundedCornersTransformation.CornerType.ALL))
            .into(view)
    }
}

/**
 * Function for Single Permission ...
 */
fun initSinglePermission(activity: Activity, permission: String, callback: PermissionCallback) {
    Dexter.withActivity(activity)
        .withPermission(permission)
        .withListener(object : PermissionListener {
            override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                callback.onPermissionGranted()
            }

            override fun onPermissionRationaleShouldBeShown(
                permission: PermissionRequest?,
                token: PermissionToken?
            ) {

            }

            override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                callback.onPermissionRejected()
            }
        }
        )
        .withErrorListener { error ->
            Log.e("Dexter", "There was an error: $error")
            callback.onPermissionRejected()
        }.check()
}

/**
 * Function for Multiple Permissions ...
 */
fun initMultiPermissions(activity: Activity, callback: PermissionCallback) {
    Dexter.withActivity(activity)
        .withPermissions(
            CAMERA,
            READ_CONTACTS,
            RECORD_AUDIO
        ).withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport) {/* ... */
                callback.onPermissionGranted()
            }

            override fun onPermissionRationaleShouldBeShown(
                permissions: List<PermissionRequest>,
                token: PermissionToken
            ) {/* ... */
                callback.onPermissionRejected()
            }
        }).check()
}

fun askPermissionIfRequired(baseActivity: BaseActivity<*, *>, permission: Permission) {
    if (baseActivity.isAllGranted(permission)) {
        baseActivity.askForPermissions(permission) { result ->
            // Check the result, see the Using Results section

        }
    }
}


fun runWithPermissions(baseActivity: BaseActivity<*, *>) {
    baseActivity.runWithPermissions(
        Permission.WRITE_EXTERNAL_STORAGE,
        Permission.CAMERA
    ) { result ->
        // Do something
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter("concatData")
fun concatData(view: TextView, concatData: String) {
    Log.e("sadfads", "asasdfasdfadsfasd")
    view.text = "Item $concatData"
}

@BindingAdapter("imageResource")
fun setImageResource(imageView: ImageView, resource: Int) {
    imageView.setImageResource(resource)
}

@BindingAdapter("setImageURI")
fun setImageURI(imageView: ImageView, resource: Uri) {
    imageView.setImageURI(resource)
}

@BindingAdapter("isBold")
fun setBold(view: TextView, isBold: Boolean) {
    if (isBold) {
        view.setTypeface(ResourcesCompat.getFont(view.context, R.font.opensans_bold))
    } else {
        view.setTypeface(ResourcesCompat.getFont(view.context, R.font.opensans_regular))
    }
}








