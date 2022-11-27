package com.zubeyirtaruz.quotable.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.zubeyirtaruz.quotable.R
import com.zubeyirtaruz.quotable.databinding.ItemTemplateBinding
import com.zubeyirtaruz.quotable.model.Quote
import com.zubeyirtaruz.quotable.model.Template

class TemplateAdapter(
    private val quoteInfo: Quote,
    private val listener: TemplateItemClickListener
    ): ListAdapter<Template,TemplateAdapter.TemplateHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TemplateHolder {
        val binding: ItemTemplateBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_template,
            parent,
            false
        )
        return TemplateHolder(binding,quoteInfo,listener)
    }

    override fun onBindViewHolder(holder: TemplateHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class TemplateHolder(
        private val binding: ItemTemplateBinding,
        private val quoteInfo: Quote,
        private val listener: TemplateItemClickListener
    ) : ViewHolder(binding.root){
        fun bind(template: Template){
            binding.item = template
            binding.quoteInfo = quoteInfo
            binding.listener = listener
            binding.executePendingBindings()
        }
    }

    interface TemplateItemClickListener {
        fun onItemClick(template: Template)
    }

    companion object{
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Template>() {
            override fun areItemsTheSame(oldItem: Template, newItem: Template) = oldItem.layoutId == newItem.layoutId
            override fun areContentsTheSame(oldItem: Template, newItem: Template) = oldItem == newItem
        }
    }
}