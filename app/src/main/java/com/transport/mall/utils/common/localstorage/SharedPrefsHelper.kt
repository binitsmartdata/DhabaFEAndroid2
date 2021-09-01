package com.transport.mall.utils.common.localstorage

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.transport.mall.model.LangModel
import com.transport.mall.model.UserModel

class SharedPrefsHelper {
    private val SELECTED_LANGUAGE = "selectedLanguage"
    private val id = "id"
    private val fname = "fname"
    private val lname = "lname"
    private val email = "email"
    private val lastLogin = "lastLogin"
    private val accessToken = "accessToken"

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

    fun setUserData(userData: UserModel) {
        editor?.putString(id, userData.id)
        editor?.putString(fname, userData.fname.en)
        editor?.putString(lname, userData.lname.en)
        editor?.putString(email, userData.email)
        editor?.putString(lastLogin, userData.lastLogin)
        editor?.putString(accessToken, userData.accessToken)
        editor?.apply()
    }

    fun getUserData(): UserModel {
        var userModel = UserModel(
            prefs?.getString(id, "")!!,
            LangModel(prefs?.getString(fname, "")!!, "", ""),
            LangModel(prefs?.getString(lname, "")!!, "", ""),
            prefs?.getString(email, "")!!,
            prefs?.getString(lastLogin, "")!!,
            prefs?.getString(accessToken, "")!!
        )
        return userModel
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