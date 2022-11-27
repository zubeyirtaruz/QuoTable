package com.zubeyirtaruz.quotable.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.huawei.hms.network.httpclient.Callback
import com.huawei.hms.network.httpclient.Response
import com.huawei.hms.network.httpclient.Submit
import com.zubeyirtaruz.quotable.model.Quote
import com.zubeyirtaruz.quotable.service.QuoteAPI
import com.zubeyirtaruz.quotable.service.QuoteAPIService
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class QuoteFeedViewModel: ViewModel() {

    private val apiClient by lazy {
        QuoteAPIService.getApiClient().create(QuoteAPI::class.java)
    }

    val quotes = MutableLiveData<List<Quote>>()
    val quoteError = MutableLiveData<Boolean>()
    val quoteLoading = MutableLiveData<Boolean>()

    fun getDataFromAPI() {
        quoteLoading.value = true
        apiClient.fetchQuotes().enqueue(object : Callback<String>() {
            override fun onResponse(p0: Submit<String>?, response: Response<String>?) {
                if (response?.isSuccessful == true) {
                    var quoteList = Json {
                        coerceInputValues = true
                    }.decodeFromString<List<Quote>>(response.body)

                    quoteList = setAnonymousAuthor(quoteList)
                    quotes.postValue(quoteList)
                    quoteError.postValue(false)
                    quoteLoading.postValue(false)
                }
            }
            override fun onFailure(p0: Submit<String>?, p1: Throwable?) {
                quoteLoading.postValue(false)
                quoteError.postValue(true)
            }
        })
    }

    private fun setAnonymousAuthor(quoteList: List<Quote>) : List<Quote> {

        for (i in quoteList.indices) {
            if (quoteList[i].author == null)
                quoteList[i].author = "Anonymous"
        }
        return  quoteList
    }
}





