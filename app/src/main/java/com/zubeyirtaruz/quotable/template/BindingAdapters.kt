package com.zubeyirtaruz.quotable

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.DimenRes
import androidx.annotation.LayoutRes
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.card.MaterialCardView
import com.zubeyirtaruz.quotable.model.Quote
import android.graphics.drawable.Drawable

import android.graphics.drawable.Animatable
import android.view.View
import android.widget.ImageView




@BindingAdapter("items", "adapter")
fun <T, VH: ViewHolder> RecyclerView.items(items: List<T>?, listAdapter: ListAdapter<T, VH>?) {
    if(items == null || listAdapter == null) return

    if(adapter == null ) adapter = listAdapter

    (adapter as ListAdapter<T, VH>).submitList(items)

}
@BindingAdapter("itemDecoration")
fun RecyclerView.itemDecoration(itemDecoration: ItemDecoration?) {

    if(itemDecoration == null) return
    if(itemDecorationCount == 0) addItemDecoration(itemDecoration)

}
@BindingAdapter("layoutId", "quoteInfo")
fun ViewGroup.childView(@LayoutRes layoutId: Int, quoteInfo: Quote) {

    if(childCount != 0) removeAllViews()

    val view = LayoutInflater.from(context).inflate(layoutId,this,false)

    view.findViewById<TextView>(R.id.quoteText).text = quoteInfo.quote
    view.findViewById<TextView>(R.id.authorText).text = quoteInfo.author
    view.findViewById<TextView>(R.id.bookText).text = quoteInfo.book

    addView(view)

}

@BindingAdapter("strokeWidthRes")
fun MaterialCardView.strokeWidthRes(@DimenRes widthRes: Int) {

    strokeWidth = resources.getDimensionPixelSize(widthRes)

}

@BindingAdapter("animateOnClick")
fun setAnimateOnClick(view: ImageView, backDrawable: Drawable?) {
    val front: Animatable = view.drawable as Animatable
    view.setOnClickListener {

        if (null == backDrawable) {
            front.start()
        } else {
            if (null == view.tag) {
                view.setImageDrawable(front as Drawable)
                front.start()
                view.tag = 0
            } else {
                view.setImageDrawable(backDrawable)
                (backDrawable as Animatable).start()
                view.tag = null
            }
        }

    }
}


