package com.zubeyirtaruz.quotable.view

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.zubeyirtaruz.quotable.R
import com.zubeyirtaruz.quotable.adapter.QuoteAdapter
import com.zubeyirtaruz.quotable.databinding.FragmentQuoteFeedBinding
import com.zubeyirtaruz.quotable.service.CloudDBZoneWrapper
import com.zubeyirtaruz.quotable.util.CustomSharedPreferences
import com.zubeyirtaruz.quotable.viewmodel.QuoteFeedViewModel

class QuoteFeedFragment : Fragment(){

    private var _binding: FragmentQuoteFeedBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: QuoteFeedViewModel
    private val quoteAdapter = QuoteAdapter(arrayListOf())

    private var authorNameTittle: String? = "null"
    private var categoryName: String? = "null"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentQuoteFeedBinding.inflate(inflater, container, false)

        CloudDBZoneWrapper().createObjectType(requireContext())

        binding.manualButton.setOnClickListener {
            findNavController().navigate(QuoteFeedFragmentDirections.actionQuoteInfo("","",""))
        }

        return binding.root
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            authorNameTittle = QuoteFeedFragmentArgs.fromBundle(it).authorNameTittle
            categoryName = QuoteFeedFragmentArgs.fromBundle(it).categoryName
        }

        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.feed_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when(menuItem.itemId){
                    R.id.filter -> findNavController().navigate(QuoteFeedFragmentDirections.actionAuthor())
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        viewModel = ViewModelProviders.of(this)[QuoteFeedViewModel::class.java]

        if(authorNameTittle.equals("null") && categoryName.equals("null")){
            setRV()
            viewModel.getDataFromAPI()
        }

        else if(!authorNameTittle.isNullOrBlank() && !categoryName.isNullOrBlank()){
            setRV()
            viewModel.getWithUrlFromAPI("quotes/author/$authorNameTittle/ef408a5946e81567b9a3407845a84266d2fcd4ea&keyword=$categoryName")
        }

        else if(!authorNameTittle.isNullOrBlank()){
            if(categoryName.isNullOrBlank()){
                setRV()
                viewModel.getWithUrlFromAPI("quotes/author/$authorNameTittle/ef408a5946e81567b9a3407845a84266d2fcd4ea")
            }

        }

        else if(!categoryName.isNullOrBlank()){
            if(authorNameTittle.isNullOrBlank()){
                setRV()
                viewModel.getWithUrlFromAPI("quotes/ef408a5946e81567b9a3407845a84266d2fcd4ea&keyword=$categoryName")
            }
        }

        removeSharedPreference()
        observeLiveData()

        binding.swipeRefreshLayout.setOnRefreshListener {
            setRV()
            binding.rv.visibility = View.GONE
            binding.quoteError.visibility = View.GONE
            binding.quoteLoading.visibility = View.VISIBLE
            viewModel.getDataFromAPI()
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun removeSharedPreference(){
        CustomSharedPreferences().removeKeySharedPreference(requireContext(),"author")
        CustomSharedPreferences().removeKeySharedPreference(requireContext(),"category")
    }

    private fun setRV(){
        binding.rv.layoutManager = LinearLayoutManager(context)
        binding.rv.adapter = quoteAdapter
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