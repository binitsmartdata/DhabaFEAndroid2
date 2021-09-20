package com.transport.mall.utils.custom

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class CustomViewPager(context: Context?, attrs: AttributeSet?) : ViewPager(
    context!!, attrs
) {
    private var pagerEnabled = true
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (pagerEnabled) {
            super.onTouchEvent(event)
        } else false
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return if (pagerEnabled) {
            super.onInterceptTouchEvent(event)
        } else false
    }

    fun setPagingEnabled(enabled: Boolean) {
        this.pagerEnabled = enabled
    }
}