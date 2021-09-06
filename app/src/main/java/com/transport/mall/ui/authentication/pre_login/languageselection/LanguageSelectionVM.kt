package com.transport.mall.ui.authentication.pre_login.languageselection

import android.app.Application
import com.transport.mall.utils.base.BaseVM

/**
 * Created by Parambir Singh on 2019-12-06.
 */
class LanguageSelectionVM(application: Application) : BaseVM(application) {
    var app: Application? = null

    init {
        app = application
    }
}