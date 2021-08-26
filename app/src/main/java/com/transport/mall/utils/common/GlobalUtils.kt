package com.transport.mall.utils.common

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.DialogInterface
import android.content.res.Resources
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.util.DisplayMetrics
import android.util.Log
import android.util.Patterns
import android.util.TypedValue
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.io.Serializable
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import com.transport.mall.R

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
    fun showConfirmationDialog(
        context: Context,
        message: String?,
        callBack: GenericCallBack<Boolean?>
    ) {
        val dialog = AlertDialog.Builder(context)
        dialog.setTitle("Please Confirm")
        dialog.setMessage(message)
        dialog.setPositiveButton("Yes") { _, _ -> callBack.onResponse(true) }
        dialog.setNegativeButton("Cancel") { _, _ -> callBack.onResponse(false) }
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
        dialog.setPositiveButton("Yes") { _, _ -> callBack.onResponse(true) }
        dialog.setNegativeButton("Cancel") { _, _ -> callBack.onResponse(false) }
        alertDialog = dialog.show()
    }

    @JvmStatic
    fun showConfirmationDialogYesNo(
        context: Context,
        message: String?,
        callBack: GenericCallBack<Boolean?>
    ) {
        val dialog = AlertDialog.Builder(context)
        dialog.setCancelable(false)
        dialog.setTitle(message)
        dialog.setPositiveButton("Yes") { _, _ -> callBack.onResponse(true) }
        dialog.setNegativeButton("Cancel") { _, _ -> callBack.onResponse(false) }
        alertDialog = dialog.show()
    }

    @JvmStatic
    fun showInfoDialog(context: Context, message: String?, callBack: GenericCallBack<Boolean?>) {
        val dialog = AlertDialog.Builder(context)
        dialog.setTitle("Transport Mall")
        dialog.setMessage(message)
        dialog.setPositiveButton("Ok") { _, _ -> callBack.onResponse(true) }
        alertDialog = dialog.show()
    }

    @JvmStatic
    fun showInfoDialog(context: Context, title: String?, message: String?) {
        val dialog = AlertDialog.Builder(context)
        dialog.setTitle(title)
        dialog.setMessage(message)
        dialog.setPositiveButton("Ok") { _, _ -> }
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
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
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
            Log.e("KEYBOARD", "------")
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


    fun CharSequence?.isValidEmail() =
        !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()


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
}