package com.zubeyirtaruz.quotable.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.*
import com.zubeyirtaruz.quotable.databinding.FragmentQuoteInfoBinding
import com.zubeyirtaruz.quotable.viewmodel.QuoteViewModel

class QuoteInfoFragment : Fragment() {

    private var _binding: FragmentQuoteInfoBinding? = null
    private val binding get() = _binding!!

    val viewModel: QuoteViewModel by activityViewModels()

    private val TAG = "AD STATUS"

    lateinit var mAdView : AdView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentQuoteInfoBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        clickNextButton()

        /*mAdView = binding.adView
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        mAdView.adListener = object: AdListener() {
            override fun onAdClicked() {
                Log.i(TAG,"AddClicked")
            }
            override fun onAdClosed() {
                Log.i(TAG,"AddClosed")
            }
            override fun onAdFailedToLoad(adError : LoadAdError) {
                Log.i(TAG, "FailedToLoad : $adError")
            }
            override fun onAdImpression() {
                Log.i(TAG,"AdImpression")
            }
            override fun onAdLoaded() {
                Log.i(TAG,"AddLoaded")
            }
            override fun onAdOpened() {
                Log.i(TAG,"AddOpened")
            }
        }
        */

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            viewModel.quote.value = QuoteInfoFragmentArgs.fromBundle(it).text
            viewModel.author.value = QuoteInfoFragmentArgs.fromBundle(it).author
            viewModel.book.value = QuoteInfoFragmentArgs.fromBundle(it).book
        }

        binding.bannerLayout.addView(viewModel.bannerBottom.value)


    }

    private fun clickNextButton() {
        binding.nextButton.setOnClickListener {
            findNavController().navigate(QuoteInfoFragmentDirections.actionTemplate())
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding.bannerLayout.removeAllViews()
        _binding = null
    }
}