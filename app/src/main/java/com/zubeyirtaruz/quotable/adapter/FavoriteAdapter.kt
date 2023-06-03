package com.zubeyirtaruz.quotable.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.huawei.agconnect.auth.AGConnectAuth
import com.zubeyirtaruz.quotable.R
import com.zubeyirtaruz.quotable.databinding.MyFavoritesRowQuoteBinding
import com.zubeyirtaruz.quotable.model.FavoriteQuote
import com.zubeyirtaruz.quotable.service.CloudDBZoneWrapper

class FavoriteAdapter(context: Context, user: MutableList<FavoriteQuote>): RecyclerView.Adapter<FavoriteAdapter.MyFavoriteViewHolder>() {

    private var data: List<FavoriteQuote>? = null
    private var mContext: Context? = null
    private val user = AGConnectAuth.getInstance().currentUser

    init {
        data = user
        mContext = context
    }

    class MyFavoriteViewHolder(var binding: MyFavoritesRowQuoteBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyFavoriteViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<MyFavoritesRowQuoteBinding>(inflater,
            R.layout.my_favorites_row_quote,parent,false)
        return MyFavoriteViewHolder(view)

    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyFavoriteViewHolder, position: Int){

        if(position % 2 == 1){
            holder.binding.rowCardView.setCardBackgroundColor(Color.WHITE)
            holder.binding.quoteText.setTextColor(R.color.charcoal)
            holder.binding.quoteAuthor.setTextColor(R.color.charcoal)
        }

        holder.binding.quote = data!! [position]
        clickedGarbage(holder.binding.itemGarbage,position)

    }

    override fun getItemCount(): Int {
        return data!!.size
    }

    private fun clickedGarbage(v: ImageButton, position: Int) {
        v.setOnClickListener {
            try {
                val ob = FavoriteQuote(user.uid,data!![position].quote,data!![position].author)
                CloudDBZoneWrapper().deleteQuote(listOf(ob))
            }catch (e: Exception) {
                Log.i("Error", "Error:$e")
            }

        }

    }




}