package com.zubeyirtaruz.quotable.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.zubeyirtaruz.quotable.R
import com.zubeyirtaruz.quotable.databinding.RowAuthorBinding
import com.zubeyirtaruz.quotable.model.Author
import com.zubeyirtaruz.quotable.util.AuthorDiffUtil
import com.zubeyirtaruz.quotable.util.CustomSharedPreferences
import com.zubeyirtaruz.quotable.view.AuthorFragmentDirections

class AuthorAdapter(private var authorList: List<Author>) : RecyclerView.Adapter<AuthorAdapter.AuthorViewHolder>(), ItemClickListener {

    class AuthorViewHolder(val binding: RowAuthorBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AuthorViewHolder {
        return AuthorViewHolder(
            RowAuthorBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: AuthorViewHolder, position: Int) {

        holder.setIsRecyclable(false)
        holder.binding.author = authorList [position]
        holder.binding.listener = this


    }

    override fun getItemCount(): Int = authorList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newQuoteList: List<Author>) {
        val diffUtil = AuthorDiffUtil(authorList, newQuoteList)
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        authorList = newQuoteList
        diffResults.dispatchUpdatesTo(this)
        notifyDataSetChanged()

    }

    override fun onItemClicked(v: View) {
        v.findViewById<ImageView>(R.id.imageViewAuthorTick).visibility = View.VISIBLE
        val nameTittle = v.findViewById<TextView>(R.id.authorNameTittle)
        CustomSharedPreferences().setKeySharedPreference(v.context,"author",nameTittle.text.toString())
    }

}