package cz.uhk.coronakt.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import cz.uhk.coronakt.db.CovidDatabase
import cz.uhk.coronakt.model.CovidData
import cz.uhk.coronakt.model.DayStats
import cz.uhk.coronakt.ws.CovidApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import java.util.concurrent.Executors
import java.util.logging.Logger

/**
 * ViewModel drzici stav aplikace(aktivity)
 */
class StatsViewModel(application: Application) : AndroidViewModel(application) {
    //livedata - observovana data, inicialiace az pri prvnim pouziti
    val data : MutableLiveData<List<DayStats>> by lazy {
        MutableLiveData<List<DayStats>>()
    }

    /**
     * Stahnout data pomoci Retrofit, callback na konci aktualizuje stav
     */
    fun downloadData() {
        //volame sluzbu
        CovidApi.covidApiService.getCovidData().enqueue(object : Callback<List<DayStats>> {
            override fun onResponse(call: Call<List<DayStats>>, response: Response<List<DayStats>>) {
                val covidData = response.body()?.asReversed()
                data.postValue(covidData)
                //stazena data propsat do DB
                Executors.newSingleThreadExecutor().submit {
                    updateDb()
                }
            }

            override fun onFailure(call: Call<List<DayStats>>, t: Throwable) {
                Log.e("DOWNLOAD", "ERROR", t)
            }

        })
    }

    /**
     * Update DB, prepis ze stavu(livedat) do DB
     */
    private fun updateDb() {
        val db = CovidDatabase.getInstance(getApplication())
        val dao = db.dayStatsDao()
        data.value?.let { dao.insertAll(*it.toTypedArray()) }
    }

    /**
     * Nacteni stavu z DB
     */
    fun loadDbData() {
        val dao = CovidDatabase.getInstance(getApplication()).dayStatsDao()
        val covidData = dao.getAll().toMutableList()
        data.postValue(covidData)
    }
}