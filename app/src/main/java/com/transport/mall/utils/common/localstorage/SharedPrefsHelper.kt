package com.transport.mall.utils.common.localstorage

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import com.google.gson.Gson
import com.transport.mall.model.DhabaModelMain
import com.transport.mall.model.UserModel

class SharedPrefsHelper {
    private val SELECTED_LANGUAGE = "selectedLanguage"
    private val id = "id"
    private val fname = "fname"
    private val lname = "lname"
    private val email = "email"
    private val lastLogin = "lastLogin"
    private val accessToken = "accessToken"
    private val draftDhaba = "draftDhaba"

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
        editor?.putString(id, userData._id)
        editor?.putString(fname, userData.fname)
        editor?.putString(lname, userData.lname)
        editor?.putString(email, userData.email)
        editor?.putString(lastLogin, userData.lastLogin)
        editor?.putString(accessToken, userData.accessToken)
        editor?.apply()
    }

    fun getUserData(): UserModel {
        var userModel = UserModel()
        userModel._id = prefs?.getString(id, "")!!
        userModel.fname = prefs?.getString(fname, "")!!
        userModel.lname = prefs?.getString(lname, "")!!
        userModel.email = prefs?.getString(email, "")!!
        userModel.lastLogin = prefs?.getString(lastLogin, "")!!
        userModel.accessToken = prefs?.getString(accessToken, "")!!

        return userModel
    }

    fun getSelectedLanguage(): String {
        return prefs?.getString(SELECTED_LANGUAGE, "")!!
    }

    fun getDraftDhaba(): DhabaModelMain? {
        try {
            return Gson().fromJson(prefs?.getString(draftDhaba, "")!!, DhabaModelMain::class.java)
        } catch (e: Exception) {
            Log.e("GET DHABA DRAFT ::", e.toString())
            return null
        }
    }

    fun setDraftDhaba(dhabaModelMain: DhabaModelMain) {
        editor?.putString(draftDhaba, Gson().toJson(dhabaModelMain).toString())
        editor?.apply()
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

    fun clearData() {
        editor?.clear()
        editor?.apply()
    }

    fun deleteDraftDhaba() {
        editor?.putString(draftDhaba, "")
        editor?.apply()
    }
}