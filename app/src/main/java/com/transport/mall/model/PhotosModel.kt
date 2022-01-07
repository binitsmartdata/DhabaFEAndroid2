package com.transport.mall.model

import android.net.Uri
import java.io.Serializable

/**
 * Created by Parambir Singh on 2020-01-24.
 */
data class PhotosModel(
    val _id: String,
    val path: String
) : Serializable {
    var isActive: Boolean = true
    var isPrimary: Boolean = false
}