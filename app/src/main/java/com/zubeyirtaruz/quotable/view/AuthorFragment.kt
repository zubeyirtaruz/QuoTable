package com.zubeyirtaruz.quotable.view

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.zubeyirtaruz.quotable.R
import com.zubeyirtaruz.quotable.adapter.AuthorAdapter
import com.zubeyirtaruz.quotable.adapter.CategoryAdapter
import com.zubeyirtaruz.quotable.databinding.FragmentAuthorBinding
import com.zubeyirtaruz.quotable.model.Author
import com.zubeyirtaruz.quotable.model.Category
import com.zubeyirtaruz.quotable.util.CustomSharedPreferences
import com.zubeyirtaruz.quotable.viewmodel.AuthorViewModel
import kotlin.collections.ArrayList

class AuthorFragment : Fragment() {

    private var _binding: FragmentAuthorBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: AuthorViewModel
    private val authorAdapter = AuthorAdapter(arrayListOf())
    private var categoryAdapter = CategoryAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAuthorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this)[AuthorViewModel::class.java]

        binding.rvAuthor.layoutManager = LinearLayoutManager(context)
        binding.rvAuthor.adapter = authorAdapter

        viewModel.getDataFromAPI()
        observeLiveData()

        setCategoryData()
        binding.rvCategory.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        binding.rvCategory.adapter = categoryAdapter

        binding.buttonApply.setOnClickListener {
            val authorName = CustomSharedPreferences().getKeySharedPreference(requireContext(),"author","")
            val categoryName = CustomSharedPreferences().getKeySharedPreference(requireContext(),"category","")

            Navigation.findNavController(it).navigate(AuthorFragmentDirections.actionAuthorFragmentToQuoteFeedFragment(authorName.toString(),categoryName.toString()))
        }
    }

    private fun observeLiveData(){

        viewModel.authors.observe(viewLifecycleOwner) { authors ->
            authors?.let {
                binding.rvAuthor.visibility = View.VISIBLE
                authorAdapter.setData(authors)
            }
        }

        viewModel.authorError.observe(viewLifecycleOwner) { error ->
            error.let {
                if (it) {
                    binding.authorError.visibility = View.VISIBLE
                    binding.rvAuthor.visibility = View.GONE
                } else {
                    binding.authorError.visibility = View.GONE
                }
            }
        }

        viewModel.authorLoading.observe(viewLifecycleOwner) { loading ->
            loading.let {
                if (it) {
                    binding.authorLoading.visibility = View.VISIBLE
                    binding.rvAuthor.visibility = View.GONE
                    binding.authorError.visibility = View.GONE
                } else {
                    binding.authorLoading.visibility = View.GONE
                }
            }
        }

    }

    private fun setCategoryData() {

        categoryAdapter = CategoryAdapter(
            listOf(
                Category(categoryName = "Anxiety", isSelected = false),
                Category(categoryName = "Change", isSelected = false),
                Category(categoryName = "Choice", isSelected = false),
                Category(categoryName = "Confidence", isSelected = false),
                Category(categoryName = "Courage", isSelected = false),
                Category(categoryName = "Death", isSelected = false),
                Category(categoryName = "Dreams", isSelected = false),
                Category(categoryName = "Excellence", isSelected = false),
                Category(categoryName = "Failure", isSelected = false),
                Category(categoryName = "Fairness", isSelected = false),
                Category(categoryName = "Fear", isSelected = false),
                Category(categoryName = "Forgiveness", isSelected = false),
                Category(categoryName = "Freedom", isSelected = false),
                Category(categoryName = "Future", isSelected = false),
                Category(categoryName = "Happiness", isSelected = false),
                Category(categoryName = "Inspiration", isSelected = false),
                Category(categoryName = "Kindness", isSelected = false),
                Category(categoryName = "Leadership", isSelected = false),
                Category(categoryName = "Life", isSelected = false),
                Category(categoryName = "Living", isSelected = false),
                Category(categoryName = "Love", isSelected = false),
                Category(categoryName = "Pain", isSelected = false),
                Category(categoryName = "Past", isSelected = false),
                Category(categoryName = "Success", isSelected = false),
                Category(categoryName = "Time", isSelected = false),
                Category(categoryName = "Today", isSelected = false),
                Category(categoryName = "Truth", isSelected = false),
                Category(categoryName = "Work", isSelected = false)
            )
        )

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.author_search,menu)
        val item = menu.findItem(R.id.action_search)
        val searchView = item?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                filter(newText!!)
                return true
            }
        })
    }

    private fun filter(text: String) {

        val filteredList: ArrayList<Author> = ArrayList()
            for (item in viewModel.authors.value!!) {
                if (item.name!!.lowercase().contains(text.lowercase()))
                    filteredList.add(item)
            }
        if (filteredList.isEmpty()) {
            binding.searchResult.visibility = View.VISIBLE
            binding.rvAuthor.visibility = View.GONE
        } else {
            authorAdapter.setData(filteredList)
            binding.searchResult.visibility = View.GONE
            binding.rvAuthor.visibility = View.VISIBLE
        }
    }

}