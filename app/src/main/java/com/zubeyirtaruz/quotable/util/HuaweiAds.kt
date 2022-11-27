package com.zubeyirtaruz.quotable

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.huawei.hms.ads.AdParam
import com.huawei.hms.ads.BannerAdSize
import com.huawei.hms.ads.banner.BannerView
import com.huawei.hms.ads.reward.Reward
import com.huawei.hms.ads.reward.RewardAd
import com.huawei.hms.ads.reward.RewardAdLoadListener
import com.huawei.hms.ads.reward.RewardAdStatusListener
import com.zubeyirtaruz.quotable.util.CustomSharedPreferences

class RewardAdClass(private val fragment: Fragment) {

    private var customSharedPreferences = CustomSharedPreferences()

    private val AD_ID = "testx9dtjwj8hp"
    private var rewardAd: RewardAd? = null
    private val TAG = "RewardAdClass"

    fun createRewardAd() {
        rewardAd = RewardAd(fragment.activity, AD_ID)
    }

    fun loadRewardAd(layoutId: Int) {

        if (rewardAd == null) {
            rewardAd = RewardAd(fragment.requireContext(), AD_ID)
        }
        val listener: RewardAdLoadListener = object : RewardAdLoadListener() {
            override fun onRewardedLoaded() {
                Log.i(TAG,"Reward was added successfully")
                rewardAdShow(layoutId)
            }
            override fun onRewardAdFailedToLoad(errorCode: Int) {
                Log.e(TAG,errorCode.toString())
            }
        }
        rewardAd!!.loadAd(AdParam.Builder().build(),listener)
    }

     fun rewardAdShow(layoutId: Int) {
         var amount = 0
        if (rewardAd!!.isLoaded) {
            rewardAd!!.show(fragment.activity, object : RewardAdStatusListener() {
                override fun onRewardAdOpened() {
                    Log.i(TAG,"Reward ad opened")
                }
                override fun onRewardAdFailedToShow(errorCode: Int) {
                    Log.e(TAG,errorCode.toString())
                }
                override fun onRewardAdClosed() {
                    Log.i(TAG,"Reward ad closed")
                    Log.i(TAG,amount.toString())

                    if(amount == 5)
                        Toast.makeText(fragment.requireContext(),
                            "Premium template unlocked, you can use it by pressing the next button",
                            Toast.LENGTH_SHORT).show()
                    else
                        Toast.makeText(fragment.requireContext(),
                            "You need to watch the full ad to unlock the premium template",
                            Toast.LENGTH_SHORT).show()
                }
                override fun onRewarded(reward: Reward) {
                    Log.i(TAG,"Won reward")
                    amount = reward.amount
                    customSharedPreferences.setTimeSharedPreference(
                        fragment.requireContext(),layoutId.toString(),
                        System.nanoTime() )
                }
            })
        }
    }
}

class BannerAdClass {

    private val AD_ID = "testw6vs28auh3"

    fun setBannerAd(context: Context): BannerView {
        val bottomBanner = BannerView(context)
        val adParam = AdParam.Builder().build()
        bottomBanner.adId = AD_ID
        bottomBanner.bannerAdSize = BannerAdSize.BANNER_SIZE_360_57
        bottomBanner.loadAd(adParam)

        return  bottomBanner
    }
}