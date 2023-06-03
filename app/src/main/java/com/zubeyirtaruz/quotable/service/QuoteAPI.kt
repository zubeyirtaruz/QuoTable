package com.zubeyirtaruz.quotable.service

import com.huawei.hms.network.httpclient.Submit
import com.huawei.hms.network.restclient.anno.GET
import com.huawei.hms.network.restclient.anno.Url

interface QuoteAPI {

    @GET("quotes/ef408a5946e81567b9a3407845a84266d2fcd4ea")
    fun fetchQuotes(): Submit<String>

    @GET("authors/ef408a5946e81567b9a3407845a84266d2fcd4ea")
    fun fetchAuthors(): Submit<String>

    @GET
    fun fetchWithUrl(@Url url:String): Submit<String>

    @GET("today")
    fun getDayQuote(): Submit<String>

}