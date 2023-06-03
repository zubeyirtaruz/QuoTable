package com.zubeyirtaruz.quotable.view

import android.content.Intent
import android.content.Intent.ACTION_SEND
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.ads.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.huawei.agconnect.crash.AGConnectCrash
import com.huawei.hms.analytics.HiAnalytics
import com.huawei.hms.analytics.HiAnalyticsTools
import com.huawei.hms.mlsdk.MLAnalyzerFactory
import com.huawei.hms.mlsdk.common.MLFrame
import com.huawei.hms.mlsdk.text.MLLocalTextSetting
import com.huawei.hms.mlsdk.text.MLTextAnalyzer
import com.huawei.secure.android.common.intent.SafeIntent
import com.zubeyirtaruz.quotable.R
import com.zubeyirtaruz.quotable.viewmodel.QuoteViewModel
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private var mTextAnalyzer: MLTextAnalyzer? = null
    val viewModel: QuoteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController

        val bottomNavigationView = findViewById<BottomNavigationView
                >(R.id.bottomNav)
        bottomNavigationView.setupWithNavController(navController)
        NavigationUI.setupActionBarWithNavController(this,navController)

        if(getCroppedImage()!= null){
            createMLTextAnalyzer()
            getCroppedImage()?.let { asyncAnalyzeText(it) }
            navHostFragment.findNavController().navigate(QuoteFeedFragmentDirections.actionQuoteInfo("","",""))
        }

        MobileAds.initialize(this) {}
        HiAnalyticsTools.enableLog()
        val instance = HiAnalytics.getInstance(this)
        instance.setUserProfile("userKey","value")
        handleIntent(intent)
        enableCrashService()

        viewModel.setBannerBottom(this)
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
                navController.navigate(QuoteFeedFragmentDirections.actionQuoteInfo(it,"",""))
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun asyncAnalyzeText(bitmap: Bitmap) {
        if (mTextAnalyzer == null) {
            createMLTextAnalyzer()
        }
        val frame = MLFrame.fromBitmap(bitmap)
        val task = mTextAnalyzer?.asyncAnalyseFrame(frame)
        task?.addOnSuccessListener {
            viewModel.quote.value = it.stringValue
        }?.addOnFailureListener {
            println(it.message)
        }
    }

    private fun createMLTextAnalyzer() {
        val setting = MLLocalTextSetting.Factory()
            .setOCRMode(MLLocalTextSetting.OCR_DETECT_MODE)
            .setLanguage("en")
            .create()
        mTextAnalyzer = MLAnalyzerFactory.getInstance().getLocalTextAnalyzer(setting)
    }

    private fun stopTextAnalyzer(){
        try {
            if (mTextAnalyzer != null)
                mTextAnalyzer!!.stop()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    private fun getCroppedImage(): Bitmap? {
        val intent: Intent = SafeIntent(intent)
        return intent.getParcelableExtra<Parcelable>("croppedImage") as Bitmap?
    }

    override fun onDestroy() {
        super.onDestroy()
        stopTextAnalyzer()
    }

}