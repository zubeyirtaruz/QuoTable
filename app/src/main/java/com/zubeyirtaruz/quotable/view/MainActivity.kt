package com.zubeyirtaruz.quotable.view

import android.content.Intent
import android.content.Intent.ACTION_SEND
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.gms.ads.*
import com.huawei.agconnect.crash.AGConnectCrash
import com.huawei.hms.analytics.HiAnalytics
import com.huawei.hms.analytics.HiAnalyticsTools
import com.zubeyirtaruz.quotable.R
import com.zubeyirtaruz.quotable.viewmodel.QuoteViewModel


class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    val viewModel: QuoteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MobileAds.initialize(this) {}

        HiAnalyticsTools.enableLog()
        val instance = HiAnalytics.getInstance(this)
        instance.setUserProfile("userKey","value")

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController

        NavigationUI.setupActionBarWithNavController(this,navController)

        handleIntent(intent)

        enableCrashService()
    }

    private fun enableCrashService() {
        AGConnectCrash.getInstance().enableCrashCollection(true)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {

        if(intent?.action == ACTION_SEND && intent.type == "text/plain"){
            intent.getStringExtra(Intent.EXTRA_TEXT)?.let {
                viewModel.onIntentWithTextExtra(it)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

}