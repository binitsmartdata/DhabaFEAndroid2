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
    private val mUserData = "userData"
    private val draftDhaba = "draftDhaba"
    private val ROOM_ID = "roomId"

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

    fun setRoomId(roomId: String) {
        editor?.putString(ROOM_ID, roomId)
        editor?.apply()
    }

    fun getRoomId(): String {
        return prefs?.getString(ROOM_ID, "")!!
    }

    fun setUserData(userData: UserModel) {
        editor?.putString(mUserData, Gson().toJson(userData).toString())
        editor?.apply()
    }

    fun getUserData(): UserModel {
        try {
            return Gson().fromJson(prefs?.getString(mUserData, "")!!, UserModel::class.java)
        } catch (e: Exception) {
            Log.e("GET DHABA DRAFT ::", e.toString())
            return UserModel()
        }
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