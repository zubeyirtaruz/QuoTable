package com.zubeyirtaruz.quotable.view

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.zubeyirtaruz.quotable.adapter.QuoteAdapter
import com.zubeyirtaruz.quotable.databinding.FragmentQuoteFeedBinding
import com.zubeyirtaruz.quotable.viewmodel.QuoteFeedViewModel

class QuoteFeedFragment : Fragment() {

    private var _binding: FragmentQuoteFeedBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: QuoteFeedViewModel
    private val quoteAdapter = QuoteAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentQuoteFeedBinding.inflate(inflater, container, false)

        binding.manualButton.setOnClickListener {
            findNavController().navigate(QuoteFeedFragmentDirections.actionQuoteInfo("","",""))
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this)[QuoteFeedViewModel::class.java]

        viewModel.getDataFromAPI()

        binding.rv.layoutManager = LinearLayoutManager(context)
        binding.rv.adapter = quoteAdapter

        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.rv.visibility = View.GONE
            binding.quoteError.visibility = View.GONE
            binding.quoteLoading.visibility = View.VISIBLE
            viewModel.getDataFromAPI()
            binding.swipeRefreshLayout.isRefreshing = false
        }

        observeLiveData()
    }

    private fun observeLiveData(){

        viewModel.quotes.observe(viewLifecycleOwner) { quotes ->
            quotes?.let {
                binding.rv.visibility = View.VISIBLE
                quoteAdapter.setData(quotes)
            }
        }

        viewModel.quoteError.observe(viewLifecycleOwner) { error ->
            error.let {
                if (it) {
                    binding.quoteError.visibility = View.VISIBLE
                    binding.rv.visibility = View.GONE
                } else {
                    binding.quoteError.visibility = View.GONE
                }
            }
        }

        viewModel.quoteLoading.observe(viewLifecycleOwner) { loading ->
            loading.let {
                if (it) {
                    binding.quoteLoading.visibility = View.VISIBLE
                    binding.rv.visibility = View.GONE
                    binding.quoteError.visibility = View.GONE
                } else {
                    binding.quoteLoading.visibility = View.GONE
                }
            }
        }



    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}