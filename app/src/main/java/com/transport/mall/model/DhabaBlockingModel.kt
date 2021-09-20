package com.transport.mall.model

import java.io.Serializable

/**
 * Created by Parambir Singh on 2020-01-24.
 */
class DhabaBlockingModel : Serializable {
    var _id: String = ""
    var blockDay: Int = 0
    var blockMonth: Int = 0
    var propertyStatus: Boolean = false
}