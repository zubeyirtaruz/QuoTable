package com.zubeyirtaruz.quotable.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.huawei.hms.analytics.HiAnalytics
import com.huawei.hms.analytics.HiAnalyticsInstance
import com.huawei.hms.analytics.HiAnalyticsTools
import com.zubeyirtaruz.quotable.RewardAdClass
import com.zubeyirtaruz.quotable.databinding.FragmentTemplateBinding
import com.zubeyirtaruz.quotable.template.TemplateViewBinding
import com.zubeyirtaruz.quotable.util.CustomSharedPreferences
import com.zubeyirtaruz.quotable.viewmodel.QuoteViewModel

class TemplateFragment : Fragment() {

    private var _binding: FragmentTemplateBinding? = null
    private val binding get() = _binding!!

    private lateinit var instance: HiAnalyticsInstance
    private val bundle = Bundle()
    private val CUSTOM_EVENT_FEEDBACK = "CustomTemplateEventFeedback"

    val viewModel: QuoteViewModel by activityViewModels()

    private val customSharedPreferences = CustomSharedPreferences()
    private val vipTemplateFreeUsagePeriod = 60 * 1000 * 1000 * 1000L  // 1440 minutes = 1 day  1440 * 60 * 1000 * 1000 * 1000L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        HiAnalyticsTools.enableLog()
        instance = HiAnalytics.getInstance(this.requireActivity())
        instance.setAnalyticsEnabled(true)

        _binding = FragmentTemplateBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.viewBinding = TemplateViewBinding(viewModel)
        binding.lifecycleOwner = viewLifecycleOwner


        binding.nextButton.setOnClickListener {

            reportCustomTemplateEventFeedback(
                viewModel.selectedTemplate.layoutId.toString(),
                viewModel.selectedTemplate.vip.toString()
            )

            if(viewModel.selectedTemplate.vip){
                val updateTime = customSharedPreferences.getTimeSharedPreference(
                    requireContext(),
                    viewModel.selectedTemplate.layoutId.toString(),
                    0)

                if(updateTime != 0L && System.nanoTime() - updateTime < vipTemplateFreeUsagePeriod){
                    adFreePass()
                }else{
                        val rewardAdClass = RewardAdClass(this)
                        rewardAdClass.createRewardAd()
                        rewardAdClass.loadRewardAd(viewModel.selectedTemplate.layoutId)
                }
            }else{
                adFreePass()
            }
        }


        return binding.root
    }

    private fun adFreePass() = findNavController().navigate(TemplateFragmentDirections.actionShare())

    private fun reportCustomTemplateEventFeedback(layoutId: String, vip: String) {
        bundle.putString("templateID", layoutId)
        bundle.putString("vip", vip)
        instance.onEvent(CUSTOM_EVENT_FEEDBACK, bundle)
        Log.i("INFO","Feedback has been sent")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}