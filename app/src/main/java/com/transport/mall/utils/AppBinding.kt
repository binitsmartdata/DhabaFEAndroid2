package com.transport.mall.utils

import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.PorterDuff
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
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * Picasso for image loading ...
 */
@BindingAdapter("loadImage", "placeholder")
fun xloadImages(
    view: ImageView?,
    image: String?,
    placeHolder: Int
) {
    val isSpecifiedPlaceholder = false//when (placeHolder) {
//        R.drawable.ic_profile_pic_placeholder -> true
//        R.drawable.ic_id_front -> true
//        R.drawable.ic_id_back -> true
//        else -> false
//    }

    image?.let {
        if (it.contains("http")) {
            Picasso.get()
                .load(it)
                .error(if (isSpecifiedPlaceholder) placeHolder else R.drawable.grey_placeholder)
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
            .load(R.drawable.grey_placeholder)
            .error(if (isSpecifiedPlaceholder) placeHolder else R.drawable.grey_placeholder)
            .placeholder(R.drawable.ic_image_placeholder)
            .into(view)
    }
}

/**
 * Picasso for image loading ...
 */
@BindingAdapter("loadProfileImage", "placeholder")
fun loadProfileImage(
    view: ImageView?,
    image: String?,
    placeHolder: Int
) {
    image?.let {
        if (it.contains("http")) {
            Picasso.get()
                .load(it)
                .error(placeHolder)
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
            .load(R.drawable.grey_placeholder)
            .error(placeHolder)
            .placeholder(placeHolder)
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
                .error(if (placeHolder == R.drawable.ic_profile_pic_placeholder) placeHolder else R.drawable.grey_placeholder)
                .placeholder(placeHolder)
                .transform(
                    RoundedCornersTransformation(
                        50,
                        50,
                        RoundedCornersTransformation.CornerType.ALL
                    )
                )
                .into(view)
        } else {
            val file = File(it)
            Picasso.get()
                .load(file)
                .error(placeHolder)
                .placeholder(placeHolder)
                .transform(
                    RoundedCornersTransformation(
                        50,
                        50,
                        RoundedCornersTransformation.CornerType.ALL
                    )
                )
                .into(view)
        }
    } ?: run {
        Picasso.get()
            .load(R.drawable.ic_image_placeholder)
            .error(if (placeHolder == R.drawable.ic_profile_pic_placeholder) placeHolder else R.drawable.grey_placeholder)
            .placeholder(R.drawable.ic_image_placeholder)
            .transform(
                RoundedCornersTransformation(
                    50,
                    50,
                    RoundedCornersTransformation.CornerType.ALL
                )
            )
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

@BindingAdapter("isHighlighted")
fun isHighlighted(view: ImageView, isHighlighted: Boolean) {
    if (isHighlighted) {
        view.setColorFilter(
            ContextCompat.getColor(view.context, R.color.colorPrimary),
            PorterDuff.Mode.SRC_IN
        )
    } else {
        view.setColorFilter(
            ContextCompat.getColor(view.context, R.color.text_gray),
            PorterDuff.Mode.SRC_IN
        )
    }
}

@BindingAdapter("timeAgo")
fun timeAgo(view: TextView, dataDate: String?) {
    dataDate?.let {
        var convertTime = ""
        val suffix = "ago"
        try {
            val inputPattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
            //            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
            val dateFormat = SimpleDateFormat(inputPattern, Locale.getDefault())
            dateFormat.timeZone = TimeZone.getTimeZone("UTC")
            val pasTime = dateFormat.parse(dataDate)
            val nowTime = Date()
            val dateDiff = nowTime.time - pasTime.time
            val second = TimeUnit.MILLISECONDS.toSeconds(dateDiff)
            val minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff)
            val hour = TimeUnit.MILLISECONDS.toHours(dateDiff)
            val day = TimeUnit.MILLISECONDS.toDays(dateDiff)


            convertTime = if (second < 60) {
                if (second == 1L) {
                    "$second second $suffix"
                } else {
                    "$second seconds $suffix"
                }
            } else if (minute < 60) {
                if (minute == 1L) {
                    "$minute minute $suffix"
                } else {
                    "$minute minutes $suffix"
                }
            } else if (hour < 24) {
                if (hour == 1L) {
                    "$hour hour $suffix"
                } else {
                    "$hour hours $suffix"
                }
            } else if (day >= 7) {
                if (day >= 365) {
                    val tempYear = day / 365
                    if (tempYear == 1L) {
                        "$tempYear year $suffix"
                    } else {
                        "$tempYear years $suffix"
                    }
                } else if (day >= 30) {
                    val tempMonth = day / 30
                    if (tempMonth == 1L) {
                        (day / 30).toString() + " month " + suffix
                    } else {
                        (day / 30).toString() + " months " + suffix
                    }
                } else {
                    val tempWeek = day / 7
                    if (tempWeek == 1L) {
                        (day / 7).toString() + " week " + suffix
                    } else {
                        (day / 7).toString() + " weeks " + suffix
                    }
                }
            } else {
                if (day == 1L) {
                    "$day day $suffix"
                } else {
                    "$day days $suffix"
                }
            }
        } catch (e: ParseException) {
            e.printStackTrace()
            Log.e("TimeAgo", e.message + "")
        }
        view.text = convertTime
    } ?: run { view.text = dataDate }
}







