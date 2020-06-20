package com.epita.covidata2019

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface GlobalInfoHolder {
    @GET("summary")
    fun getinfo() : Call<SummaryGlobal>

    @GET("total/country/{name}")
    fun getcountryname(@Path("name") name : String) : Call<List<CasesByCountry>>

    @GET("total/country/{name}")
    fun getGraphElements(@Path("name") name : String) : Call<GraphElements>
}