package com.zubeyirtaruz.quotable.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.huawei.agconnect.auth.AGConnectAuth
import com.zubeyirtaruz.quotable.R
import com.zubeyirtaruz.quotable.databinding.RowQuoteBinding
import com.zubeyirtaruz.quotable.model.FavoriteQuote
import com.zubeyirtaruz.quotable.util.QuoteDiffUtil
import com.zubeyirtaruz.quotable.model.Quote
import com.zubeyirtaruz.quotable.service.CloudDBZoneWrapper
import com.zubeyirtaruz.quotable.view.QuoteFeedFragmentDirections

class QuoteAdapter(private var quoteList: List<Quote>) : RecyclerView.Adapter<QuoteAdapter.QuoteViewHolder>(){

    private val user = AGConnectAuth.getInstance().currentUser

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

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {

        holder.setIsRecyclable(false)

        if(position % 2 == 1){
            holder.binding.rowCardView.setCardBackgroundColor(Color.WHITE)
            holder.binding.quoteText.setTextColor(R.color.charcoal)
            holder.binding.quoteAuthor.setTextColor(R.color.charcoal)
        }

        holder.binding.quote = quoteList [position]

        holder.binding.itemStar.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                try {
                    val ob = FavoriteQuote(user.uid,holder.binding.quoteText.text.toString(),holder.binding.quoteAuthor.text.toString())
                    CloudDBZoneWrapper().insertQuote(ob)
                }catch (e: Exception) {
                    Log.i("Error", "Error:$e")
                }

            }else{
                try {
                    val ob = FavoriteQuote(user.uid,holder.binding.quoteText.text.toString(),holder.binding.quoteAuthor.text.toString())
                    CloudDBZoneWrapper().deleteQuote(listOf(ob))
                }catch (e: Exception) {
                    Log.i("Error", "Error:$e")
                }
            }
        }



        holder.binding.rowCardView.setOnClickListener {
            onQuoteClicked(holder)
        }

    }

    override fun getItemCount(): Int = quoteList.size

    fun setData(newQuoteList: List<Quote>) {
        val diffUtil = QuoteDiffUtil(quoteList, newQuoteList)
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        quoteList = newQuoteList
        diffResults.dispatchUpdatesTo(this)

        CloudDBZoneWrapper().openCloudDBZone()
    }

     private fun onQuoteClicked(holder: QuoteViewHolder) {
        val text = holder.binding.quoteText.text.toString()
        val author = holder.binding.quoteAuthor.text.toString()
        val action = QuoteFeedFragmentDirections.actionQuoteInfo(text,author,"")
        Navigation.findNavController(holder.binding.rowCardView).navigate(action)
    }

}