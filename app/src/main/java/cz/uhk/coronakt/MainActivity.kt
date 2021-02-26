package cz.uhk.coronakt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.*
import androidx.activity.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import cz.uhk.coronakt.ui.CovidDataAdapter
import cz.uhk.coronakt.ui.StatsViewModel
import java.util.concurrent.Executors

/**
 * Hlavni aktivita aplikace, obsahuje RecyclerView, SwipeRefreshLayout
 */
class MainActivity : AppCompatActivity() {
    //SwipeRereshLayout - podpora refreshe gestem
    private lateinit var swipeLayout: SwipeRefreshLayout
    //state - drzi seznam itemu CovidData, jako obserovana LiveData
    private val model : StatsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recView = findViewById<RecyclerView>(R.id.rvItems)
        //nastaveni obseveru na model/state (livedata)
        model.data.observe(this) {
            recView.adapter = CovidDataAdapter(it)
        }
        //nastaveni akce Swipe na refresh dat
        swipeLayout = findViewById<SwipeRefreshLayout>(R.id.swipe_layout)
        swipeLayout.setOnRefreshListener {
            downloadData(null)
        }

        //uvodni nacteni dat z Room DB
        loadData()
    }

    //inicializace menu - obsahuje refresh akci
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater : MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    //stazeni dat z webu
    fun downloadData(menuItem: MenuItem?) {
        //sit nesmi byt na main-threadu
        val ex = Executors.newSingleThreadExecutor()
        swipeLayout.isRefreshing = true //ukazat ikonu refresh
        ex.execute {
            //stahnout data do modelu, diky observeru na live datech
            //automaticky na zaver refreshuje recyclerview
            model.downloadData()
            swipeLayout.isRefreshing = false //schovat refresh ikonu
        }
    }

    //nacteni dat z DB
    private fun loadData() {
        val ex = Executors.newSingleThreadExecutor()
        ex.execute {
            //opet diky observeru na livedatech modelu aktualizuje
            // se nakonec recyclerview
            model.loadDbData()
        }
    }
}