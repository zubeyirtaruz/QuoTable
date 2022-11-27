package com.zubeyirtaruz.quotable.service

import com.huawei.hms.network.httpclient.HttpClient
import com.huawei.hms.network.restclient.RestClient

const val BASE_URL = "https://type.fit/api/"

class QuoteAPIService {

    companion object {
        private var restClient: RestClient? = null

        fun getApiClient(): RestClient {
            val httpClient = HttpClient.Builder()
                .callTimeout(1000)
                .connectTimeout(10000)
                .build()

            if (restClient == null) {
                restClient = RestClient.Builder()
                    .baseUrl(BASE_URL)
                    .httpClient(httpClient)
                    .build()
            }
            return restClient!!
        }
    }
}