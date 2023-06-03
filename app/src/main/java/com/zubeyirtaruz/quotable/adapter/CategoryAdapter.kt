package com.zubeyirtaruz.quotable.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zubeyirtaruz.quotable.R
import com.zubeyirtaruz.quotable.databinding.ItemCategoryBinding
import com.zubeyirtaruz.quotable.model.Category
import com.zubeyirtaruz.quotable.util.CustomSharedPreferences

class CategoryAdapter(private var categoryList: List<Category>) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>(), ItemClickListener {

    class CategoryViewHolder(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            ItemCategoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {

        holder.setIsRecyclable(false)
        holder.binding.category = categoryList [position]
        holder.binding.listener = this

    }

    override fun getItemCount(): Int = categoryList.size


    override fun onItemClicked(v: View) {
        v.findViewById<ImageView>(R.id.imageViewCategoryTick).visibility = View.VISIBLE
        val categoryName = v.findViewById<TextView>(R.id.categoryName)
        CustomSharedPreferences().setKeySharedPreference(v.context,"category",categoryName.text.toString().lowercase())
    }

}