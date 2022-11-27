package com.zubeyirtaruz.quotable.service

import android.app.Application
import android.util.Log
import com.huawei.hms.network.NetworkKit

class App : Application() {

    private val TAG = "Application"

    override fun onCreate() {
        super.onCreate()
        initNetworkKit()
    }

    private fun initNetworkKit() {
        NetworkKit.init(applicationContext, object : NetworkKit.Callback() {
            override fun onResult(result: Boolean) {
                if (result) {
                    Log.i(TAG, "Network Kit init success")
                } else {
                    Log.i(TAG, "Network Kit init failed")
                }
            }
        })
    }
}