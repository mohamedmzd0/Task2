package com.kayan.voicechat.helper

import android.content.Context
import android.content.SharedPreferences
import com.appsquare.task.data.LoginResponse
import com.appsquare.task.data.UserModel

class SharedHelper {


    companion object {

        val PROFILE_URL: String = "profile_url"
        val TOKEN = "TOKEN"
        val NAME = "NAME"
        val EMAIL = "EMAIL"
        val ID = "ID"


        var sharedPreferences: SharedPreferences? = null

        private fun getSharedPref(context: Context): SharedPreferences? {
            if (sharedPreferences == null) {
                sharedPreferences = context.getSharedPreferences("app_data", Context.MODE_PRIVATE)
            }
            return sharedPreferences
        }

        private fun getSharedPrefEditor(context: Context): SharedPreferences.Editor? {
            return getSharedPref(context)?.edit()
        }

        public fun saveString(context: Context, key: String, value: String?) {
            getSharedPrefEditor(context)?.putString(key, value)?.apply()
        }

        public fun saveInt(context: Context, key: String, value: Int) {
            getSharedPrefEditor(context)?.putInt(key, value)?.apply()
        }

        public fun saveBoolean(context: Context, key: String, value: Boolean) {
            getSharedPrefEditor(context)?.putBoolean(key, value)?.apply()
        }

        public fun getString(context: Context, key: String): String? {
            return getSharedPref(context)?.getString(key, "")
        }

        public fun getBoolean(context: Context, key: String): Boolean? {
            return getSharedPref(context)?.getBoolean(key, false)
        }

        public fun getInt(context: Context, key: String): Int? {
            return getSharedPref(context)?.getInt(key, -1)
        }


        fun clearUser(context: Context) {
            getSharedPrefEditor(context)?.clear()?.apply()

        }

        fun saveUser(context: Context, userData: UserModel?,token:String) {
            clearUser(context)
            userData?.let {
                saveInt(context, ID, it.id?:-1)
            saveString(context, NAME, it.name)
            saveString(context, EMAIL, it.email)
            saveString(context, TOKEN, "Bearer $token")
        }
    }
}
}