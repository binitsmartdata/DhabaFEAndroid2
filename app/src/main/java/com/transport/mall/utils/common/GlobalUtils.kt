package com.transport.mall.utils.common

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.media.MediaMetadataRetriever
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import android.util.Patterns
import android.util.TypedValue
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import com.essam.simpleplacepicker.MapActivity
import com.essam.simpleplacepicker.utils.SimplePlacePicker
import com.google.android.gms.location.*
import com.transport.mall.R
import com.transport.mall.model.LocationAddressModel
import io.github.g00fy2.versioncompare.Version
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.*
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


object GlobalUtils {
    private var mDialog: Dialog? = null

    private var alertDialog: AlertDialog? = null

    @JvmStatic
    fun showProgressDialog(con: Context?) {
        try {
            if (mDialog == null) {
                mDialog = Dialog(con!!, android.R.style.Theme_Translucent_NoTitleBar)
                var loaderView =
                    LayoutInflater.from(con).inflate(R.layout.loader, null)
                mDialog!!.setContentView(loaderView)
                mDialog!!.setCancelable(false)
                var tvProgressMessage =
                    loaderView.findViewById(R.id.tvProgressMessage) as TextView
                tvProgressMessage.visibility = View.GONE
                mDialog!!.show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @JvmStatic
    fun showProgressDialog(con: Context?, message: String?) {
        try {
            if (mDialog == null) {
                mDialog = Dialog(con!!, android.R.style.Theme_Translucent_NoTitleBar)
                var loaderView: View = LayoutInflater.from(con)
                    .inflate(R.layout.loader, null, false)
                mDialog!!.setContentView(loaderView)
                mDialog!!.setCancelable(false)

                var tvProgressMessage =
                    loaderView.findViewById(R.id.tvProgressMessage) as TextView
                tvProgressMessage.text = message
                tvProgressMessage.visibility = View.VISIBLE

                mDialog!!.show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @JvmStatic
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

    @JvmStatic
    fun showDhabaDiscardAlert(
        context: Context,
        message: String?,
        callBack: GenericCallBack<Int>
    ) {
        val dialog = AlertDialog.Builder(context)
        dialog.setTitle(context.getString(R.string.please_confirm))
        dialog.setMessage(message)
        dialog.setPositiveButton(context.getString(R.string.yes)) { _, _ -> callBack.onResponse(1) }
        dialog.setNegativeButton(context.getString(R.string.no)) { _, _ -> callBack.onResponse(2) }
        alertDialog = dialog.show()
    }

    @JvmStatic
    fun showConfirmationDialog(
        context: Context,
        title: String?,
        message: String?,
        callBack: GenericCallBack<Boolean?>
    ) {
        val dialog = AlertDialog.Builder(context)
        dialog.setTitle(title)
        dialog.setMessage(message)
        dialog.setPositiveButton(context.getString(R.string.yes)) { _, _ -> callBack.onResponse(true) }
        dialog.setNegativeButton(context.getString(R.string.cancel)) { _, _ ->
            callBack.onResponse(
                false
            )
        }
        alertDialog = dialog.show()
    }

    @JvmStatic
    fun showConfirmationDialogYesNo(
        context: Context,
        message: String?,
        callBack: GenericCallBack<Boolean>
    ) {
        val dialog = AlertDialog.Builder(context)
        dialog.setCancelable(false)
        dialog.setTitle(context.getString(R.string.please_confirm))
        dialog.setMessage(message)
        dialog.setPositiveButton(context.getString(R.string.yes)) { _, _ -> callBack.onResponse(true) }
        dialog.setNegativeButton(context.getString(R.string.no)) { _, _ ->
            callBack.onResponse(
                false
            )
        }
        alertDialog = dialog.show()
    }

    @JvmStatic
    fun showConfirmationDialogYesNo(
        context: Context,
        title: String?,
        message: String?,
        callBack: GenericCallBack<Boolean>
    ) {
        val dialog = AlertDialog.Builder(context)
        dialog.setCancelable(false)
        dialog.setTitle(title)
        dialog.setMessage(message)
        dialog.setPositiveButton(context.getString(R.string.yes)) { _, _ -> callBack.onResponse(true) }
        dialog.setNegativeButton(context.getString(R.string.no)) { _, _ ->
            callBack.onResponse(
                false
            )
        }
        alertDialog = dialog.show()
    }

    @JvmStatic
    fun showInfoDialog(
        context: Context,
        message: String?,
        cancelable: Boolean,
        callBack: GenericCallBack<Boolean?>
    ) {
        val dialog = AlertDialog.Builder(context)
        dialog.setTitle(context.getString(R.string.appName))
        dialog.setMessage(message)
        dialog.setCancelable(cancelable)
        dialog.setPositiveButton(context.getString(R.string.ok)) { _, _ -> callBack.onResponse(true) }
        alertDialog = dialog.show()
    }

    @JvmStatic
    fun showInfoDialog(context: Context, message: String?, callBack: GenericCallBack<Boolean?>) {
        val dialog = AlertDialog.Builder(context)
        dialog.setTitle(context.getString(R.string.appName))
        dialog.setMessage(message)
        dialog.setPositiveButton(context.getString(R.string.ok)) { _, _ -> callBack.onResponse(true) }
        alertDialog = dialog.show()
    }

    @JvmStatic
    fun showInfoDialog(
        context: Context,
        title: String?,
        message: String?,
        callBack: GenericCallBack<Boolean?>
    ) {
        val dialog = AlertDialog.Builder(context)
        dialog.setTitle(title)
        dialog.setMessage(message)
        dialog.setPositiveButton(context.getString(R.string.ok)) { _, _ -> callBack.onResponse(true) }
        alertDialog = dialog.show()
    }

    @JvmStatic
    fun showInfoDialogNoTitle(context: Context, message: String?) {
        val dialog = AlertDialog.Builder(context)
        dialog.setMessage(message)
        alertDialog = dialog.show()
    }

    fun hideAlertDialog() {
        if (alertDialog != null) {
            alertDialog?.dismiss()
        }
    }

    @JvmStatic
    fun showToastInCenter(context: Context?, msg: String) {
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


    @JvmStatic
    fun showOptionsDialog(
        context: Context?,
        strings: Array<String?>?,
        dialogTitle: String?,
        listener: DialogInterface.OnClickListener?
    ) {
        val dialog = AlertDialog.Builder(context!!)
        dialog.setTitle(dialogTitle)
        dialog.setItems(strings, listener)
        dialog.show()
    }

    @JvmStatic
    fun showOptionsDialog(
        context: Context?,
        strings: Array<String?>?,
        dialogTitle: String?,
        cancellable: Boolean,
        listener: DialogInterface.OnClickListener?
    ) {
        val dialog = AlertDialog.Builder(context!!)
        dialog.setCancelable(cancellable)
        dialog.setTitle(dialogTitle)
        dialog.setItems(strings, listener)
        dialog.show()
    }

    @JvmStatic
    fun openKeyboard(activity: Activity, editText: EditText?) {
        editText!!.post {
            editText.requestFocus()
            if (activity != null) {
                val imm =
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm?.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
            }
        }

    }

    @JvmStatic
    fun hideKeyboard(context: Context, view: View) {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    @JvmStatic
    fun getNonNullString(target: String?, defValue: String): String {
        return if (target == null || target.trim { it <= ' ' }
                .equals("null", ignoreCase = true) || target.trim { it <= ' ' }
                .isEmpty()) defValue else target
    }

    @JvmStatic
    fun getNullifEmpty(target: String?): String? {
        return if (target == null || target.trim { it <= ' ' }
                .equals("null", ignoreCase = true) || target.trim { it <= ' ' }
                .isEmpty()) null else target
    }

    @JvmStatic
    fun roundTwoDecimals(d: Double): Double {
//        val format: NumberFormat = DecimalFormat("#.##")
//        format.setRoundingMode(RoundingMode.CEILING)
//        return java.lang.Double.valueOf(format.format(d))
        var bigDecimal = BigDecimal(java.lang.Double.toString(d))
        bigDecimal = bigDecimal.setScale(2, RoundingMode.HALF_UP)
        return bigDecimal.toDouble();
    }

    @JvmStatic
    fun roundThreeDecimals(d: Double): Double {
        val twoDForm = DecimalFormat("#.###")
        twoDForm.setRoundingMode(RoundingMode.CEILING)
        return java.lang.Double.valueOf(twoDForm.format(d))
    }

    /*
    public static float convertDpToPx(Context context, float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }
*/

    @JvmField
    var buttonTouchEffect = View.OnTouchListener { v, event ->
        android.util.Log.d("ON_TOUCH", "onTouch $event")
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                v.alpha = 0.5f
                return@OnTouchListener false
            }
            MotionEvent.ACTION_UP -> {
                v.alpha = 1.0f
                v.alpha = 1.0f
                return@OnTouchListener false
            }
            MotionEvent.ACTION_CANCEL -> {
                v.alpha = 1.0f
                return@OnTouchListener false
            }
        }
        false
    }

    /*
    public static double roundTo2Decimals(double val) {
        DecimalFormat df2 = new DecimalFormat("###.##");
        return Double.valueOf(df2.format(val));
    }
*/
    @JvmStatic
    val screenWidth: Int
        get() = Resources.getSystem().displayMetrics.widthPixels

    @JvmStatic
    fun isAccurateRange(time: String?, endtime: String?): Boolean {
        @SuppressLint("SimpleDateFormat") val sdf = SimpleDateFormat("HH:mm")
        try {
            return sdf.parse(time).before(sdf.parse(endtime))
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return false
    }

    fun localToUTC(datesToConvert: String): String {
        var dateToReturn = datesToConvert
        @SuppressLint("SimpleDateFormat") val sdf = SimpleDateFormat("HH:mm:ss")
        sdf.timeZone = TimeZone.getDefault()
        var gmt: Date?

        @SuppressLint("SimpleDateFormat") val sdfOutPutToSend = SimpleDateFormat("HH:mm:ss")
        sdfOutPutToSend.timeZone = TimeZone.getTimeZone("UTC")
        try {
            gmt = sdf.parse(datesToConvert)
            dateToReturn = sdfOutPutToSend.format(gmt)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return dateToReturn
    }

    fun dateTimeLocalToUTC(datesToConvert: String): String {
        var dateToReturn = datesToConvert
        @SuppressLint("SimpleDateFormat") val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
        sdf.timeZone = TimeZone.getDefault()
        var gmt: Date?

        @SuppressLint("SimpleDateFormat") val sdfOutPutToSend =
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        sdfOutPutToSend.timeZone = TimeZone.getTimeZone("UTC")
        try {
            gmt = sdf.parse(datesToConvert)
            dateToReturn = sdfOutPutToSend.format(gmt)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return dateToReturn
    }

    fun uTCToLocal(datesToConvert: String?): String? {
        var dateToReturn = datesToConvert
        @SuppressLint("SimpleDateFormat") val sdf = SimpleDateFormat("HH:mm:ss")
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        var gmt: Date?
        @SuppressLint("SimpleDateFormat") val sdfOutPutToSend = SimpleDateFormat("HH:mm:ss")
        sdfOutPutToSend.timeZone = TimeZone.getDefault()
        try {
            gmt = sdf.parse(datesToConvert)
            dateToReturn = sdfOutPutToSend.format(gmt)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return dateToReturn
    }

    public fun showKeyboard(activity: Activity) {
        val view = activity.currentFocus
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
    }

    fun hideKeyboard(activity: Activity) {
        try {
            val view = activity.currentFocus
            val methodManager =
                activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            assert(view != null)
            methodManager.hideSoftInputFromWindow(
                view!!.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        } catch (e: Exception) {
            Log.e("KEYBOARD", e.toString() + "------")
        } catch (e: Error) {
            Log.e("KEYBOARD", e.toString() + "------")
        }
    }

    fun calculateNoOfColumns(
        context: Context,
        columnWidthDp: Float
    ): Int { // For example columnWidthdp=180
        val displayMetrics: DisplayMetrics = context.resources.displayMetrics
        val screenWidthDp: Float = displayMetrics.widthPixels / displayMetrics.density
        return (screenWidthDp / columnWidthDp + 0.5).toInt()
    }

    fun isScreenLarge(context: Context): Boolean {
        val dm = context.resources.displayMetrics
        val screenWidth = dm.widthPixels / dm.density
        val screenHeight = dm.heightPixels / dm.density
        return screenWidth >= 600 && isTabletSize(context)!!
    }

    private fun isTabletSize(context: Context): Boolean? {
        var screenLayout = context.resources.configuration.screenLayout
        screenLayout = screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK
        return when (screenLayout) {
            Configuration.SCREENLAYOUT_SIZE_SMALL, Configuration.SCREENLAYOUT_SIZE_NORMAL -> false
            Configuration.SCREENLAYOUT_SIZE_LARGE, Configuration.SCREENLAYOUT_SIZE_XLARGE -> true
            else -> false
        }
    }

    fun isValidEmail(str: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(str).matches()
    }

    @JvmStatic
    fun dpToPx(dp: Int, context: Context): Int {
        val px = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            context.resources.getDisplayMetrics()
        )
        return px.toInt()
    }

    @JvmStatic
    fun isTimeLiesBetween(transactionTimeStamp: Long, startTime: Long, endtime: Long): Boolean {
        val sdf = SimpleDateFormat("HH:mm")

        val time = sdf.format(transactionTimeStamp)
        val transactionDate = sdf.parse(time)

        val timeStart = sdf.format(startTime)
        val startDate = sdf.parse(timeStart)

        val timeEnd = sdf.format(endtime)
        val endDate = sdf.parse(timeEnd)

        return (transactionDate.after(startDate) && transactionDate.before(endDate))
    }

    data class QrOutputModel(
        var cardNumber: String? = null,
        var dealCode: String? = null,
        var bucketId: String? = null
    ) : Serializable

    @JvmStatic
    fun parseQrCode(inputString: String?): QrOutputModel {
        var qrOutputModel = QrOutputModel()

        if (inputString != null && !inputString.isEmpty()) {
            if (inputString.contains("||")) {
                val qrData: Array<String> = inputString.split("||").toTypedArray()
                if (qrData.size > 0) qrOutputModel.cardNumber = qrData[0]
                if (qrData.size > 1) qrOutputModel.bucketId = qrData[1]
            } else if (inputString.contains("|")) {
                val qrData: Array<String> = inputString.split("|").toTypedArray()
                if (qrData.size > 0) qrOutputModel.cardNumber = qrData[0]
                if (qrData.size > 1) qrOutputModel.dealCode = qrData[1]
                if (qrData.size > 2) qrOutputModel.bucketId = qrData[2]
            } else {
                qrOutputModel.cardNumber = inputString
                qrOutputModel.dealCode = null
                qrOutputModel.bucketId = null
            }
        }
        return qrOutputModel
    }

    @JvmStatic
    fun isNetworkAvailable(applicationContext: Context): Boolean {
        val connectivity =
            applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivity == null) {
            return false
        } else {
            val info = connectivity.allNetworkInfo
            if (info != null) {
                for (i in info.indices) {
                    if (info[i].state == NetworkInfo.State.CONNECTED) {
                        return true
                    }
                }
            }
        }
        return false
    }

    @JvmStatic
    fun setThemeColorToThisButton(button: Button, context: Context?) {
        val themeColor = Color.RED

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            button.background.setColorFilter(BlendModeColorFilter(themeColor, BlendMode.MULTIPLY))
        } else {
            button.background.setColorFilter(themeColor, PorterDuff.Mode.MULTIPLY)
        }
    }

    private var notificationManager: NotificationManagerCompat? = null
    private var notificationId = 999

    @JvmStatic
    fun showInfoNotification(context: Context, title: String?, message: String?) {
        val builder = NotificationCompat.Builder(context, "OFFLINE_TRANSACTIONS")
            .setSmallIcon(android.R.drawable.btn_plus)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "TRANSACTIONS"
            val description = message
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("OFFLINE_TRANSACTIONS", name, importance)
            channel.description = description
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager: NotificationManager =
                context.getSystemService<NotificationManager>(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager = NotificationManagerCompat.from(context)

        // notificationId is a unique int for each notification that you must define
        notificationManager?.notify(notificationId, builder.build())
    }

    @JvmStatic
    fun hideNotificationIfShowing() {
        if (notificationManager != null) {
            notificationManager?.cancel(notificationId)
        }
    }

    @JvmStatic
    fun isMemoryAvailable(): Boolean {
        // Get app memory info
        var available = Runtime.getRuntime().maxMemory()
        var used = Runtime.getRuntime().totalMemory()

        // Check for & and handle low memory state
        var percentAvailable = 100f * (1f - (used / available));
        if (percentAvailable <= 5) {
            return false;
        } else {
            return true;
        }
    }

    fun getCurrentDate(): String {
        return SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
    }

    @SuppressLint("MissingPermission")
    fun refreshLocation(context: Context) {
        val mLocationRequest = LocationRequest.create()
        mLocationRequest.interval = 60000
        mLocationRequest.fastestInterval = 5000
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val mLocationCallback: LocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                if (locationResult.lastLocation != null) {
                    LocationServices.getFusedLocationProviderClient(context)
                        .removeLocationUpdates(this)
                }
            }
        }
        LocationServices.getFusedLocationProviderClient(context)
            .requestLocationUpdates(mLocationRequest, mLocationCallback, null)
    }

    fun getAddressUsingLatLong(
        context: Context,
        latitude: Double,
        longitude: Double,
        callBack: GenericCallBack<LocationAddressModel>
    ) {
        showProgressDialog(context, context.getString(R.string.fetching_location_address))
        GlobalScope.launch(Dispatchers.Main) {
            val geocoder: Geocoder
            val addresses: List<Address>
            geocoder = Geocoder(context, Locale.getDefault())

            var address = ""
            var city = ""
            var state = ""
            var country = ""
            var postalCode = ""
            var knownName = ""

            try {
                // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                addresses = geocoder.getFromLocation(latitude, longitude, 1)

                address = addresses[0].getAddressLine(0)
                city = getNonNullString(addresses[0].getLocality(), "")
                state = getNonNullString(addresses[0].getAdminArea(), "")
                country = getNonNullString(addresses[0].getCountryName(), "")
                postalCode = getNonNullString(addresses[0].getPostalCode(), "")
                knownName = getNonNullString(addresses[0].getFeatureName(), "")

                hideProgressDialog()
                callBack.onResponse(
                    LocationAddressModel(
                        address,
                        city,
                        state,
                        country,
                        postalCode,
                        knownName,
                        latitude,
                        longitude
                    )
                )
            } catch (e: Exception) {
                Log.e("FETCH ADDRESS::::", e.toString())
                hideProgressDialog()
                showInfoDialog(
                    context,
                    context.getString(R.string.cant_fetch_location_address),
                    GenericCallBack {
                        callBack.onResponse(
                            LocationAddressModel(
                                address,
                                city,
                                state,
                                country,
                                postalCode,
                                knownName,
                                latitude,
                                longitude
                            )
                        )
                    })
            }
        }
    }

    @Throws(Throwable::class)
    fun getThumbnailFromVideo(videoPath: String?): Bitmap? {
        var bitmap: Bitmap? = null
        var mediaMetadataRetriever: MediaMetadataRetriever? = null
        try {
            mediaMetadataRetriever = MediaMetadataRetriever()
            if (Build.VERSION.SDK_INT >= 14) mediaMetadataRetriever.setDataSource(
                videoPath,
                HashMap()
            ) else mediaMetadataRetriever.setDataSource(videoPath)
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            Log.e("ThumbnailFromVideo :::", e.message.toString())
        } finally {
            mediaMetadataRetriever?.release()
        }
        return bitmap
    }

    fun playThisVideo(context: Context, videoPath: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(Uri.parse(videoPath), "video/mp4")
        context.startActivity(intent)
    }

    fun isLocationEnabled(mContext: Context): Boolean {
        var isEnabled = false
        val lm = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        isEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER) || lm.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
        return isEnabled
    }

    fun selectLocationOnMap(fragment: Fragment, latitude: String?, longitude: String?) {
        val apiKey = fragment.getString(R.string.googleMapsApiKey)
        val intent = Intent(fragment.activity, MapActivity::class.java)

        val bundle = Bundle()
        bundle.putString(SimplePlacePicker.API_KEY, apiKey)

        try {
            if (latitude != null && longitude != null && latitude.toDouble() != 0.0 && longitude.toDouble() != 0.0) {
                bundle.putDouble(SimplePlacePicker.LATITUDE, latitude.toDouble())
                bundle.putDouble(SimplePlacePicker.LONGITUDE, longitude.toDouble())
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

        intent.putExtras(bundle)
        fragment.startActivityForResult(intent, SimplePlacePicker.SELECT_LOCATION_REQUEST_CODE)
    }

    fun shakeThisView(context: Context, view: View) {
        val shake: Animation = AnimationUtils.loadAnimation(context, R.anim.shake_anim)
        view.startAnimation(shake)
    }

    fun getCurrentVersion(context: Context): String {
        try {
            val pInfo: PackageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            return pInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            return "0"
        }
    }

    fun isCurrentVersionSupported(context: Context, supportedVersion: String): Boolean {
        val currentVersion = Version(getCurrentVersion(context))
        val lastSupportedVersion = Version(supportedVersion)
        return !currentVersion.isLowerThan(lastSupportedVersion)
    }

    fun showCustomConfirmationDialogYesNo(
        context: Context,
        messageString: String,
        callBack: GenericCallBack<Boolean>
    ) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.custome_confirmation_dialog_box)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        val message = dialog.findViewById<TextView>(R.id.message)
        message.text = messageString
        val btn_no_exit = dialog.findViewById<TextView>(R.id.btn_no_exit)
        val btn_yes_exit = dialog.findViewById<Button>(R.id.btn_yes_exit)
        btn_no_exit.setOnClickListener { v: View? ->
            callBack.onResponse(false)
            dialog.dismiss()
        }
        btn_yes_exit.setOnClickListener { v: View? ->
            dialog.dismiss()
            callBack.onResponse(true)
        }
        dialog.show()
    }

    fun showCustomConfirmationDialog(
        context: Context,
        messageString: String,
        positiveBtnText: String,
        negativeBtnText: String,
        callBack: GenericCallBack<Boolean>
    ) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.custome_confirmation_dialog_box)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        val message = dialog.findViewById<TextView>(R.id.message)
        message.text = messageString
        val btn_no_exit = dialog.findViewById<TextView>(R.id.btn_no_exit)
        val btn_yes_exit = dialog.findViewById<Button>(R.id.btn_yes_exit)
        btn_no_exit.text = negativeBtnText
        btn_yes_exit.text = positiveBtnText
        btn_no_exit.setOnClickListener { v: View? ->
            callBack.onResponse(false)
            dialog.dismiss()
        }
        btn_yes_exit.setOnClickListener { v: View? ->
            dialog.dismiss()
            callBack.onResponse(true)
        }
        dialog.show()
    }

    fun disableTemporarily(view: View) {
        view.isEnabled = false
        Handler(Looper.getMainLooper()).postDelayed({ view.isEnabled = true }, 1000)
    }

    fun getTimeAgo(dataDate: String?): String {
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
            return convertTime
        } ?: run { return dataDate.toString() }
    }

    @JvmStatic
    fun getWidthScreenResolution(context: Context): Int {
        var width = 0
        try {
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            val metrics = DisplayMetrics()
            display.getMetrics(metrics)
            width = metrics.widthPixels
            val height = metrics.heightPixels
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return width
    }

    @JvmStatic
    fun getHeightScreenResolution(context: Context): Int {
        var height = 0
        try {
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            val metrics = DisplayMetrics()
            display.getMetrics(metrics)
            val width = metrics.widthPixels
            height = metrics.heightPixels
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return height
    }
}