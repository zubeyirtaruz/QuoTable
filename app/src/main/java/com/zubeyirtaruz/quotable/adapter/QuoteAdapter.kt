package com.zubeyirtaruz.quotable.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.zubeyirtaruz.quotable.databinding.RowQuoteBinding
import com.zubeyirtaruz.quotable.util.QuoteDiffUtil
import com.zubeyirtaruz.quotable.model.Quote
import com.zubeyirtaruz.quotable.view.QuoteFeedFragmentDirections

class QuoteAdapter(private var quoteList: List<Quote>) : RecyclerView.Adapter<QuoteAdapter.QuoteViewHolder>() {

    class QuoteViewHolder(val binding: RowQuoteBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder {
        return QuoteViewHolder(
            RowQuoteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {

        if(position % 2 == 1){
            holder.binding.rowCardView.setCardBackgroundColor(Color.GRAY)
            holder.binding.quoteText.setTextColor(Color.WHITE)
            holder.binding.quoteAuthor.setTextColor(Color.WHITE)
        }else{
            holder.binding.rowCardView.setCardBackgroundColor(Color.WHITE)
            holder.binding.quoteText.setTextColor(Color.GRAY)
            holder.binding.quoteAuthor.setTextColor(Color.GRAY)
        }

        holder.binding.quote = quoteList [position]

        holder.binding.rowCardView.setOnClickListener {
            onQuoteClicked(holder)
        }
    }

    override fun getItemCount(): Int = quoteList.size

    fun setData(newUserList: List<Quote>) {
        val diffUtil = QuoteDiffUtil(quoteList, newUserList)
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        quoteList = newUserList
        diffResults.dispatchUpdatesTo(this)
    }

     private fun onQuoteClicked(holder: QuoteViewHolder) {
        val text = holder.binding.quoteText.text.toString()
        val author = holder.binding.quoteAuthor.text.toString()
        val action = QuoteFeedFragmentDirections.actionQuoteInfo(text,author)
        Navigation.findNavController(holder.binding.rowCardView).navigate(action)
    }

}