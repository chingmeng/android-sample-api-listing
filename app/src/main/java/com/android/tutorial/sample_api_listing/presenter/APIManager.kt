package com.android.tutorial.sample_api_listing.presenter

import android.util.Log
import com.android.tutorial.sample_api_listing.models.Shop
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

object APIManager {

    private const val TAG = "--APIManager"
    private const val BASE_URL = "https://2826536e-d00d-4575-b35e-5562354bf840.mock.pstmn.io/"

    interface APIService {
        @GET("get")
        fun get(): Call<JsonArray>
    }

    fun getListing(completion: ((Boolean, Array<Shop>?) -> Unit)) {
        getRetrofit().apply {

            val call = this.create(APIService::class.java).get()

            call.enqueue(object : Callback<JsonArray> {
                override fun onResponse(call: Call<JsonArray>, response: Response<JsonArray>) {
                    if (response.isSuccessful) {
                        Log.d(TAG, "Get: Success \n${response.body()}")

                        val jsonResponse = response.body() as JsonArray
                        val shops = Gson().fromJson(jsonResponse, Array<Shop>::class.java)

                        completion.invoke(true, shops)

                    } else {
                        Log.d(TAG, "Get: Failed $response")
                        completion.invoke(false, null)
                    }
                }

                override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                    Log.d(TAG, "Get: Failed ${t.localizedMessage}")
                    completion.invoke(false, null)
                }
            })
        }
    }

    private fun getRetrofit(): Retrofit {
        // Create, config and return retrofit instance
        return Retrofit.Builder()
            .baseUrl(this.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}