package com.appsquare.task.helper

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.appsquare.task.BuildConfig
import com.appsquare.task.ui.activity.login.LoginActivity
import com.kayan.voicechat.helper.SharedHelper
import org.json.JSONObject

private const val TAG = "ResponseErrorHandler"

class ResponseErrorHandler {
    companion object {
        fun handleErrorMessage(
            isLogin: Boolean = false,
            activity: Activity,
            responseCode: Int?,
            message: String?
        ) {
            if (BuildConfig.DEBUG) {
                Log.d(
                    TAG,
                    "handleErrorMessage:\n isLogin $isLogin \n response code : $responseCode \n message : $message  "
                )
            }
            if (responseCode == 401 && !isLogin) {
                SharedHelper.clearUser(activity)
                activity.startActivity(Intent(activity, LoginActivity::class.java))
                activity.finishAffinity()
                return
            }
            if (responseCode in 401..499) {
                kotlin.runCatching {
                    JSONObject(message.toString())
                }.onSuccess {
                    Toast.makeText(
                        activity,
                        it.optString("message"),
                        Toast.LENGTH_SHORT
                    ).show()

                }.onFailure {
                    Toast.makeText(
                        activity,
                        message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                return
            }
            if (responseCode ?: 0 >= 500) {
                Toast.makeText(
                    activity,
                    "Temporary error",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }
            Toast.makeText(
                activity,
                message,
                Toast.LENGTH_SHORT
            ).show()
        }

    }
}