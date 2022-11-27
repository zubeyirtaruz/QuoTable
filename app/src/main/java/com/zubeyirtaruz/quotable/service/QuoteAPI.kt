package com.zubeyirtaruz.quotable.service

import com.huawei.hms.network.httpclient.Submit
import com.huawei.hms.network.restclient.anno.GET

interface QuoteAPI {
    @GET("quotes")
    fun fetchQuotes(): Submit<String>
}