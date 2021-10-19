package com.transport.mall.utils

import android.app.Activity
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.transport.mall.R
import com.transport.mall.callback.TimePickerCallBack
import java.text.SimpleDateFormat
import java.util.*

fun Snackbar.withColor(@ColorInt colorInt: Int): Snackbar {
    this.view.setBackgroundColor(colorInt)
    return this
}

fun Activity.showError(message: String, viewGroup: ViewGroup) {

    val duration: Long = 2000

    val snackBar = Snackbar.make(viewGroup, message, duration.toInt())
    snackBar.withColor(ContextCompat.getColor(this, com.transport.mall.R.color.colorAccent))

    val params = snackBar.view.layoutParams as FrameLayout.LayoutParams
    params.gravity = Gravity.TOP
    snackBar.view.layoutParams = params

    val messageTv =
        snackBar.view.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
    messageTv.textSize = 20.0f

    snackBar.view.visibility = View.INVISIBLE
    snackBar.addCallback(object : Snackbar.Callback() {
        override fun onShown(snackbar: Snackbar?) {
            super.onShown(snackbar)
            snackbar?.view?.visibility = View.VISIBLE
            Handler().postDelayed({
                snackbar?.view?.visibility = View.INVISIBLE
            }, duration)
        }
    })

    snackBar.show()

}

inline fun <reified T : Activity> Activity.navigate() {
    val intent = Intent(this, T::class.java)
    startActivity(intent)
}

//Ternary Operator ...
infix fun <T> Boolean.then(param: T): T? = if (this) param else null

fun AppCompatActivity.openFragment(containerId: Int, targetFragment: Fragment) {
    val fragmentName = targetFragment.javaClass.name
    supportFragmentManager.beginTransaction()
        .replace(containerId, targetFragment, fragmentName)
        .addToBackStack(fragmentName)
        .commit()
}

fun Fragment.openFragment(containerId: Int, targetFragment: Fragment) {
    val fragmentName = targetFragment.javaClass.name
    fragmentManager?.beginTransaction()
        ?.replace(containerId, targetFragment, fragmentName)
        ?.addToBackStack(fragmentName)
        ?.commit()
}

fun Fragment.popBackFragment(fragment: Fragment) {
    try {
        if (fragmentManager != null) {
            fragmentManager?.popBackStackImmediate()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.remove(fragment)
            transaction?.commit()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Fragment.timePick(context: Context, textView: TextView, startTime: String) {
    val cal = Calendar.getInstance()
    val sdf = SimpleDateFormat("h:mm a")
    val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
        cal.set(Calendar.HOUR_OF_DAY, hour)
        cal.set(Calendar.MINUTE, minute)

        if (startTime.isNotEmpty()) {
            val date1 = sdf.parse(startTime)
            val date2 = sdf.parse(sdf.format(cal.time))

            if (date1.before(date2)) {
                textView.text = sdf.format(cal.time)
            } else {
                Toast.makeText(context, getString(R.string.please_select_another_time), Toast.LENGTH_SHORT).show()
                textView.text = getString(R.string.not_available)
            }
        } else {
            textView.text = sdf.format(cal.time)
        }
    }

    TimePickerDialog(
        context, timeSetListener, cal.get(Calendar.HOUR_OF_DAY),
        cal.get(Calendar.MINUTE), false
    ).show()
}