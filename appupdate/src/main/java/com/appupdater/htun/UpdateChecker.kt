package com.appupdater.htun

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

object UpdateChecker {

    private val client = OkHttpClient()

    fun checkForUpdate(activity: Activity, url: String, packageName: String) {
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle failure
            }

            @RequiresApi(Build.VERSION_CODES.P)
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseData = response.body?.string()
                    if (responseData != null) {
                        val gson = Gson()
                        val apiResponse = gson.fromJson(responseData, UpdateData::class.java)
                        val versionCode = apiResponse.versionCode

                        val manager = activity.packageManager
                        val info = manager.getPackageInfo(packageName, 0)
                        val currentVersion = info.longVersionCode

                        if (versionCode > currentVersion) {
                            activity.runOnUiThread {
                                UpdateDialogUtil.showUpdateDialog(activity, packageName)
                            }
                        }
                    }
                }
            }
        })
    }

}