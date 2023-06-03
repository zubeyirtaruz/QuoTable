package com.zubeyirtaruz.quotable.service

import android.app.Application
import android.content.Context
import android.util.Log
import com.huawei.agconnect.AGCRoutePolicy
import com.huawei.agconnect.AGConnectInstance
import com.huawei.agconnect.AGConnectOptionsBuilder
import com.huawei.agconnect.cloud.database.AGConnectCloudDB
import com.huawei.agconnect.cloud.storage.core.AGCStorageManagement
import com.huawei.hms.mlsdk.common.MLApplication
import com.huawei.hms.network.NetworkKit

class App : Application() {

    private val TAG = "Application"

    override fun onCreate() {
        super.onCreate()
        initNetworkKit()
        CloudDBZoneWrapper.initAGConnectCloudDB(applicationContext)
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