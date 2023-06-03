package com.zubeyirtaruz.quotable.service

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.huawei.agconnect.AGCRoutePolicy
import com.huawei.agconnect.AGConnectInstance
import com.huawei.agconnect.AGConnectOptionsBuilder
import com.huawei.agconnect.auth.AGConnectAuth
import com.huawei.agconnect.cloud.database.*
import com.huawei.agconnect.cloud.database.exceptions.AGConnectCloudDBException
import com.zubeyirtaruz.quotable.model.FavoriteQuote

class CloudDBZoneWrapper {

    private var user = AGConnectAuth.getInstance().currentUser
    private var mCloudDB: AGConnectCloudDB = AGConnectCloudDB.getInstance()
    private var mConfig: CloudDBZoneConfig? = null
    private var mUiCallBack = UiCallBack.DEFAULT

    private val mSnapshotListener = OnSnapshotListener { cloudDBZoneSnapshot, e ->
        if (e != null) {
            Log.i(TAG, "onSnapshot: " + e.message)
            return@OnSnapshotListener
        }
        val snapshotObjects = cloudDBZoneSnapshot.snapshotObjects
        val favoriteQuoteList: MutableList<FavoriteQuote> = ArrayList()
        try {
            if (snapshotObjects != null) {
                while (snapshotObjects.hasNext()) {
                    val favoriteQuote = snapshotObjects.next()
                    favoriteQuoteList.add(favoriteQuote)
                }
            }
            mUiCallBack.onSubscribe(favoriteQuoteList)
        } catch (snapshotException: AGConnectCloudDBException) {
            Log.i(TAG, "onSnapshot:(getObject) " + snapshotException.message)
        } finally {
            cloudDBZoneSnapshot.release()
        }
    }

    fun createObjectType(context: Context) {
        try {
            val agcConnectOptions = AGConnectOptionsBuilder().setRoutePolicy(AGCRoutePolicy.GERMANY).build(context)
            val instance = AGConnectInstance.buildInstance(agcConnectOptions)
            mCloudDB = AGConnectCloudDB.getInstance(instance, AGConnectAuth.getInstance(instance))
            mCloudDB.createObjectType(ObjectTypeInfoHelper.getObjectTypeInfo())
            Log.i(TAG, "createObjectTypeSuccess ")
        } catch (e: AGConnectCloudDBException) {
            Log.i(TAG, "createObjectTypeError: " + e.message)
        }
    }

    fun openCloudDBZone() {
        mConfig = CloudDBZoneConfig("QuoTable",
            CloudDBZoneConfig.CloudDBZoneSyncProperty.CLOUDDBZONE_CLOUD_CACHE,
            CloudDBZoneConfig.CloudDBZoneAccessProperty.CLOUDDBZONE_PUBLIC)
        mConfig!!.persistenceEnabled = true
        val task = mCloudDB.openCloudDBZone2(mConfig!!, true)
        task?.addOnSuccessListener {
            Log.i(TAG, "Open cloudDBZone success")
            mCloudDBZone = it
        }?.addOnFailureListener {
            Log.w(TAG, "Open cloudDBZone failed for " + it.message)
        }
    }

    fun closeCloudDBZone() {
        try {
            mCloudDB.closeCloudDBZone(mCloudDBZone)
            Log.i(TAG, "closedCloudDBZone")
        } catch (e: AGConnectCloudDBException) {
            Log.i(TAG, "closeCloudDBZone: " + e.message)
        }
    }

    fun addCallBacks(uiCallBack: UiCallBack) {
        mUiCallBack = uiCallBack
    }

    @SuppressLint("SuspiciousIndentation")
    fun addSubscription() {
        if (mCloudDBZone == null) {
            Log.i(TAG, "CloudDBZone is null, try re-open it")
            return
        }
        try {
            val snapshotQuery: CloudDBZoneQuery<FavoriteQuote> =
                CloudDBZoneQuery.where(FavoriteQuote::class.java).equalTo("userUid", user.uid)

                mCloudDBZone!!.subscribeSnapshot(
                snapshotQuery,
                CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY,
                mSnapshotListener
            )
        } catch (e: AGConnectCloudDBException) {
            Log.i(TAG, "subscribeSnapshot: " + e.message)
        }
    }

    fun queryUsers(query: CloudDBZoneQuery<FavoriteQuote>) {
        if (mCloudDBZone == null) {
            Log.i(TAG, "CloudDBZone is null, try re-open it")
            return
        }

        val queryTask = mCloudDBZone!!.executeQuery(
            query,
            CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY
        )
        queryTask.addOnSuccessListener { snapshot -> processQueryResult(snapshot)
        }.addOnFailureListener {
            mUiCallBack.updateUiOnError("Query failed")
        }
    }

    private fun processQueryResult(snapshot: CloudDBZoneSnapshot<FavoriteQuote>) {
        val quoteInfoCursor = snapshot.snapshotObjects
        val quoteInfoList: MutableList<FavoriteQuote> = ArrayList()
        try {
            while (quoteInfoCursor.hasNext()) {
                val userInfo = quoteInfoCursor.next()
                quoteInfoList.add(userInfo)
            }
        } catch (e: AGConnectCloudDBException) {
            Log.i(TAG, "processQueryResult: " + e.message)
        } finally {
            snapshot.release()
        }
        mUiCallBack.onAddOrQuery(quoteInfoList)
    }

    fun insertQuote(quoteInfo: FavoriteQuote?) {
        if (mCloudDBZone == null) {
            Log.i(TAG, "CloudDBZone is null, try re-open it")
            return
        }
        val upsertTask = mCloudDBZone!!.executeUpsert(quoteInfo!!)
        upsertTask.addOnSuccessListener { cloudDBZoneResult ->
            Log.i(TAG, "$cloudDBZoneResult quote added")
        }.addOnFailureListener {
            mUiCallBack.updateUiOnError("Insert quote failed")
        }
    }

    fun deleteQuote(quoteInfoList: List<FavoriteQuote>?) {
        if (mCloudDBZone == null) {
            Log.i(TAG, "CloudDBZone is null, try re-open it")
            return
        }
        val deleteTask = mCloudDBZone!!.executeDelete(quoteInfoList!!)
        if (deleteTask.exception != null) {
            mUiCallBack.updateUiOnError("Delete quote failed")
            return
        }
        mUiCallBack.onDelete(quoteInfoList)
    }

    interface UiCallBack {
        fun onAddOrQuery(userInfoList: List<FavoriteQuote>)
        fun onSubscribe(userInfoList: List<FavoriteQuote>?)
        fun onDelete(userInfoList: List<FavoriteQuote>?)
        fun updateUiOnError(errorMessage: String?)

        companion object {
            val DEFAULT: UiCallBack = object : UiCallBack {
                override fun onAddOrQuery(userInfoList: List<FavoriteQuote>) {
                    Log.i(TAG, "Using default onAddOrQuery")
                }

                override fun onSubscribe(userInfoList: List<FavoriteQuote>?) {
                    Log.i(TAG, "Using default onSubscribe")
                }

                override fun onDelete(userInfoList: List<FavoriteQuote>?) {
                    Log.i(TAG, "Quote deleted")
                }

                override fun updateUiOnError(errorMessage: String?) {
                    Log.i(TAG, errorMessage!!)
                }
            }
        }
    }

    companion object {
        private const val TAG = "CloudDBZoneWrapper"
        private var mCloudDBZone: CloudDBZone? = null

        fun initAGConnectCloudDB(context: Context?) {
            AGConnectCloudDB.initialize(context!!)
        }
    }
}
