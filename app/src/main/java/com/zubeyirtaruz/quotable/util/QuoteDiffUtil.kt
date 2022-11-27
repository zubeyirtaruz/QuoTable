package com.zubeyirtaruz.quotable.util

import androidx.recyclerview.widget.DiffUtil
import com.zubeyirtaruz.quotable.model.Quote

class QuoteDiffUtil(
    private val oldList: List<Quote>,
    private val newList: List<Quote>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].quote.equals(newList[newItemPosition].quote)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

}