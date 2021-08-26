package com.transport.mall.callback

interface PermissionCallback {
    fun onPermissionGranted()
    fun onPermissionRejected()
}