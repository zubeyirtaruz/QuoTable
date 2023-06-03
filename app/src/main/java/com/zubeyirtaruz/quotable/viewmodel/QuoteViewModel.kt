package com.zubeyirtaruz.quotable.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.huawei.hms.ads.banner.BannerView
import com.zubeyirtaruz.quotable.BannerAdClass
import com.zubeyirtaruz.quotable.R
import com.zubeyirtaruz.quotable.adapter.TemplateAdapter
import com.zubeyirtaruz.quotable.model.Quote
import com.zubeyirtaruz.quotable.model.Template

class QuoteViewModel: ViewModel(), TemplateAdapter.TemplateItemClickListener {

    val quote = MutableLiveData("")
    val author = MutableLiveData("")
    val book = MutableLiveData("")
    val bannerBottom = MutableLiveData<BannerView>()
    val templates = MutableLiveData(
        listOf(
        Template(layoutId = R.layout.template_1, vip = false,isSelected = true),
        Template(layoutId = R.layout.template_2,vip = true, isSelected = false),
        Template(layoutId = R.layout.template_3,vip = true, isSelected = false),
            /*
        Template(layoutId = R.layout.template_4,vip = true, isSelected = false),
        Template(layoutId = R.layout.template_5,vip = true, isSelected = false),
        Template(layoutId = R.layout.template_6,vip = true, isSelected = false),
        Template(layoutId = R.layout.template_7,vip = true, isSelected = false),
        Template(layoutId = R.layout.template_8,vip = true, isSelected = false),
        Template(layoutId = R.layout.template_9,vip = true, isSelected = false),
        Template(layoutId = R.layout.template_10,vip = true, isSelected = false),
        */
        )
    )

    var selectedTemplate = templates.value!![0]

    val quoteInfo get() = Quote(
        quote = quote.value!!,
        author = author.value!!,
        book = book.value!!,
        imageUrl = null
    )

    fun onIntentWithTextExtra(textExtra: String) {
        quote.value = textExtra
    }

    override fun onItemClick(template: Template) {

        templates.value = templates.value!!.map {
            if(it.layoutId == template.layoutId){
                it.copy(isSelected = true)
            }else{
                it.copy(isSelected = false)
            }
        }
        selectedTemplate = template
    }

    fun setBannerBottom(context: Context){
        bannerBottom.value = BannerAdClass().setBannerAd(context)
    }
}