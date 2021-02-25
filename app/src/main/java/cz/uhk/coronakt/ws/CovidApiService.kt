package cz.uhk.coronakt.ws

import cz.uhk.coronakt.model.CovidData
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

private val retrofit = Retrofit.Builder()
    .baseUrl("https://lide.uhk.cz/fim/ucitel/kozelto1/prog/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

interface CovidApiService {
    @GET("covid.php")
    fun getCovidData() : Call<CovidData>
}

object CovidApi {
    val covidApiService : CovidApiService by lazy {
        retrofit.create(CovidApiService::class.java)
    }
}