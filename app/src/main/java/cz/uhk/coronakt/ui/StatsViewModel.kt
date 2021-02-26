package cz.uhk.coronakt.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import cz.uhk.coronakt.db.CovidDatabase
import cz.uhk.coronakt.model.CovidData
import cz.uhk.coronakt.ws.CovidApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import java.util.concurrent.Executors

/**
 * ViewModel drzici stav aplikace(aktivity)
 */
class StatsViewModel(application: Application) : AndroidViewModel(application) {
    //livedata - obserovana data, inicialiace az pri prvnim pouziti
    val data : MutableLiveData<CovidData> by lazy {
        MutableLiveData<CovidData>()
    }

    /**
     * Stahnout data pomoci Retrofit, callback na konci aktualizuje stav
     */
    fun downloadData() {
        //volame sluzbu
        CovidApi.covidApiService.getCovidData().enqueue(object : Callback<CovidData> {
            override fun onResponse(call: Call<CovidData>, response: Response<CovidData>) {
                val covidData = response.body()
                covidData?.data?.reverse()
                data.postValue(covidData)
                //stazena data propsat do DB
                Executors.newSingleThreadExecutor().submit {
                    updateDb()
                }
            }

            override fun onFailure(call: Call<CovidData>, t: Throwable) {
            }

        })
    }

    /**
     * Update DB, prepis ze stavu(livedat) do DB
     */
    private fun updateDb() {
        val db = CovidDatabase.getInstance(getApplication())
        val dao = db.dayStatsDao()
        data.value?.data?.let { dao.insertAll(*it.toTypedArray()) }
    }

    /**
     * Nacteni stavu z DB
     */
    fun loadDbData() {
        val dao = CovidDatabase.getInstance(getApplication()).dayStatsDao()
        val covidData = CovidData(Date(), dao.getAll().toMutableList())
        data.postValue(covidData)
    }
}