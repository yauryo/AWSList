package com.yaury.awslist.data

import com.yaury.awslist.model.MobileList
import retrofit2.http.GET

interface MobileApi {
    @GET("hiring.json")
    suspend fun getMobiles(): MobileList

    companion object {
        const val BASE_URL = "https://fetch-hiring.s3.amazonaws.com/"
    }

}