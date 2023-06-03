package com.zubeyirtaruz.quotable.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.huawei.hms.network.httpclient.Callback
import com.huawei.hms.network.httpclient.Response
import com.huawei.hms.network.httpclient.Submit
import com.zubeyirtaruz.quotable.R
import com.zubeyirtaruz.quotable.model.Author
import com.zubeyirtaruz.quotable.model.Template
import com.zubeyirtaruz.quotable.service.QuoteAPI
import com.zubeyirtaruz.quotable.service.QuoteAPIService
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class AuthorViewModel: ViewModel() {

    private val apiClient by lazy {
        QuoteAPIService.getApiClient().create(QuoteAPI::class.java)
    }

    val authors = MutableLiveData<List<Author>>()
    val authorError = MutableLiveData<Boolean>()
    val authorLoading = MutableLiveData<Boolean>()

    @kotlinx.serialization.ExperimentalSerializationApi
    fun getDataFromAPI() {
        authorLoading.value = true
        apiClient.fetchAuthors().enqueue(object : Callback<String>() {

            private val json = Json {
                ignoreUnknownKeys = true
            }

            override fun onResponse(p0: Submit<String>?, response: Response<String>?) {
                if (response?.isSuccessful == true) {
                    var authorList = json.decodeFromString<List<Author>>(response.body)

                    authors.postValue(authorList)
                    authorError.postValue(false)
                    authorLoading.postValue(false)
                }
            }
            override fun onFailure(p0: Submit<String>?, p1: Throwable?) {
                authorLoading.postValue(false)
                authorError.postValue(true)
            }
        })
    }





}




