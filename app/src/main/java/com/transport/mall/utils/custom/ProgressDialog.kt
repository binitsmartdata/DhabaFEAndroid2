package com.transport.mall.utils.custom

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.transport.mall.R

class ProgressDialog(context: Context, var message: String) : Dialog(context, R.style.DialogTheme) {

    var messageTv: AppCompatTextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_progress)

        window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

        val mProgressBar: ProgressBar = findViewById(R.id.progressBar)
        mProgressBar.isIndeterminate = true
        mProgressBar.indeterminateDrawable.setColorFilter(
            ContextCompat.getColor(
                context,
                android.R.color.white
            ), android.graphics.PorterDuff.Mode.MULTIPLY
        )

        messageTv = findViewById(R.id.message)
        messageTv?.text = message
    }

    public fun updateMessage(message: String) {
        messageTv?.text = message
    }
}