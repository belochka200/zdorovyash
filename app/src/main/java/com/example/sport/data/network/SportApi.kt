package com.example.sport.data.network

import android.util.Log
import com.example.sport.data.models.SportCard
import com.example.sport.data.models.SportCardItem
import com.example.sport.data.models.SportItem
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import retrofit2.http.GET
import java.net.URL

const val BASE_URL = "http://10.45.19.103:8000"

//private val moshi = Moshi.Builder()
//    .add(KotlinJsonAdapterFactory())
//    .build()
//
//private val retrofit = Retrofit.Builder()
//    .addConverterFactory(MoshiConverterFactory.create(moshi))
//    .baseUrl(BASE_URL)
//    .build()

private interface SportApi {
//    @GET("sports")
    suspend fun loadSportItems(): List<SportCard>
}

class SportApiImpl : SportApi {
    override suspend fun loadSportItems(): List<SportCard> {
//        val response = URL("${BASE_URL}sports").readText()
//        Log.d("Response", response)
//        try {
//            Log.d("Answer", Json.decodeFromString(response))
//        } catch (e: Exception) {
//            Log.e("Answer", e.toString())
//        }
        return emptyList()
    }
}