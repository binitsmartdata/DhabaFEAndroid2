package com.transport.mall.utils.common.localstorage

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class SharedPrefsHelper {
    val SELECTED_LANGUAGE = "selectedLanguage"

    var context: Context? = null

    constructor(context: Context?) {
        this.context = context
    }

    public companion object {
        var prefs: SharedPreferences? = null
        var editor: SharedPreferences.Editor? = null

        @SuppressLint("CommitPrefEdits")
        fun getInstance(context: Context): SharedPrefsHelper {
            prefs = PreferenceManager.getDefaultSharedPreferences(context)
            editor = prefs?.edit()

            return SharedPrefsHelper(context)
        }
    }

    fun setSelectedLanguage(language: String) {
        editor?.putString(SELECTED_LANGUAGE, language)
        editor?.apply()
    }

    fun getSelectedLanguage(): String {
        return prefs?.getString(SELECTED_LANGUAGE, "en")!!
    }

    public fun setLanguageToHindi() {
        setSelectedLanguage("hi")
    }

    public fun setLanguageToPunjabi() {
        setSelectedLanguage("pa")
    }

    public fun setLanguageToEnglish() {
        setSelectedLanguage("en")
    }
}